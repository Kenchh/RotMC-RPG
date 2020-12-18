package me.kench.events;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.player.Stats;
import me.kench.utils.WorldGuardUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class DamageEvent implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        PlayerData pd = RotMC.getPlayerData(p);

        if(pd == null) return;

        PlayerClass pclass = pd.getMainClass();

        if(pclass == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ultimatekits:kit " + pclass.getData().getName() + " " + p.getName());
            }
        }.runTaskLater(RotMC.getInstance(), 5L);

    }

    @EventHandler
    public void onDamaged(EntityDamageEvent e) {

        if(e.isCancelled()) return;

        if(e.getEntity() instanceof Player) {
           if(e.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
               PlayerData pd = RotMC.getPlayerData((Player) e.getEntity());
               pd.lastKiller = "";
               pd.lastDamage = e.getCause().name().toLowerCase();
           }
        }
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if(e.isCancelled()) return;

        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();

            if (!WorldGuardUtils.wgPvP(p) && e.getEntity() instanceof Player) {
                e.setCancelled(true);
                return;
            }

            if(e.getEntity() instanceof Player) {
                e.setCancelled(true);
                return;
            }

            PlayerData pd = RotMC.getPlayerData(p);

            if(pd == null || pd.getMainClass() == null) {
                return;
            }

            float attackAll = pd.getMainClass().attackAllStat;

            if(attackAll > Stats.attackMaxCap) {
                attackAll = Stats.attackMaxCap;
            }

            e.setDamage(e.getDamage() + e.getDamage() * attackAll);

        }

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (!WorldGuardUtils.wgPvP(p) && e.getDamager() instanceof Player) {
                e.setCancelled(true);
                return;
            }

            if(e.getDamager() instanceof Player) {
                e.setCancelled(true);
                return;
            }

            PlayerData pd = RotMC.getPlayerData(p);

            double damage = e.getDamage();

            if(pd == null || pd.getMainClass() == null) {
                return;
            }

            pd.lastKiller = e.getDamager().getName();

            Random r = new Random();
            int random = r.nextInt(100) + 1;

            float dodgeAll = pd.getMainClass().dodgeAllStat;

            if(dodgeAll > Stats.dodgeMaxCap) {
                dodgeAll = Stats.dodgeMaxCap;
            }

            float defenseAll = pd.getMainClass().defenseAllStat;

            if(defenseAll > Stats.defenseMaxCap) {
                defenseAll = Stats.defenseMaxCap;
            }

            double dmg = damage - damage*defenseAll;

            if(random <= dodgeAll) {
                p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1F, 1F);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + ChatColor.BOLD.toString() + "DODGE"));
                dmg = 0;
            }

            e.setCancelled(true);
            if(dmg > 0) {
                p.damage(dmg);
            }

        }
    }


}
