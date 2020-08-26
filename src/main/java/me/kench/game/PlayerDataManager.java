package me.kench.game;

import me.kench.RotMC;
import me.kench.player.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerDataManager {

    ArrayList<PlayerData> playerData = new ArrayList<>();

    public void registerPlayerData(Player player) {
        if(!hasPlayerData(player))
            playerData.add(new PlayerData(player));
    }

    public void unregisterPlayerData(Player player) {
        if(hasPlayerData(player))
            playerData.remove(RotMC.getPlayerData(player));
    }

    public PlayerData getPlayerData(OfflinePlayer p) {
        for(PlayerData pd : playerData) {
            if(pd.getPlayer().getUniqueId() == p.getUniqueId()) {
                return pd;
            }
        }
        return null;
    }

    public boolean hasPlayerData(Player player) {
        for(PlayerData pd : playerData) {
            if(pd.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

}
