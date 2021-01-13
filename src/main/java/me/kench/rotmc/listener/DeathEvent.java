package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.session.PlayerSession;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerSession session = data.getSession();
                    PlayerClass selectedClass = data.getSelectedClass();

                    if (selectedClass != null) {
                        long loseFame = (long) (((double) selectedClass.getFame()) * -0.2D);
                        if (loseFame * -1 > selectedClass.getFame()) {
                            loseFame = selectedClass.getFame() * -1;
                        }

                        String killer = "custom";
                        if (session.getLastKiller() != null && !session.getLastKiller().equals("") && !session.getLastKiller().toUpperCase().contains("CUSTOM")) {
                            killer = session.getLastKiller();
                        } else {
                            String name = "custom";
                            if (session.getLastDamage() != null && !session.getLastDamage().equals("")) {
                                name = session.getLastDamage();
                            }

                            killer = Arrays.stream(name.split("_"))
                                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                                    .collect(Collectors.joining(" "));
                        }

                        event.setDeathMessage(TextUtils.parseMini(String.format(
                                "<gray>[<gold>Lvl %d %s<gray>] <red>%s <gold>has died to <red>%s <gold>and lost <yellow>%s <red>fame.",
                                selectedClass.getLevel(),
                                selectedClass.getRpgClass().getName(),
                                player.getName(),
                                killer,
                                TextUtils.getDecimalFormat().format(loseFame * -1)
                        )));

                        if (loseFame < 0) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " " + (loseFame * -1));
                            selectedClass.giveFame(loseFame, true);
                            selectedClass.getStats().getPotionStats().zeroStats();
                        }

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/ultimatekits:kit " + player.getName() + " " + selectedClass.getRpgClass().getName().toLowerCase());
                    }
                })
                .execute();
    }

}
