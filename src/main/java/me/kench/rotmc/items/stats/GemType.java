package me.kench.rotmc.items.stats;

import me.kench.rotmc.player.stat.Stat;
import org.bukkit.ChatColor;

public enum GemType {
    HEALTH("Ruby of Health", ChatColor.RED.toString(), 340),
    ATTACK("Diamond of Attack", ChatColor.AQUA.toString(), 300),
    DEFENSE("Sapphire of Defense", ChatColor.BLUE.toString(), 320),
    SPEED("Emerald of Speed", ChatColor.GREEN.toString(), 310),
    EVASION("Crystal of Evasion", ChatColor.YELLOW.toString(), 330),
    VITALITY("Topaz of Vitality", ChatColor.LIGHT_PURPLE.toString(), 350);

    private final String name;
    private final String prefix;
    private final int modelData;

    GemType(String name, String prefix, int modelData) {
        this.name = name;
        this.prefix = prefix;
        this.modelData = modelData;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getModelData() {
        return getModelData(0);
    }

    public int getModelData(int level) {
        return modelData + level;
    }

    public Stat toStat() {
        return Stat.valueOf(name());
    }

    public static GemType fromStat(Stat stat) {
        return valueOf(stat.name());
    }
}
