package me.kench.events;

import me.kench.RotMC;
import me.kench.gui.CreateClassGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiEvents implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getView() != null && e.getView().getTitle() != "Choose your class") return;

        if(RotMC.getPlayerData(p).getMainClass() != null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                p.openInventory(new CreateClassGUI(RotMC.getPlayerData(p)).getInv());
            }
        }.runTaskLater(RotMC.getInstance(), 1L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(RotMC.getPlayerData(e.getPlayer()) == null || RotMC.getPlayerData(e.getPlayer()).getMainClass() == null) e.setCancelled(true);
    }

}
