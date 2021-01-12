package me.kench.utils;

import me.kench.player.RpgClass;
import me.kench.player.stat.Stat;

public class StatUtils {

    /**
     * Gets the maximum cap (at level 20) for the given {@link Stat} in the given {@link RpgClass}.
     *
     * @param rpgClass the {@link RpgClass}
     * @param stat the {@link Stat}
     * @return the maximum stat points that can be achieved at level 20 for the given stat
     */
    public static int getCap(RpgClass rpgClass, Stat stat) {
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
    public static double getCapForLevel(RpgClass rpgClass, Stat stat, int level) {
        if (level < 1 || level > 20) {
            throw new IllegalArgumentException(String.format("level is clamped between 1 and 20; given %d!", level));
        }

        return Stat.getCapForLevel(rpgClass, stat, level);
    }
}
