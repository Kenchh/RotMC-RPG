package me.kench.database.playerdata;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PlayerDataDam {
    private final PlayerDataDao dao;
    private final AsyncLoadingCache<UUID, PlayerData> playerDataCache;

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
                .buildAsync(dao::load);
    }

    /**
     * Creates the database table required for operation. Does nothing if the table already exists.
     */
    public void createContainer() {
        dao.createContainer();
    }

    /**
     * Checks if a PlayerData entry exists for the given Player.
     *
     * @param uniqueId the unique id of the Player
     * @return true or false
     */
    public boolean exists(UUID uniqueId) {
        return dao.exists(uniqueId);
    }

    /**
     * Creates a new PlayerData entry for the given Player, so long as one does not already exist.
     *
     * @param uniqueId the unique id of the Player
     */
    public void create(UUID uniqueId) {
        if (exists(uniqueId)) return;
        dao.create(uniqueId);
    }

    /**
     * Loads the PlayerData for the given Player. If it is present in the cache, it is immediately returned; otherwise,
     * the data is requested from the database and saved in the cache for fast future requests.
     *
     * @param uniqueId the unique id of the Player
     * @return a completable operation
     */
    public CompletableFuture<PlayerData> load(UUID uniqueId) {
        return playerDataCache.get(uniqueId);
    }

    /**
     * Explicitly marks the PlayerData entry for the given Player for removal, which is then caught by the RemovalListener and saved
     * back to the database asynchronously.
     *
     * @param uniqueId the unique id of the Player
     */
    public void invalidate(UUID uniqueId) {
        playerDataCache.synchronous().invalidate(uniqueId);
    }
}
