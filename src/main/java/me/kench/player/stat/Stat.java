package me.kench.player.stat;

import me.kench.player.RpgClass;

public enum Stat {
    HEALTH,
    ATTACK,
    DEFENSE,
    SPEED,
    EVASION,
    VITALITY;

    /**
     * Gets the "nice" name for the enum constant. Capitalizes first letter and lowercases
     * the rest.
     *
     * @return the "nice" name
     */
    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    /**
     * Needed because sometimes {@link #EVASION} is referred to as Dodge.
     *
     * @return the new name in {@link #EVASION}'s case, otherwise see {@link #getName()}.
     */
    public String getItemStatsOrGemLoreName() {
        if (this == EVASION) {
            return "Dodge";
        }

        return getName();
    }

    /**
     * Gets the maximum stat points for a given {@link Stat} in a given {@link RpgClass} for a given {@code level}
     * which is used to calculate a multiplier via {@code level / 20}.
     *
     * @param rpgClass the {@link RpgClass}
     * @param stat     the {@link Stat}
     * @param level    the {@code level} to calculate the cap with
     * @return the stat points cap for the given {@code level}
     */
    public static double getCapForLevel(RpgClass rpgClass, Stat stat, int level) {
        RpgClass.StatCaps caps = rpgClass.getStatCaps();

        double multiplier = level / 20D;

        switch (stat) {
            case HEALTH:
                return caps.getHealth() * multiplier;
            case ATTACK:
                return caps.getAttack() * multiplier;
            case DEFENSE:
                return caps.getDefense() * multiplier;
            case SPEED:
                return caps.getSpeed() * multiplier;
            case EVASION:
                return caps.getEvasion() * multiplier;
            case VITALITY:
                return caps.getVitality() * multiplier;
        }

        return -1;
    }

    /**
     * Finds a {@link Stat} by name, or returns null if one can't find found.
     * Does a simple conversion by capitalizing the input. Should be sufficient.
     *
     * @param name the name of the stat to search for
     * @return the {@link Stat} or null
     */
    public static Stat getByName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
