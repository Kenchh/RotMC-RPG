package me.kench.events;

import me.kench.RotMC;
import me.kench.player.Stats;
import me.kench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class DamageEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {

        if(e.isCancelled()) return;

        if(e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();

            if(RotMC.getPlayerData(p) == null || RotMC.getPlayerData(p).getMainClass() == null) {
                return;
            }

            Stats stats = RotMC.getPlayerData(p).getMainClass().getStats();

            e.setDamage(e.getDamage() + e.getDamage()*(stats.getAttack(stats.attack + ItemUtils.getOverallGemStatsFromEquipment(p).attack + ItemUtils.getOverallItemStatsFromEquipment(p).attack, false))+(((float) ItemUtils.getOverallItemStatsFromEquipment(p).attack)/100F));

        }

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            double damage = e.getDamage();

            Stats stats = RotMC.getPlayerData(p).getMainClass().getStats();

            if(RotMC.getPlayerData(p) == null || RotMC.getPlayerData(p).getMainClass() == null) {
                return;
            }

            Random r = new Random();
            int random = r.nextInt(100);

            if(random <= stats.getDodge(stats.dodge + ItemUtils.getOverallGemStatsFromEquipment(p).dodge, true)+(((float) ItemUtils.getOverallItemStatsFromEquipment(p).dodge)/100F)) {
                e.setCancelled(true);
                return;
            }

            double dmg = damage - damage*(stats.getDefense(stats.defense + ItemUtils.getOverallGemStatsFromEquipment(p).defense, false)+(((float) ItemUtils.getOverallItemStatsFromEquipment(p).defense)/200F));

            e.setDamage(dmg);

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setVelocity(new Vector(0, 0, 0));
                }
            }.runTaskLater(RotMC.getInstance(), 1L);

        }
    }

}
