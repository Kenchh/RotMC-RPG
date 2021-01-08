package me.kench.player;

public enum Stat {
    HEALTH,
    ATTACK,
    DEFENSE,
    SPEED,
    DODGE,
    VITALITY;

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static double getCap(RpgClass rpgClass, Stat stat, double multiplier) {
        RpgClass.StatCaps caps = rpgClass.getStatCaps();

        switch (stat) {
            case HEALTH:
                return caps.getHealth() * multiplier;
            case ATTACK:
                return caps.getAttack() * multiplier;
            case DEFENSE:
                return caps.getDefense() * multiplier;
            case SPEED:
                return caps.getSpeed() * multiplier;
            case DODGE:
                return caps.getDodge() * multiplier;
            case VITALITY:
                return caps.getVitality() * multiplier;
        }

        return -1;
    }
}
