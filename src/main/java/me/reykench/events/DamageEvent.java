package me.reykench.events;

import me.reykench.RotMC;
import me.reykench.player.Stats;
import me.reykench.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class DamageEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {

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

            if(RotMC.getPlayerData(p) == null || RotMC.getPlayerData(p).getMainClass() == null) {
                return;
            }

            Stats stats = RotMC.getPlayerData(p).getMainClass().getStats();

            Random r = new Random();
            int random = r.nextInt(100);

            if(random <= stats.getDodge(stats.dodge + ItemUtils.getOverallGemStatsFromEquipment(p).dodge, true)+(((float) ItemUtils.getOverallItemStatsFromEquipment(p).dodge)/100F)) {
                e.setCancelled(true);
                return;
            }

            e.setDamage(e.getDamage() - e.getDamage()*(stats.getDefense(stats.defense + ItemUtils.getOverallGemStatsFromEquipment(p).defense, false)+(((float) ItemUtils.getOverallItemStatsFromEquipment(p).defense)/200F)));

        }
    }

}
