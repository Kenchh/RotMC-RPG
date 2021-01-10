package me.kench.listener;

import me.kench.RotMC;
import me.kench.gui.createclass.CreateClassGui;
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

        RotMC.getInstance().getDataManager().getPlayerData()
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
