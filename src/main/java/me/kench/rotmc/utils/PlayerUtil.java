package me.kench.rotmc.utils;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.player.PlayerMetadataKey;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.Consumer;

public class PlayerUtil {
    /**
     * Checks if a metadata key is stored on the target player
     *
     * @param key the metadata key to check
     * @param player the target player
     * @return true or false
     */
    public static boolean hasMetadata(PlayerMetadataKey key, Player player) {
        return player.hasMetadata(key.name());
    }

    /**
     * Adds data to a metadata key on the specified Player so we can check it later
     *
     * @param <T> the type of data being stored in the metadata
     * @param key The metadata key to add
     * @param player The player to add the metadata to
     */
    public static <T> void addMetadata(PlayerMetadataKey key, Player player, T data) {
        player.setMetadata(key.name(), new FixedMetadataValue(RotMcPlugin.getInstance(), data));
    }

    /**
     * Gets the first found entry for the specified {@link PlayerMetadataKey}.
     *
     * @param <T> the type of data being stored; method trusts programmer on type
     * @param key The metadata key to get
     * @param player The target player
     * @return the stored string or empty string if null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMetadata(PlayerMetadataKey key, Player player) {
        return (T) player.getMetadata(key.name()).get(0).value();
    }

    /**
     * Removes the specified metadata key from the specified player
     *
     * @param key The metadata key to remove
     * @param player The target player
     */
    public static void removeMetadata(PlayerMetadataKey key, Player player) {
        player.removeMetadata(key.name(), RotMcPlugin.getInstance());
    }

    /**
     * Performs the task specified by the consumer if a value exists for the specified key in the specified
     * player's metadata. After running, if it ran, the key will be removed from the player's metadata.
     *
     * @param <T> the type of data stored in the metadata
     * @param key the metadata key to check and remove if present
     * @param player the player to check
     * @param consumer the action to perform
     */
    public static <T> void doIfPresent(PlayerMetadataKey key, Player player, Consumer<T> consumer) {
        if (hasMetadata(key, player)) {
            consumer.accept(getMetadata(key, player));
            removeMetadata(key, player);
        }
    }
}
