package me.kench.rotmc.gui.chooseclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;

public class ClassSlotUnavailableItem extends GuiItem {
    public ClassSlotUnavailableItem() {
        super(
                ItemBuilder.create(Material.IRON_BARS).name(TextUtils.parseMini("<gray>**Locked**")).build(),
                event -> event.setCancelled(true)
        );
    }
}
