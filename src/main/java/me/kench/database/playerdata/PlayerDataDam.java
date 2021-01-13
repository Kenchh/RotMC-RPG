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
     * Creates a new PlayerData entry for the given Player, so long as one does not already exist.
     *
     * @param uniqueId the unique id of the Player
     */
    public void create(UUID uniqueId) {
        RotMC.newChain()
                .asyncFirst(() -> dao.exists(uniqueId))
                .abortIf(true)
                .async(() -> dao.create(uniqueId))
                .execute();
    }

    /**
     * Loads the PlayerData for the given Player. If it is present in the cache, it is immediately returned; otherwise,
     * the data is requested from the database and saved in the cache for fast future requests.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link PlayerData} to be used in further task execution
     */
    public PlayerData load(UUID uniqueId) {
        return playerDataCache.get(uniqueId);
    }


    /**
     * Returns a readonly copy of the entire PlayerData table. Should be called from an async context. If needed
     * from a sync context, use {@link #chainLoadAll()} to go async and show results sync.
     *
     * @return a {@link List< PlayerData >}
     */
    public List<PlayerData> loadAll() {
        return Collections.unmodifiableList(dao.loadAll());
    }

    /**
     * Returns a readonly copy of the top 10 classes in the PlayerData table. Should be called from an async context.
     * If needed from a sync context, use {@link #chainLoadTop10Classes()} to go async and show results sync.
     *
     * @return a {@link Map} of {@link PlayerClass} to {@link Long} (fame).
     */
    public Map<PlayerClass, Long> loadTop10Classes() {
        return loadAll().stream()
                .flatMap(data -> data.getClasses().stream())
                .sorted(PlayerClass::compareTo)
                .limit(10)
                .collect(Collectors.toMap(playerClass -> playerClass, PlayerClass::getFame));
    }

    /**
     * Returns a readonly copy of the top 10 guilds by first loading all PlayerData and then summing
     * all the fame for every guild member before finally sorting and limiting the end result.
     *
     * @return a {@link Map} of {@link Guild} to {@link Long} (fame).
     */
    public Map<Guild, Long> loadTop10Guilds() {
        GuildHandler guilds = Guilds.getApi().getGuildHandler();
        Map<Guild, Long> map = new HashMap<>();

        List<PlayerData> all = loadAll();

        guilds.getGuilds().forEach(guild -> {
            guild.getMembers().forEach(member -> {
                PlayerData memberData = all.stream()
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
    }

    /**
     * Exactly the same as {@link #load(UUID)} but forces calling from an async context, and you
     * can then sync to the main thread to do stuff with the result if necessary.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link TaskChain< PlayerData >} to be used in further task execution
     */
    public TaskChain<PlayerData> chainLoad(UUID uniqueId) {
        return RotMC.newChain()
                .asyncFirst(() -> load(uniqueId));
    }

    /**
     * Exactly the same as {@link #chainLoad(UUID)} but aborts further tasks if the data is not found.
     *
     * @param uniqueId the unique id of the Player
     * @return a {@link TaskChain< PlayerData >} to be used in further task execution
     */
    public TaskChain<PlayerData> chainLoadSafe(UUID uniqueId) {
        return chainLoad(uniqueId).abortIfNull();
    }

    /**
     * Exactly the same as {@link #loadAll()} but forces calling from an async context, and you
     * can then sync to the main thread to do stuff with the result if necessary.
     */
    public TaskChain<List<PlayerData>> chainLoadAll() {
        return RotMC
                .newChain()
                .asyncFirst(this::loadAll);
    }

    /**
     * Exactly the same as {@link #loadTop10Classes()} but forces calling from an async context, and you
     * can then sync to the main thread to do stuff with the result if necessary.
     */
    public TaskChain<Map<PlayerClass, Long>> chainLoadTop10Classes() {
        return RotMC.newChain().asyncFirst(this::loadTop10Classes);
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
    public TaskChain<Map<Guild, Long>> chainLoadTop10Guilds() {
        return RotMC.newChain().asyncFirst(this::loadTop10Guilds);
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
