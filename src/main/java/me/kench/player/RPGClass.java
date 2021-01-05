package me.kench.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RPGClass {
    KNIGHT(RPGWeapon.SWORD),
    WARRIOR(RPGWeapon.SWORD),
    HUNTRESS(RPGWeapon.BOW),
    NECROMANCER(RPGWeapon.STAFF),
    ASSASSIN(RPGWeapon.DAGGER),
    ROGUE(RPGWeapon.DAGGER);

    private final RPGWeapon weapon;

    RPGClass(RPGWeapon weapon) {
        this.weapon = weapon;
    }

    public RPGWeapon getWeapon() {
        return weapon;
    }

    public List<String> getPermissions() {
        return Arrays.asList(
                String.format("rotmc.%s", name().toLowerCase()),
                getWeapon().getPermission()
        );
    }

    public static List<String> getAllPermissions() {
        List<String> out = new ArrayList<>();
        Arrays.stream(values()).forEach(entry -> out.addAll(entry.getPermissions()));
        return out;
    }

    public static RPGClass getByName(String name) {
        return RPGClass.valueOf(name.toUpperCase());
    }
}
