package me.reykench.events;

import me.reykench.RotMC;
import me.reykench.player.PlayerClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        if(RotMC.getPlayerData(p) == null || RotMC.getPlayerData(p).getMainClass() == null) return;

        PlayerClass pc = RotMC.getPlayerData(p).getMainClass();

        int losexp = (int) (((double) pc.getXp()) * -0.2D);

        if(losexp*-1 > pc.getXp()) {
            losexp = pc.getXp() * -1;
        }

        String msg = "";

        EntityDamageEvent lastDamageCause = p.getLastDamageCause();
        if(lastDamageCause != null) {
            if (lastDamageCause instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent lastEntityDamageEvent = (EntityDamageByEntityEvent) lastDamageCause;
                Entity killer = lastEntityDamageEvent.getDamager();

                if(killer != null) {
                    if(killer.getName() != null) {
                        msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + killer.getName() + " &6and lost &e" + (losexp * -1) + " fame.";
                    } else {
                        msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + killer.getType() + " &6and lost &e" + (losexp * -1) + " fame.";
                    }
                }

            } else {

                String name = p.getLastDamageCause().getCause().name().toLowerCase();

                String words[] = name.split("_");
                name = "";
                for(String word : words) {
                    String firstletter = word.substring(0, 1).toUpperCase();
                    String other = word.substring(1);

                    String uppercaseWord = firstletter + other;
                    name += uppercaseWord + " ";
                }
                name = name.substring(0, name.length() - 1);

                msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + name + " &6and lost &e" + (losexp * -1) + " fame.";
            }
        } else {

            String name = p.getLastDamageCause().getCause().name().toLowerCase();

            String words[] = name.split("_");
            name = "";
            for(String word : words) {
                String firstletter = word.substring(0, 1).toUpperCase();
                String other = word.substring(1);

                String uppercaseWord = firstletter + other;
                name += uppercaseWord + " ";
            }
            name = name.substring(0, name.length() - 1);

            msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + name + " &6and lost &e" + (losexp * -1) + " fame.";
        }

        if(losexp < 0)
            pc.giveXP(losexp);
        e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', msg));

    }

}
