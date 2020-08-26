package me.kench.items.stats;

import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

public enum RuneType {

    WATER_BREATHING("Rune of Water Breathing", ChatColor.AQUA.toString(), PotionEffectType.WATER_BREATHING, 352),
    NIGHT_VISION("Rune of Night Vision", ChatColor.DARK_PURPLE.toString(), PotionEffectType.NIGHT_VISION, 354),
    FIRE_RESISTANCE("Rune of Fire Resistance", ChatColor.RED.toString(), PotionEffectType.FIRE_RESISTANCE, 353),
    JUMP("Rune of Jump", ChatColor.GREEN.toString(), PotionEffectType.JUMP, 355),
    HASTE("Rune of Haste", ChatColor.GOLD.toString(), PotionEffectType.FAST_DIGGING, 351);

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public PotionEffectType getPotionEffectType() {
        return effect;
    }

    public int getModeldata() {
        return modeldata;
    }

    String name;
    String prefix;
    PotionEffectType effect;
    int modeldata;

    RuneType(String name, String prefix, PotionEffectType effect, int modeldata) {
        this.name = name;
        this.prefix = prefix;
        this.effect = effect;
        this.modeldata = modeldata;
    }

}
