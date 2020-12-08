package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public void registerPlayerData(Player player) {
        if(!hasPlayerData(player)) {
            playerData.put(player.getUniqueId(), RotMC.getInstance().getSqlManager().getPlayerData(player));
        }
    }

    public void unregisterPlayerData(Player player) {
        if(hasPlayerData(player)) {
            playerData.remove(RotMC.getPlayerData(player));
        }
    }

    public PlayerData getPlayerData(OfflinePlayer p) {
        for(PlayerData pd : playerData.values()) {
            if(pd.getPlayer().getUniqueId() == p.getUniqueId()) {
                return pd;
            }
        }
        return null;
    }

    public boolean hasPlayerData(Player player) {
        for(PlayerData pd : playerData.values()) {
            if(pd.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

}
