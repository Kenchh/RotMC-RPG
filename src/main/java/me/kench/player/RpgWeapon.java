package me.kench.player;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;

public enum RpgWeapon {
    SWORD(61),
    DAGGER(41),
    STAFF(1),
    BOW(21);

    private final int customModelData;

    RpgWeapon(int customModelData) {
        this.customModelData = customModelData;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public GuiItem getCreateClassGuiItem() {
        return new GuiItem(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(TextUtils.parseMini(String.format("<dark_gray>↓ [<white>%s Classes<dark_gray>] ↓", getName())))
                        .modelData(getCustomModelData())
                        .build()
        );
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getPermission() {
        return String.format("rotmc.weapon.%s", name().toLowerCase());
    }
}
