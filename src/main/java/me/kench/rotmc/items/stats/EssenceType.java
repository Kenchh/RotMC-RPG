package me.kench.rotmc.items.stats;

import org.bukkit.ChatColor;

public enum EssenceType {
    VENOM("Essence of Venom", ChatColor.YELLOW.toString(), 405),
    AQUA("Essence of Aqua", ChatColor.DARK_AQUA.toString(), 402),
    FIRE("Essence of Fire", ChatColor.GOLD.toString(), 401),
    BLOOD("Essence of Blood", ChatColor.DARK_RED.toString(), 403),
    WINTER("Essence of Winter", ChatColor.WHITE.toString(), 406),
    LOVE("Essence of Love", ChatColor.RED.toString(), 404),
    GANDA(ChatColor.translateAlternateColorCodes('&', "&3&k!&f[&eEssence of &bGanda&f]&3&k!"), ChatColor.BOLD + ChatColor.YELLOW.toString(), 8001),
    HADORI(ChatColor.translateAlternateColorCodes('&', "&1&k!&f[&3Essence of &aHadori&f]&1&k!"), ChatColor.BOLD + ChatColor.GREEN.toString(), 8002),
    ILLURI(ChatColor.translateAlternateColorCodes('&', "&f&k!&f[&1Essence of &9Illuri&f]&f&k!"), ChatColor.BOLD + ChatColor.AQUA.toString(), 8003),
    KABIRI(ChatColor.translateAlternateColorCodes('&', "&e&k!&f[&cEssence of &4Kabiri&f]&e&k!"), ChatColor.BOLD + ChatColor.RED.toString(), 8004);

    private final String name;
    private final String prefix;
    private final int modelData;

    EssenceType(String name, String prefix, int modelData) {
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
        return modelData;
    }
}
