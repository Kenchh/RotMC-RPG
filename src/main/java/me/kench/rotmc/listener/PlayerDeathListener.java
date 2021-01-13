package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.session.PlayerSession;
import me.kench.rotmc.utils.EnumUtil;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerSession session = data.getSession();

                    PlayerClass selectedClass = data.getSelectedClass();
                    if (selectedClass != null) {
                        long loseFame = (long) (selectedClass.getFame() * 0.2D);
                        if (loseFame > selectedClass.getFame()) {
                            loseFame = selectedClass.getFame();
                        }

                        String killer = "custom";

                        if (session.getLastKiller() != null) {
                            killer = session.getLastKiller().getName();
                            session.setLastKiller(null);
                        } else {
                            String name = "custom";

                            if (session.getLastDamageCause() != null) {
                                name = EnumUtil.prettifyConstant(session.getLastDamageCause());
                                session.setLastDamageCause(null);
                            }

                            killer = name;
                        }

                        // TODO: not sure this will work tbh
                        event.setDeathMessage(TextUtils.parseMini(String.format(
                                "<gray>[<gold>Lvl %d %s<gray>] <red>%s <gold>has died to <red>%s <gold>and lost <yellow>%s <red>fame.",
                                selectedClass.getLevel(),
                                selectedClass.getRpgClass().getName(),
                                player.getName(),
                                killer,
                                TextUtils.getDecimalFormat().format(loseFame * -1)
                        )));

                        if (loseFame > 0) {
                            // TODO: they get money on death?
                            Bukkit.dispatchCommand(
                                    Bukkit.getConsoleSender(),
                                    String.format(
                                            "eco give %s %d",
                                            player.getName(),
                                            loseFame
                                    )
                            );

                            selectedClass.giveFame(loseFame * -1, true);
                            selectedClass.getStats().getPotionStats().zeroStats();
                        }

                        Bukkit.dispatchCommand(
                                Bukkit.getConsoleSender(),
                                String.format(
                                        "/ultimatekits:kit %s %s",
                                        player.getName(),
                                        selectedClass.getRpgClass().getName().toLowerCase()
                                )
                        );
                    }
                })
                .execute();
    }
}
