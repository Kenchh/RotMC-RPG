package me.kench.gui.items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;

public class LockedItem extends GuiItem {
    public LockedItem() {
        super(ItemBuilder.create(Material.IRON_BARS).name(TextUtils.parseMini("<gray>**Locked**")).build());
    }
}
