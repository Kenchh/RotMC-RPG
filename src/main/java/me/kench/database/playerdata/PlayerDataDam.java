package me.kench.database.playerdata;

import co.aikar.taskchain.TaskChain;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import me.kench.RotMC;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;
import java.util.function.Function;

public class PlayerDataDam {
    private final PlayerDataDao dao;
    private final LoadingCache<UUID, PlayerData> playerDataCache;

    public PlayerDataDam(Jdbi jdbi) {
        dao = jdbi.onDemand(PlayerDataDao.class);
        playerDataCache = Caffeine.newBuilder()
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
     * Loads the PlayerData for the given Player as in {@link #load(UUID)}, applies a mutator, and then returns the
     * data to the cache for future lookups.
     *
     * @param uniqueId the unique id of the Player
     * @param mutator  the modifications to apply to the PlayerData
     */
    public void modify(UUID uniqueId, Function<PlayerData, PlayerData> mutator) {
        loadSafe(uniqueId)
                .async(mutator::apply)
                .asyncLast(mutated -> playerDataCache.put(uniqueId, mutated))
                .execute();
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
