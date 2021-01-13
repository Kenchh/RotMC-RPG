package me.kench.items.stats;

import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public enum RuneType {
    WATER_BREATHING("Rune of Water Breathing", ChatColor.AQUA.toString(), PotionEffectType.WATER_BREATHING, 352),
    NIGHT_VISION("Rune of Night Vision", ChatColor.DARK_PURPLE.toString(), PotionEffectType.NIGHT_VISION, 354),
    FIRE_RESISTANCE("Rune of Fire Resistance", ChatColor.RED.toString(), PotionEffectType.FIRE_RESISTANCE, 353),
    JUMP("Rune of Jump", ChatColor.GREEN.toString(), PotionEffectType.JUMP, 355),
    HASTE("Rune of Haste", ChatColor.GOLD.toString(), PotionEffectType.FAST_DIGGING, 351);

    private final String name;
    private final String prefix;
    private final PotionEffectType effect;
    private final int modelData;

    RuneType(String name, String prefix, PotionEffectType effect, int modelData) {
        this.name = name;
        this.prefix = prefix;
        this.effect = effect;
        this.modelData = modelData;
    }

    public String getName() {
        return name;
    }

    public String getNiceConstant() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public String getPrefix() {
        return prefix;
    }

    public PotionEffectType getPotionEffectType() {
        return effect;
    }

    public int getModelData() {
        return modelData;
    }

    public static RuneType getByPotionEffectType(PotionEffectType potionEffectType) {
        return Arrays.stream(RuneType.values()).filter(type -> type.effect == potionEffectType).findFirst().orElse(null);
    }
}
