package me.kench.player;

public enum RPGWeapon {
    SWORD(61),
    DAGGER(41),
    STAFF(1),
    BOW(21);

    private final int customModelData;

    RPGWeapon(int customModelData) {
        this.customModelData = customModelData;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getPermission() {
        return String.format("rotmc.weapon.%s", name().toLowerCase());
    }
}
