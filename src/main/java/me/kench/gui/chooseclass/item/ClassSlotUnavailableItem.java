package me.kench.gui.chooseclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;

public class ClassSlotUnavailableItem extends GuiItem {
    public ClassSlotUnavailableItem() {
        super(
                ItemBuilder.create(Material.IRON_BARS).name(TextUtils.parseMini("<gray>**Locked**")).build(),
                event -> event.setCancelled(true)
        );
    }
}
