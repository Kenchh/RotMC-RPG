package me.reykench.player;

public class Stats {

    public int health = 0;
    public int attack = 0;
    public int defense = 0;
    public int speed = 0;
    public int dodge = 0;

    public Stats() {
    }

    public float getHealth(int level) {
        return (float) level;
    }

    public float getAttack(int level, boolean inPercentage) {

        if(!inPercentage) {
            return (((float) level) / 100F) / 2F;
        } else {
            return ((float) level) / 2F;
        }
    }

    public float getDefense(int level, boolean inPercentage) {

        if(!inPercentage) {
            return (((float) level) / 100F) / 2F;
        } else {
            return (float) level;
        }
    }

    public float getSpeed(int level, boolean inPercentage) {

        if(!inPercentage) {
            return ((float) level) / 100F;
        } else {
            return (float) level;
        }
    }

    public float getDodge(int level, boolean inPercentage) {

        if(!inPercentage) {
            return (((float) level) / 100F) / 2F;
        } else {
            return ((float) level) / 2F;
        }
    }

    public int getCap(String clazz, String stat, int level) {

        double multiplier = (double) level/20D;

        switch (clazz) {
            case "Knight":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 20*multiplier);
                    case "Attack":
                        return (int) ((double) 15*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 5*multiplier);
                    case "Dodge":
                        return (int) ((double) 10*multiplier);
                }
            case "Necromancer":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 5*multiplier);
                    case "Attack":
                        return (int) ((double) 20*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 15*multiplier);
                    case "Dodge":
                        return (int) ((double) 10*multiplier);
                }
            case "Warrior":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 15*multiplier);
                    case "Attack":
                        return (int) ((double) 20*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 10*multiplier);
                    case "Dodge":
                        return (int) ((double) 5*multiplier);
                }
            case "Huntress":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 15*multiplier);
                    case "Attack":
                        return (int) ((double) 20*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 10*multiplier);
                    case "Dodge":
                        return (int) ((double) 5*multiplier);
                }
            case "Assassin":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 5*multiplier);
                    case "Attack":
                        return (int) ((double) 10*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 20*multiplier);
                    case "Dodge":
                        return (int) ((double) 15*multiplier);
                }
            case "Rogue":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 5*multiplier);
                    case "Attack":
                        return (int) ((double) 10*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 15*multiplier);
                    case "Dodge":
                        return (int) ((double) 20*multiplier);
                }
        }
        return 1;
    }

}
