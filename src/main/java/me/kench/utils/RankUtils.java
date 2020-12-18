package me.kench.utils;

import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RankUtils {

    public static int getCharacterRank(Player p, String classname) {

        int rank = 0;

        for (PlayerClass playerClass : RotMC.getPlayerData(p).getClasses()) {
            GameClass gameClass = playerClass.getGameClass();

            if (gameClass.getName().equalsIgnoreCase(classname)) {
                return getRankFromFame(playerClass);
            }

        }

        String key = "rank"+classname.substring(0, 1).toUpperCase()+classname.substring(1).toLowerCase();
        rank = Math.max(rank, RotMC.getInstance().getSqlManager().getValueOrDefaultFromDatabase(p, key, Integer.class, 0));

        return rank;
    }

    public static int getRankFromFame(PlayerClass pc) {
        if(pc.getXp() >= 100000) {
            return 5;
        }

        if(pc.getXp() >= 50000) {
            return 4;
        }

        if(pc.getXp() >= 15000) {
            return 3;
        }

        if(pc.getXp() >= 5000) {
            return 2;
        }

        if(pc.getXp() >= 1000) {
            return 1;
        }

        return 0;
    }

    public static int getOverallRank(Player p) {
        int rank = getCharacterRank(p, "Huntress")
                + getCharacterRank(p, "Rogue")
                + getCharacterRank(p, "Assassin")
                + getCharacterRank(p, "Warrior")
                + getCharacterRank(p, "Knight")
                + getCharacterRank(p, "Necromancer");

        return rank;
    }

    public static ChatColor getStarColor(PlayerData pd) {
        switch ((int) Math.floor(getOverallRank(pd.getPlayer())/5D)) {
            case 1:
                return ChatColor.WHITE;
            case 2:
                return ChatColor.AQUA;
            case 3:
                return ChatColor.DARK_AQUA;
            case 4:
                return ChatColor.BLUE;
            case 5:
                return ChatColor.DARK_BLUE;
            case 6:
                return ChatColor.YELLOW;
            default:
                return ChatColor.GRAY;
        }
    }

}
