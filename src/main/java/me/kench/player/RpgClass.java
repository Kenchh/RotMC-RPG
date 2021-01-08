package me.kench.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RpgClass {
    KNIGHT(RPGWeapon.SWORD, new StatCaps(35, 25, 30, 15, 20, 35), 151),
    WARRIOR(RPGWeapon.SWORD, new StatCaps(30, 35, 25, 20, 15, 30), 141),
    HUNTRESS(RPGWeapon.BOW, new StatCaps(20, 30, 25, 35, 15, 25), 111),
    NECROMANCER(RPGWeapon.STAFF, new StatCaps(30, 35, 25, 15, 20, 20), 101),
    ASSASSIN(RPGWeapon.DAGGER, new StatCaps(15, 25, 20, 30 ,35, 25), 121),
    ROGUE(RPGWeapon.DAGGER, new StatCaps(20, 25, 15, 35, 30, 30), 131);

    private final RPGWeapon weapon;
    private final StatCaps statCaps;
    private final int customModelData;

    RpgClass(RPGWeapon weapon, StatCaps statCaps, int customModelData) {
        this.weapon = weapon;
        this.statCaps = statCaps;
        this.customModelData = customModelData;
    }

    public RPGWeapon getWeapon() {
        return weapon;
    }

    public StatCaps getStatCaps() {
        return statCaps;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<String> getPermissions() {
        return Arrays.asList(
                String.format("rotmc.%s", name().toLowerCase()),
                getWeapon().getPermission()
        );
    }

    public String getName() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    public static List<String> getAllPermissions() {
        List<String> out = new ArrayList<>();
        Arrays.stream(values()).forEach(entry -> out.addAll(entry.getPermissions()));
        return out;
    }

    public static RpgClass getByName(String name) {
        return RpgClass.valueOf(name.toUpperCase());
    }

    public static class StatCaps {
        private final double health;
        private final double attack;
        private final double defense;
        private final double speed;
        private final double dodge;
        private final double vitality;

        public StatCaps(double health, double attack, double defense, double speed, double dodge, double vitality) {
            this.health = health;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
            this.dodge = dodge;
            this.vitality = vitality;
        }

        public double getHealth() {
            return health;
        }

        public double getAttack() {
            return attack;
        }

        public double getDefense() {
            return defense;
        }

        public double getSpeed() {
            return speed;
        }

        public double getDodge() {
            return dodge;
        }

        public double getVitality() {
            return vitality;
        }
    }
}
