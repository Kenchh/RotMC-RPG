package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.player.PlayerMetadataKey;
import me.kench.rotmc.utils.PlayerUtil;
import me.kench.rotmc.utils.external.worldguard.WorldGuardUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.concurrent.ThreadLocalRandom;

public class DamageListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .delay(1)
                .syncLast(data -> {
                            if (!data.hasSelectedClass()) {
                                return;
                            }

                            Bukkit.dispatchCommand(
                                    Bukkit.getConsoleSender(),
                                    String.format(
                                            "ultimatekits:kit %s %s",
                                            data.getSelectedClass().getRpgClass().getName(),
                                            player.getName()
                                    ));
                        }
                )
                .execute();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        handleAttacker(event);
    }

    private void handleAttacker(EntityDamageByEntityEvent event) {
        event.setCancelled(true);

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Entity defender = event.getEntity();

            // defender is another player and (attacker not in pvp region or defender not in pvp region)
            if (defender instanceof Player && (WorldGuardUtils.notInPvpRegion(attacker) || WorldGuardUtils.notInPvpRegion((Player) defender))) {
                return;
            }

            RotMcPlugin.getInstance().getDataManager().getPlayerData()
                    .chainLoadSafe(attacker.getUniqueId())
                    .syncLast(data -> {
                        if (!data.hasSelectedClass()) {
                            return;
                        }

                        final double damage = event.getDamage()
                                * data.getSelectedClass().getAttackAllStat().getValue();

                        if (defender instanceof Player) {
                            // Only players can dodge, so we're sending extra data to catch this downstream.
                            Player defenderPlayer = (Player) defender;

                            PlayerUtil.addMetadata(
                                    PlayerMetadataKey.AWAITING_PROPAGATED_DAMAGE_FROM,
                                    defenderPlayer,
                                    attacker.getPlayer()
                            );

                            defenderPlayer.damage(damage, null);
                        } else {
                            // Plain damage
                            ((LivingEntity) defender).damage(damage, null);
                        }
                    })
                    .execute();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player defender = (Player) event.getEntity();

            if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                // in the previous method we found that the defender is a player and could potentially dodge,
                // so we forward the event to handleDefender
                event.setCancelled(true);

                // only if awaiting propagated damage, so should only be called once per actual damage
                PlayerUtil.doIfPresent(
                        PlayerMetadataKey.AWAITING_PROPAGATED_DAMAGE_FROM,
                        defender,
                        attacker -> handleDefender((Player) attacker, defender, event)
                );
            } else {
                // this is a normal damage event, we just record what happened in the session data
                RotMcPlugin.getInstance().getSessionManager()
                        .getSession(event.getEntity().getUniqueId())
                        .setLastDamageCause(event.getCause());
            }
        }
    }

    private void handleDefender(Player attacker, Player defender, EntityDamageEvent event) {
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(defender.getUniqueId())
                .syncLast(data -> {
                    if (!data.hasSelectedClass()) {
                        return;
                    }

                    PlayerClass playerClass = data.getSelectedClass();

                    double damage = event.getDamage() / playerClass.getDefenseAllStat().getValue();

                    if (ThreadLocalRandom.current().nextInt(100) + 1 <= playerClass.getEvadeAllStat().getValue()) {
                        defender.playSound(defender.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1F, 1F);
                        defender.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + ChatColor.BOLD.toString() + "DODGE"));
                        damage = 0;
                    }

                    if (defender.getHealth() - damage <= 0) {
                        RotMcPlugin.getInstance().getSessionManager()
                                .getSession(defender.getUniqueId())
                                .setLastKiller(attacker);
                    }

                    if (damage > 0) {
                        defender.damage(damage, null);
                    }
                })
                .execute();
    }
}
