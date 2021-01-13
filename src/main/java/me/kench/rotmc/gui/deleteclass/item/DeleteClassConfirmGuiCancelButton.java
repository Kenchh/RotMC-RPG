package me.kench.rotmc.gui.deleteclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.chooseclass.ChooseClassGui;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DeleteClassConfirmGuiCancelButton extends GuiItem {
    public DeleteClassConfirmGuiCancelButton() {
        super(
                ItemBuilder.create(Material.REDSTONE_BLOCK).name(TextUtils.parseMini("<red>**CANCEL**")).build(),
                event -> {
                    event.setCancelled(true);
                    RotMcPlugin.newChain().delay(1).sync(() -> new ChooseClassGui().display((Player) event.getWhoClicked())).execute();
                }
        );
    }
}
