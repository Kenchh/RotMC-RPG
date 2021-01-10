package me.kench.gui.extractor.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.RotMC;
import me.kench.gui.chooseclass.ChooseClassGui;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ExtractorGuiCancelButton extends GuiItem {
    public ExtractorGuiCancelButton() {
        super(
                ItemBuilder.create(Material.BARRIER).name(TextUtils.parseMini("<red>Cancel")).build(),
                event -> {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }
        );
    }
}
