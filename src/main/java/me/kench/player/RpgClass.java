package me.kench.player;

import org.bukkit.ChatColor;

import java.util.*;

public enum RpgClass {
    KNIGHT(RpgWeapon.SWORD, new StatCaps(35, 25, 30, 15, 20, 35), 151),
    WARRIOR(RpgWeapon.SWORD, new StatCaps(30, 35, 25, 20, 15, 30), 141),
    HUNTRESS(RpgWeapon.BOW, new StatCaps(20, 30, 25, 35, 15, 25), 111),
    NECROMANCER(RpgWeapon.STAFF, new StatCaps(30, 35, 25, 15, 20, 20), 101),
    ASSASSIN(RpgWeapon.DAGGER, new StatCaps(15, 25, 20, 30 ,35, 25), 121),
    ROGUE(RpgWeapon.DAGGER, new StatCaps(20, 25, 15, 35, 30, 30), 131);

    private final RpgWeapon weapon;
    private final StatCaps statCaps;
    private final int customModelData;

    RpgClass(RpgWeapon weapon, StatCaps statCaps, int customModelData) {
        this.weapon = weapon;
        this.statCaps = statCaps;
        this.customModelData = customModelData;
    }

    public RpgWeapon getWeapon() {
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

    public List<String> getLore() {
        switch (this) {
            case KNIGHT:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "A slow hitting sword",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and shield user who",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "relies on their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "physical strength to",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "hinder opponents."
                );
            case WARRIOR:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "A swift swordsman in",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "which their speed",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and hasteful hands",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "allow them to strike",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "unscathed in any situation."
                );
            case HUNTRESS:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "These are precisive",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "archers who lay traps",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "to catch foes off",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "guard with large",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "amounts of instant damage."
                );
            case NECROMANCER:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Said to manipulate",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "the souls life force,",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "necromancer's use",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "magic staffs and",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "their enchanted skull",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "to drain their enemies",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "health."
                );
            case ASSASSIN:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Lethal dagger users",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "who induce their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "opponents with poison",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and finish them off with",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "their nimble attack speed."
                );
            case ROGUE:
                return Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Dagger wielding",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "mercenaries, they gain",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "the advantage in combat",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "with their specialised",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "cloaks which grant",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "invisibility and their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "daggers which",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "grant fast attack speed."
                );
        }

        return Collections.emptyList();
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
