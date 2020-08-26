package me.reykench.events;

import me.reykench.RotMC;
import me.reykench.player.PlayerClass;
import me.reykench.gui.CreateClassGUI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinLeaveEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        RotMC.getInstance().getPlayerDataManager().registerPlayerData(e.getPlayer());
        if(RotMC.getPlayerData(e.getPlayer()).getMainClass() != null) {
            PlayerClass pc = RotMC.getPlayerData(e.getPlayer()).getMainClass();
            RotMC.getInstance().getLevelProgression().displayLevelProgression(e.getPlayer());
            e.getPlayer().sendMessage(ChatColor.GREEN + "Your current profile: " + ChatColor.YELLOW + pc.getData().getName() + " " + ChatColor.GOLD + pc.getLevel());
            pc.applyStats();
            RotMC.getInstance().getDatabase().update(e.getPlayer(), null);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(new CreateClassGUI(RotMC.getPlayerData(e.getPlayer())).getInv());
                }
            }.runTaskLater(RotMC.getInstance(), 20L);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        RotMC.getInstance().getPlayerDataManager().unregisterPlayerData(e.getPlayer());
    }

}
