package me.kench.player;

public class Stats {

    /*
    public static float healthMaxCap = 30F * 2F;
    public static float attackMaxCap = 1.5F;
    public static float defenseMaxCap = 1.70F / 2F;
    public static float speedMaxCap = 1.50F;
    public static float dodgeMaxCap = 60F;
     */

    public float health = 0;
    public float attack = 0;
    public float defense = 0;
    public float speed = 0;
    public float dodge = 0;
    public float vitality = 0;

    public Stats() {
    }

    public float getHealth(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return level / 2F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return level / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public float getAttack(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return (level / 100F) / 2F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return level / 100F / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public float getDefense(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return (level / 100F) / 4F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return (level / 100F) / 2F / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public float getSpeed(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return (level / 100F) / 2F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return level / 100F / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public float getDodge(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return (level / 100F) / 2F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return level / 100F / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public float getVitality(float level, boolean visual, boolean gem) {
        if(gem) {
            if (!visual) {
                return (level / 100F) / 2F;
            } else {
                return level / 2F;
            }
        } else {
            if (!visual) {
                return level / 100F / 2F;
            } else {
                return level / 2F;
            }
        }
    }

    public int getCap(String clazz, String stat) {

        //double multiplier = (double) level/20;

        double multiplier = 1;

        switch (clazz) {
            case "Knight":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 35*multiplier);
                    case "Attack":
                        return (int) ((double) 25*multiplier);
                    case "Defense":
                        return (int) ((double) 30*multiplier);
                    case "Speed":
                        return (int) ((double) 15*multiplier);
                    case "Dodge":
                        return (int) ((double) 20*multiplier);
                }
            case "Necromancer":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 30*multiplier);
                    case "Attack":
                        return (int) ((double) 35*multiplier);
                    case "Defense":
                        return (int) ((double) 25*multiplier);
                    case "Speed":
                        return (int) ((double) 15*multiplier);
                    case "Dodge":
                        return (int) ((double) 20*multiplier);
                }
            case "Warrior":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 30*multiplier);
                    case "Attack":
                        return (int) ((double) 35*multiplier);
                    case "Defense":
                        return (int) ((double) 25*multiplier);
                    case "Speed":
                        return (int) ((double) 20*multiplier);
                    case "Dodge":
                        return (int) ((double) 15*multiplier);
                }
            case "Huntress":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 20*multiplier);
                    case "Attack":
                        return (int) ((double) 30*multiplier);
                    case "Defense":
                        return (int) ((double) 25*multiplier);
                    case "Speed":
                        return (int) ((double) 35*multiplier);
                    case "Dodge":
                        return (int) ((double) 15*multiplier);
                }
            case "Assassin":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 15*multiplier);
                    case "Attack":
                        return (int) ((double) 25*multiplier);
                    case "Defense":
                        return (int) ((double) 20*multiplier);
                    case "Speed":
                        return (int) ((double) 30*multiplier);
                    case "Dodge":
                        return (int) ((double) 35*multiplier);
                }
            case "Rogue":
                switch (stat) {
                    case "Health":
                        return (int) ((double) 20*multiplier);
                    case "Attack":
                        return (int) ((double) 25*multiplier);
                    case "Defense":
                        return (int) ((double) 15*multiplier);
                    case "Speed":
                        return (int) ((double) 35*multiplier);
                    case "Dodge":
                        return (int) ((double) 30*multiplier);
                }
        }
        return 1;
    }

    public double getCap(String clazz, String stat, int level) {

        double multiplier = (double) level/20;

        switch (clazz) {
            case "Knight":
                switch (stat) {
                    case "Health":
                        return ((double) 35*multiplier);
                    case "Attack":
                        return ((double) 25*multiplier);
                    case "Defense":
                        return ((double) 30*multiplier);
                    case "Speed":
                        return ((double) 15*multiplier);
                    case "Dodge":
                        return ((double) 20*multiplier);
                    case "Vitality":
                        return ((double) 35*multiplier);
                }
            case "Necromancer":
                switch (stat) {
                    case "Health":
                        return ((double) 30*multiplier);
                    case "Attack":
                        return ((double) 35*multiplier);
                    case "Defense":
                        return ((double) 25*multiplier);
                    case "Speed":
                        return ((double) 15*multiplier);
                    case "Dodge":
                        return ((double) 20*multiplier);
                    case "Vitality":
                        return ((double) 20*multiplier);
                }
            case "Warrior":
                switch (stat) {
                    case "Health":
                        return ((double) 30*multiplier);
                    case "Attack":
                        return ((double) 35*multiplier);
                    case "Defense":
                        return ((double) 25*multiplier);
                    case "Speed":
                        return ((double) 20*multiplier);
                    case "Dodge":
                        return ((double) 15*multiplier);
                    case "Vitality":
                        return ((double) 30*multiplier);
                }
            case "Huntress":
                switch (stat) {
                    case "Health":
                        return ((double) 20*multiplier);
                    case "Attack":
                        return ((double) 30*multiplier);
                    case "Defense":
                        return ((double) 25*multiplier);
                    case "Speed":
                        return ((double) 35*multiplier);
                    case "Dodge":
                        return ((double) 15*multiplier);
                    case "Vitality":
                        return ((double) 25*multiplier);
                }
            case "Assassin":
                switch (stat) {
                    case "Health":
                        return ((double) 15*multiplier);
                    case "Attack":
                        return ((double) 25*multiplier);
                    case "Defense":
                        return ((double) 20*multiplier);
                    case "Speed":
                        return ((double) 30*multiplier);
                    case "Dodge":
                        return ((double) 35*multiplier);
                    case "Vitality":
                        return ((double) 25*multiplier);
                }
            case "Rogue":
                switch (stat) {
                    case "Health":
                        return ((double) 20*multiplier);
                    case "Attack":
                        return ((double) 25*multiplier);
                    case "Defense":
                        return ((double) 15*multiplier);
                    case "Speed":
                        return ((double) 35*multiplier);
                    case "Dodge":
                        return ((double) 30*multiplier);
                    case "Vitality":
                        return ((double) 30*multiplier);
                }
        }
        return 1;
    }

}
