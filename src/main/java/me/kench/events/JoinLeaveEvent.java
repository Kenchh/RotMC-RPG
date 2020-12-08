package me.kench.events;

import me.kench.RotMC;
import me.kench.items.stats.EssenceType;
import me.kench.player.PlayerClass;
import me.kench.gui.CreateClassGUI;
import me.kench.player.PlayerData;
import me.kench.utils.GlowUtils;
import me.kench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class JoinLeaveEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        
        RotMC.getInstance().getPlayerDataManager().registerPlayerData(p);

        if(RotMC.getPlayerData(p).getMainClass() != null) {
            PlayerClass pc = RotMC.getPlayerData(p).getMainClass();

            RotMC.getInstance().getLevelProgression().displayLevelProgression(p);

            p.sendMessage(ChatColor.GREEN + "Your current profile: " + ChatColor.YELLOW + pc.getData().getName() + " " + ChatColor.GOLD + pc.getLevel());

            pc.applyStats();

            RotMC.getInstance().getSqlManager().update(p, null);

        } else {

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.openInventory(new CreateClassGUI(RotMC.getPlayerData(p)).getInv());
                }
            }.runTaskLater(RotMC.getInstance(), 20L);

        }

        PlayerData pd = RotMC.getPlayerData(p);

        pd.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (pd != null) {
                    if (pd.getMainClass() != null) {
                        new BukkitRunnable() {
                            final Player pp = p;
                            @Override
                            public void run() {
                                if(pp != null && pd.getMainClass() != null) {
                                    pd.getMainClass().tickEssences();
                                    pd.getMainClass().applyStats();
                                }
                            }
                        }.runTaskLater(RotMC.getInstance(), 1L);
                    }
                }

                GlowUtils.clearWhenForbidden(p);
            }
        }.runTaskTimer(RotMC.getInstance(), 1L, 60L);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        PlayerData pd = RotMC.getPlayerData(e.getPlayer());

        if(pd.task != null)
            pd.task.cancel();

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
