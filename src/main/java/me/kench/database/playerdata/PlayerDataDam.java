package me.kench.database.playerdata;

import co.aikar.taskchain.TaskChain;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import me.glaremasters.guilds.guild.GuildHandler;
import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.jdbi.v3.core.Jdbi;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerDataDam {
    private final PlayerDataDao dao;
    private final LoadingCache<UUID, PlayerData> playerDataCache;

    public PlayerDataDam(Jdbi jdbi) {
        dao = jdbi.onDemand(PlayerDataDao.class);
        playerDataCache = Caffeine.newBuilder()
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .removalListener((UUID uniqueId, PlayerData data, RemovalCause cause) -> {
                    if (data == null) return;
                    dao.save(
                            data.getUniqueId(),
                            data.getClassesAsJson(),
                            data.getMaxSlots(),
                            data.getRankHuntress(),
                            data.getRankKnight(),
                            data.getRankWarrior(),
                            data.getRankNecromancer(),
                            data.getRankAssassin(),
                            data.getRankRogue()
                    );
                })
                .build(dao::load);
    }

    /**
     * Creates the database table required for operation. Does nothing if the table already exists.
     */
    public void createContainer() {
        RotMC.newChain()
                .async(dao::createContainer)
                .execute();
    }

    /**
     * Checks if a PlayerData entry exists for the given Player.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link TaskChain<Boolean>} to be used in further task execution
     */
    public TaskChain<Boolean> exists(UUID uniqueId) {
        return RotMC.newChain().asyncFirst(() -> dao.exists(uniqueId));
    }

    /**
     * Creates a new PlayerData entry for the given Player, so long as one does not already exist.
     *
     * @param uniqueId the unique id of the Player
     */
    public void create(UUID uniqueId) {
        exists(uniqueId)
                .abortIf(true)
                .async(() -> dao.create(uniqueId))
                .execute();
    }

    /**
     * Loads the PlayerData for the given Player. If it is present in the cache, it is immediately returned; otherwise,
     * the data is requested from the database and saved in the cache for fast future requests.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link TaskChain<PlayerData>} to be used in further task execution
     */
    public TaskChain<PlayerData> load(UUID uniqueId) {
        return RotMC
                .newChain()
                .asyncFirst(() -> playerDataCache.get(uniqueId));
    }

    /**
     * Exactly the same as {@link #load(UUID)} but aborts further tasks if the data is not found.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link TaskChain<PlayerData>} to be used in further task execution
     */
    public TaskChain<PlayerData> loadSafe(UUID uniqueId) {
        return load(uniqueId).abortIfNull();
    }

    /**
     * Loads a readonly copy of the PlayerData in the database for reporting purposes.
     * Bypasses cache.
     */
    public TaskChain<List<PlayerData>> loadAll() {
        return RotMC
                .newChain()
                .asyncFirst(() -> Collections.unmodifiableList(dao.loadAll()));
    }

    /**
     * Loads a readonly copy of the PlayerData in the database, flat maps all the classes together,
     * sorts them by fame, gets the top 10, and returns it as a TaskChain for further processing.
     *
     * @return A {@link TaskChain<Map<Integer, PlayerClass>>} to be used in further task execution.
     */
    public TaskChain<Map<PlayerClass, Long>> getTop10Classes() {
        return loadAll()
                .async(dataObjects -> dataObjects.stream()
                        .flatMap(data -> data.getClasses().stream())
                        .sorted(PlayerClass::compareTo)
                        .limit(10)
                        .collect(Collectors.toMap(playerClass -> playerClass, PlayerClass::getFame)));
    }

    /**
     * Loads a readonly copy of the PlayerData is the database, then iterates every guild member of every guild
     * gathering their playerdata from the readonly copy. It looks at each player class in the readonly copy and
     * sums all of the cumulative fame and assigns it to the guild. Finally, the guilds are sorted based on
     * cumulative fame and saved for further task execution.
     *
     * <strong>This method will be out of sync with the cache, until the cache updates! This is normal.</strong>
     *
     * @return A {@link TaskChain<Map<Guild, Long>>} to be used in further task execution.
     */
    public TaskChain<Map<Guild, Long>> getTop10Guilds() {
        GuildHandler guilds = Guilds.getApi().getGuildHandler();

        return loadAll()
                .async(dataObjects -> {
                    Map<Guild, Long> map = new HashMap<>();

                    guilds.getGuilds().forEach(guild -> {
                        guild.getMembers().forEach(member -> {
                            PlayerData memberData = dataObjects.stream()
                                    .filter(data -> data.getUniqueId().equals(member.getUuid()))
                                    .findFirst().orElse(null);

                            if (memberData != null) {
                                final long[] newFame = { 0L };

                                memberData.getClasses().forEach(playerClass -> {
                                    newFame[0] += playerClass.getFame();
                                });

                                if (map.containsKey(guild)) {
                                    map.put(guild, map.get(guild) + newFame[0]);
                                } else {
                                    map.put(guild, newFame[0]);
                                }
                            }
                        });
                    });

                    return map.entrySet().stream()
                            .sorted(Comparator.comparingLong(Map.Entry::getValue))
                            .limit(10)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                });
    }

    /**
     * Explicitly marks the PlayerData entry for the given Player for removal, which is then caught by the RemovalListener and saved
     * back to the database asynchronously.
     *
     * @param uniqueId the unique id of the Player
     */
    public void invalidate(UUID uniqueId) {
        playerDataCache.invalidate(uniqueId);
    }
}
