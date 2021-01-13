package me.kench.utils;

import me.kench.database.playerdata.PlayerData;
import me.kench.player.PlayerClass;
import me.kench.player.RpgClass;
import org.bukkit.ChatColor;

public class RankUtils {
    public static int getCharacterRank(PlayerData data, RpgClass rpgClass) {
        // TODO: make sure ranks are appropriately calculated upon levelUp as they are NOT yet
        switch (rpgClass) {
            case KNIGHT:
                return data.getRankKnight();
            case WARRIOR:
                return data.getRankWarrior();
            case HUNTRESS:
                return data.getRankHuntress();
            case NECROMANCER:
                return data.getRankNecromancer();
            case ASSASSIN:
                return data.getRankAssassin();
            case ROGUE:
                return data.getRankRogue();
        }

        return -1;
    }

    public static int getRankFromFame(PlayerClass pc) {
        if (pc.getFame() >= 100000) {
            return 5;
        } else if (pc.getFame() >= 50000) {
            return 4;
        } else if (pc.getFame() >= 15000) {
            return 3;
        } else if (pc.getFame() >= 5000) {
            return 2;
        } else if (pc.getFame() >= 1000) {
            return 1;
        }

        return 0;
    }

    public static ChatColor getStarColor(PlayerData playerData) {
        switch ((int) Math.floor(playerData.getOverallRank() / 5D)) {
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
