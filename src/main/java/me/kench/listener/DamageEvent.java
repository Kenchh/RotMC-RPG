package me.kench.listener;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.session.PlayerSession;
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

import java.util.concurrent.ThreadLocalRandom;

public class DamageEvent implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(PlayerData::getSelectedClass)
                .delay(1)
                .syncLast(playerClass -> Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        String.format("ultimatekits:kit %s %s", playerClass.getRpgClass().getName(), player.getName()))
                )
                .execute();
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent event) {
        if (event.isCancelled()) return;

        if (event.getEntity() instanceof Player) {
            if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
                RotMC.getInstance().getDataManager().getPlayerData()
                        .chainLoadSafe(event.getEntity().getUniqueId())
                        .asyncLast(data -> {
                            PlayerSession session = data.getSession();
                            session.setLastKiller("");
                            session.setLastDamage(event.getCause().name().toLowerCase());
                        })
                        .execute();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (WorldGuardUtils.notInPvpRegion(player) && event.getEntity() instanceof Player) {
                event.setCancelled(true);
                return;
            }

            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
                return;
            }

            RotMC.getInstance().getDataManager().getPlayerData()
                    .chainLoadSafe(player.getUniqueId())
                    .async(data -> data.getSelectedClass().getAttackAllStat())
                    .syncLast(stat -> event.setDamage(event.getDamage() + event.getDamage() * stat))
                    .execute();
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (WorldGuardUtils.notInPvpRegion(player) && event.getDamager() instanceof Player) {
                event.setCancelled(true);
                return;
            }

            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
                return;
            }

            RotMC.getInstance().getDataManager().getPlayerData()
                    .chainLoadSafe(player.getUniqueId())
                    .async(data -> {
                        data.getSession().setLastKiller(event.getDamager().getName());
                        return data.getSelectedClass();
                    })
                    .syncLast(playerClass -> {
                        double damage = event.getDamage();
                        damage = damage - (damage * playerClass.getDefenseAllStat());

                        if (ThreadLocalRandom.current().nextInt(100) + 1 <= playerClass.getEvadeAllStat()) {
                            player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1F, 1F);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + ChatColor.BOLD.toString() + "DODGE"));
                            damage = 0;
                        }

                        event.setCancelled(true);

                        if (damage > 0) {
                            player.damage(damage);
                        }
                    })
                    .execute();
        }
    }
}
