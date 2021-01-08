package me.kench.player;

public class Stats {
    private float health = 0F;
    private float attack = 0F;
    private float defense = 0F;
    private float speed = 0F;
    private float dodge = 0F;
    private float vitality = 0F;

    public float getStat(Stat stat) {
        switch (stat) {
            case HEALTH:
                return health;
            case ATTACK:
                return attack;
            case DEFENSE:
                return defense;
            case SPEED:
                return speed;
            case DODGE:
                return dodge;
            case VITALITY:
                return vitality;
        }

        return 0;
    }

    public void incrementStat(Stat stat) {
        setStat(stat, getStat(stat) + 1F);
    }

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
            case DODGE:
                dodge = value;
                break;
            case VITALITY:
                vitality = value;
                break;
        }
    }

    public void zeroStats() {
        health = 0F;
        attack = 0F;
        defense = 0F;
        speed = 0F;
        dodge = 0F;
        vitality = 0F;
    }

    public int getCap(RpgClass rpgClass, Stat stat) {
        return (int) getCapForLevel(rpgClass, stat, 20);
    }

    public double getCapForLevel(RpgClass rpgClass, Stat stat, int level) {
        return Stat.getCap(rpgClass, stat, (double) level / 20);
    }
}
