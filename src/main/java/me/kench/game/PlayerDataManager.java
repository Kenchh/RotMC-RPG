package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public void registerPlayerData(Player player) {
        if (!hasPlayerData(player)) {
            playerData.put(player.getUniqueId(), RotMC.getInstance().getSqlManager().getPlayerData(player));
        }
    }

    public void unregisterPlayerData(Player player) {
        playerData.remove(player.getUniqueId());
    }

    public PlayerData getPlayerData(OfflinePlayer p) {
        return playerData.get(p.getUniqueId());
    }

    public boolean hasPlayerData(Player player) {
        return playerData.containsKey(player.getUniqueId());
    }

}
