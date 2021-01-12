package me.kench.player.stat;

import com.destroystokyo.paper.util.SneakyThrow;
import me.kench.items.ItemBuilder;
import me.kench.items.stats.GemType;
import me.kench.player.RpgClass;
import me.kench.player.stat.view.*;
import me.kench.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Stats implements Cloneable {
    private float health = 0F;
    private float attack = 0F;
    private float defense = 0F;
    private float speed = 0F;
    private float evasion = 0F;
    private float vitality = 0F;

    /**
     * Gets the current stat points for the given {@link Stat}
     *
     * @param stat the given {@link Stat}
     * @return the current stat points
     */
    public StatView getStat(Stat stat) {
        switch (stat) {
            case HEALTH:
                return new HealthView(health);
            case ATTACK:
                return new AttackView(attack);
            case DEFENSE:
                return new DefenseView(defense);
            case SPEED:
                return new SpeedView(speed);
            case EVASION:
                return new EvasionView(evasion);
            case VITALITY:
                return new VitalityView(vitality);
        }

        return null;
    }

    /**
     * Convenience method to add 0.5 stat points to a stat. See {@link #incrementStat(Stat, float)}.
     * Each potion is worth 0.5 of a stat.
     *
     * @param stat the stat to increment
     */
    public void addPotion(Stat stat) {
        // Each potion is worth 0.5 of a stat
        incrementStat(stat, 0.5F);
    }

    /**
     * Convenience method to add {@code 0.5 * gemLevel} stat points to a stat. See {@link #incrementStat(Stat, float)}.
     * Each gem is worth 0.5 stat points per level that the gem is. A level 5 gem will be worth 0.5 * 5 = 2.5 stat points.
     *
     * @param gemType the type of gem to add
     * @param gemLevel the level of the gem that we are adding
     */
    public void addGem(GemType gemType, int gemLevel) {
        if (gemLevel < 1 || gemLevel > 5) {
            throw new IllegalArgumentException(String.format("gemLevel is clamped between 1 and 5; given %d!", gemLevel));
        }

        // Each gem is worth a scaling amount of a stat
        // 0.5 of a stat per level
        incrementStat(gemType.toStat(), 0.5F * gemLevel);
    }

    /**
     * Adds {@code amount} of stat points to the specified {@link Stat}.
     *
     * @param stat the stat to increment
     * @param amount the amount of stat points to increment by
     */
    public void incrementStat(Stat stat, float amount) {
        setStat(stat, getStat(stat).getStatPoints() + amount);
    }

    /**
     * Adds the given {@link Stats} to this {@link Stats}.
     *
     * @param stats the stats to add to this object
     */
    public void incrementStats(Stats stats) {
        health += stats.health;
        attack += stats.attack;
        defense += stats.defense;
        speed += stats.speed;
        evasion += stats.evasion;
        vitality += stats.vitality;
    }

    /**
     * Adds all of the given {@link Stats} to this {@link Stats}.
     * See {@link #incrementStats(Stats)}.
     *
     * @param stats the stats to add to this object
     */
    public void incrementStats(Stats... stats) {
        Arrays.stream(stats).forEach(this::incrementStats);
    }

    /**
     * Sets a specific stat to the given {@code value}.
     *
     * @param stat the stat to set
     * @param value the value to set the stat to
     */
    public void setStat(Stat stat, float value) {
        switch (stat) {
            case HEALTH:
                health = value;
                break;
            case ATTACK:
                attack = value;
                break;
            case DEFENSE:
                defense = value;
                break;
            case SPEED:
                speed = value;
                break;
            case EVASION:
                evasion = value;
                break;
            case VITALITY:
                vitality = value;
                break;
        }
    }

    /**
     * Sets all stats to 0.
     * Done when the player dies.
     */
    public void zeroStats() {
        health = 0F;
        attack = 0F;
        defense = 0F;
        speed = 0F;
        evasion = 0F;
        vitality = 0F;
    }

    /**
     * Gets the maximum cap (at level 20) for the given {@link Stat} in the given {@link RpgClass}.
     *
     * @param rpgClass the {@link RpgClass}
     * @param stat the {@link Stat}
     * @return the maximum stat points that can be achieved at level 20 for the given stat
     */
    public int getCap(RpgClass rpgClass, Stat stat) {
        return (int) getCapForLevel(rpgClass, stat, 20);
    }

    /**
     * Gets the maximum cap at the given {@code level} for the given {@link Stat} in the given {@link RpgClass}.
     *
     * @param rpgClass the {@link RpgClass}
     * @param stat the {@link Stat}
     * @param level the level, from 1 thru 20, that should be used for cap calculation
     * @return the maximum stat points that can be achieved at {@code level} for the given stat
     */
    public double getCapForLevel(RpgClass rpgClass, Stat stat, int level) {
        if (level < 1 || level > 20) {
            throw new IllegalArgumentException(String.format("level is clamped between 1 and 20; given %d!", level));
        }

        return Stat.getCapForLevel(rpgClass, stat, level);
    }

    /**
     * Gets an {@link ItemStack} with lore that describes this Stats object.
     *
     * @param stat the stat to get an {@link ItemStack} for
     * @return the {@link ItemStack}
     */
    public ItemStack getDescriptiveItemFor(Stat stat) {
        ItemBuilder builder = ItemBuilder.create(Material.CARROT_ON_A_STICK);

        StatView view = getStat(stat);

        String nameFormat = "";
        int modelData = -1;
        switch (stat) {
            case HEALTH:
                nameFormat = "<red>**%s**<gray>. <white>+%s";
                modelData = 205;
                break;
            case ATTACK:
                nameFormat = "<aqua>**%s**<gray>. <white>+%s%%";
                modelData = 201;
                break;
            case DEFENSE:
                nameFormat = "<blue>**%s**<gray>. <white>+%s%%";
                modelData = 203;
                break;
            case SPEED:
                nameFormat = "<green>**%s**<gray>. <white>+%s%%";
                modelData = 202;
                break;
            case EVASION:
                nameFormat = "<yellow>**%s**<gray>. <white>+%s%%";
                modelData = 204;
                break;
            case VITALITY:
                nameFormat = "<yellow>**%s**<gray>. <white>+%s";
                modelData = 206;
                break;
        }

        builder = builder
                .name(TextUtils.parseMini(String.format(nameFormat, stat.getName(), view.getDisplayValue())))
                .modelData(modelData);

    }

    /**
     * Clones the object so when modifying it, it doesn't affect the parent.
     *
     * @return an exact copy with a different memory backing
     */
    @Override
    public Stats clone() {
        try {
            return (Stats) super.clone();
        } catch (CloneNotSupportedException ex) {
            // This should never happen.
            SneakyThrow.sneaky(ex);
            return null;
        }
    }
}
