package me.kench.rotmc.gui.createclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;

public class CreateClassGuiCancelButton extends GuiItem {
    public CreateClassGuiCancelButton() {
        super(
                ItemBuilder.create(Material.BARRIER).name(TextUtils.parseMini("<red>Cancel")).build(),
                event -> {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }
        );
    }
}
