package me.kench.events;

import me.kench.RotMC;
import me.kench.items.stats.EssenceType;
import me.kench.player.PlayerClass;
import me.kench.gui.CreateClassGUI;
import me.kench.player.PlayerData;
import me.kench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class JoinLeaveEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

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

        PlayerData pd = RotMC.getPlayerData(e.getPlayer());


        /* Removing old essences */
        ArrayList<EssenceType> etToRemove = new ArrayList<>();
        for(EssenceType et : pd.activeEssences.keySet()) {
            etToRemove.add(et);
            pd.activeEssences.get(et).cancel();
        }

        for(EssenceType et : etToRemove) {
            pd.activeEssences.remove(et);
        }
        /*  */

        RotMC.getInstance().getPlayerDataManager().unregisterPlayerData(e.getPlayer());
    }

}
