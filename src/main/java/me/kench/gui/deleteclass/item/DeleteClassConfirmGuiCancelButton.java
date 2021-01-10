package me.kench.gui.deleteclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.RotMC;
import me.kench.gui.chooseclass.ChooseClassGui;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DeleteClassConfirmGuiCancelButton extends GuiItem {
    public DeleteClassConfirmGuiCancelButton() {
        super(
                ItemBuilder.create(Material.REDSTONE_BLOCK).name(TextUtils.parseMini("<red>**CANCEL**")).build(),
                event -> {
                    event.setCancelled(true);
                    RotMC.newChain().delay(1).sync(() -> new ChooseClassGui().display((Player) event.getWhoClicked())).execute();
                }
        );
    }
}
