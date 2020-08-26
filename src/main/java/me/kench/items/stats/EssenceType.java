package me.kench.items.stats;

import org.bukkit.ChatColor;

public enum EssenceType {

    VENOM("Essence of Venom", ChatColor.YELLOW.toString(), 405),
    AQUA("Essence of Aqua", ChatColor.DARK_AQUA.toString(), 402),
    FIRE("Essence of Fire", ChatColor.GOLD.toString(), 401),
    BLOOD("Essence of Blood", ChatColor.DARK_RED.toString(), 403),
    WINTER("Essence of Winter", ChatColor.WHITE.toString(), 406),
    LOVE("Essence of Love", ChatColor.RED.toString(), 404);

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

    EssenceType(String name, String prefix, int modeldata) {
        this.name = name;
        this.prefix = prefix;
        this.modeldata = modeldata;
    }

}
