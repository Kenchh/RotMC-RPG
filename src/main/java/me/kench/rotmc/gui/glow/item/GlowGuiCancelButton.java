package me.kench.rotmc.gui.glow.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;

public class GlowGuiCancelButton extends GuiItem {
    public GlowGuiCancelButton() {
        super(
                ItemBuilder.create(Material.BARRIER).name(TextUtils.parseMini("<red>Cancel")).build(),
                event -> {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }
        );
    }
}
