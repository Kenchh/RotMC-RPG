package me.kench.gui.deleteclass;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.RotMC;
import me.kench.gui.deleteclass.item.DeleteClassConfirmGuiCancelButton;
import me.kench.gui.deleteclass.item.DeleteClassConfirmGuiConfirmButton;
import me.kench.player.PlayerClass;
import org.bukkit.entity.Player;

public class DeleteClassConfirmGui {
    public void display(Player player, PlayerClass toDelete) {
        if (!player.getUniqueId().equals(toDelete.getPlayerUniqueId())) {
            RotMC.getInstance().getLogger().warning("Attempted to display class deletion confirmation for class not owned by given player.");
            return;
        }

        RotMC.newChain()
                .asyncFirst(() -> {
                    ChestGui gui = new ChestGui(3, "Delete this class?");

                    StaticPane buttons = new StaticPane(2, 1, 5, 1);
                    buttons.addItem(new DeleteClassConfirmGuiCancelButton(), 0, 0);
                    buttons.addItem(new DeleteClassConfirmGuiConfirmButton(toDelete), 4, 0);

                    gui.addPane(buttons);

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
