package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.createclass.CreateClassGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiEvents implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getView().getTitle().equals("Choose your class")) {
            return;
        }

        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .delay(1)
                .syncLast(data -> {
                    if (data.getSelectedClass() == null) {
                        new CreateClassGui().display(player);
                    }
                })
                .execute();
    }
}
