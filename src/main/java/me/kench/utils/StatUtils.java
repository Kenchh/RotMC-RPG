package me.kench.utils;

public class StatUtils {
    public static float getHealth(float level, boolean visual, boolean gem) {
        if (!visual) {
            return level / 2F;
        } else {
            return level / 2F;
        }
    }

    public static float getAttack(float level, boolean visual, boolean gem) {
        if (!visual) {
            return level / 100F / 2F;
        } else {
            return level / 2F;
        }
    }

    public static float getDefense(float level, boolean visual, boolean gem) {
        if (!visual) {
            return (level / 100F) / 2F / 2F;
        } else {
            return level / 2F;
        }
    }

    public static float getSpeed(float level, boolean visual, boolean gem) {
        if (!visual) {
            return level / 100F / 2F;
        } else {
            return level / 2F;
        }
    }

    public static float getDodge(float level, boolean visual, boolean gem) {
        if (!visual) {
            return level / 100F / 2F;
        } else {
            return level / 2F;
        }
    }

    public static float getVitality(float level, boolean visual, boolean gem) {
        if (!visual) {
            return level / 100F / 2F;
        } else {
            return level / 2F;
        }
    }
}
