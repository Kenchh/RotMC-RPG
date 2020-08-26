package me.kench.items.stats;

import org.bukkit.ChatColor;

public enum GemType {

    HEALTH("Ruby of Health", ChatColor.RED.toString(), 340),
    ATTACK("Diamond of Attack", ChatColor.AQUA.toString(), 300),
    DEFENSE("Sapphire of Defense", ChatColor.BLUE.toString(), 320),
    SPEED("Emerald of Speed", ChatColor.GREEN.toString(), 310),
    DODGE("Crystal of Evasion", ChatColor.YELLOW.toString(), 330);

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getModeldata() {
        return modeldata;
    }

    String name;
    String prefix;
    int modeldata;

    GemType(String name, String prefix, int modeldata) {
        this.name = name;
        this.prefix = prefix;
        this.modeldata = modeldata;
    }

}
