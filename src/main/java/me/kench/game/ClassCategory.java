package me.kench.game;

import org.bukkit.ChatColor;

public enum ClassCategory {
    DAGGER,BOW,STAFF,SWORD;

    public static String getName(ClassCategory category) {
        switch (category) {
            case DAGGER:
                return ChatColor.DARK_GRAY + "↓ [" + ChatColor.WHITE + "Dagger Classes" + ChatColor.DARK_GRAY + "] ↓";
            case BOW:
                return ChatColor.DARK_GRAY + "↓ [" + ChatColor.WHITE + "Bow Classes" + ChatColor.DARK_GRAY + "] ↓";
            case STAFF:
                return ChatColor.DARK_GRAY + "↓ [" + ChatColor.WHITE + "Staff Classes" + ChatColor.DARK_GRAY + "] ↓";
            case SWORD:
                return ChatColor.DARK_GRAY + "↓ [" + ChatColor.WHITE + "Sword Classes" + ChatColor.DARK_GRAY + "] ↓";
        }
        return "NULL";
    }

    public static int getCustomModelData(ClassCategory category) {
        switch (category) {
            case DAGGER:
                return 41;
            case BOW:
                return 21;
            case STAFF:
                return 1;
            case SWORD:
                return 61;
        }
        return 0;
    }

}
