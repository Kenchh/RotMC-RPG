package me.kench.events;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData pd = RotMC.getPlayerData(p);

        if (pd == null || pd.getMainClass() == null) return;

        PlayerClass pc = pd.getMainClass();

        int losexp = (int) (((double) pc.getFame()) * -0.2D);

        if (losexp * -1 > pc.getFame()) {
            losexp = pc.getFame() * -1;
        }

        String msg = "";

        if (pd.lastKiller != null && pd.lastKiller.toUpperCase().contains("CUSTOM") == false && pd.lastKiller != "") {
            msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + pd.lastKiller + " &6and lost &e" + (TextUtils.getDecimalFormat().format(losexp * -1)) + " fame.";
        } else {

            String name = "custom";

            if (pd.lastDamage != null && pd.lastDamage != "") {
                name = pd.lastDamage;
            }

            String words[] = name.split("_");
            name = "";
            for (String word : words) {
                String firstletter = word.substring(0, 1).toUpperCase();
                String other = word.substring(1);

                String uppercaseWord = firstletter + other;
                name += uppercaseWord + " ";
            }
            name = name.substring(0, name.length() - 1);

            msg = "&7[&6Lvl " + pc.getLevel() + " &6" + pc.getData().getName() + "&7] &c" + p.getName() + " &6has died to &c" + name + " &6and lost &e" + (TextUtils.getDecimalFormat().format(losexp * -1)) + " fame.";
        }

        if (losexp < 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + (losexp * -1));

            pc.giveFame(losexp, true);
            pc.getStats().zeroStats();
        }

        if (pd.getMainClass() != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/ultimatekits:kit " + p.getName() + " " + pd.getMainClass().getRpgClass().getName().toLowerCase());
        }

        e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', msg));

    }

}
