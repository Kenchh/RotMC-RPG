package me.kench.player.stat;

import com.destroystokyo.paper.util.SneakyThrow;
import me.kench.items.stats.GemType;
import me.kench.player.stat.view.*;

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
     * @param gemType  the type of gem to add
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
     * @param stat   the stat to increment
     * @param amount the amount of stat points to increment by
     */
    public void incrementStat(Stat stat, float amount) {
        setStat(stat, getStat(stat).getStatPoints() + amount);
    }

    /**
     * Merges the given {@link Stats} objects into this object. Operates on a clone.
     *
     * @param other the other {@link Stats} objects to merge into this one
     * @return the clone with merges applied
     */
    public Stats merge(Stats... other) {
        Stats merged = clone();

        Arrays.stream(other).forEach(obj -> {
            merged.health += obj.health;
            merged.attack += obj.attack;
            merged.defense += obj.defense;
            merged.speed += obj.speed;
            merged.evasion += obj.evasion;
            merged.vitality += obj.vitality;
        });

        return merged;
    }

    /**
     * Sets a specific stat to the given {@code value}.
     *
     * @param stat  the stat to set
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
