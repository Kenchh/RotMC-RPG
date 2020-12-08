package me.kench.commands;

import me.kench.RotMC;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class GuildCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage(ChatColor.GOLD + "Top 10 guilds with highest fame");

                HashMap<Integer, List<String>> topGuilds = RotMC.getInstance().getSqlManager().getTopGuilds();
                for (int i : topGuilds.keySet()) {
                    List<String> data = topGuilds.get(i);
                    sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + data.get(0) + " - " + TextUtils.getDecimalFormat().format(Integer.parseInt(data.get(1))));
                }
            }
        }.runTaskAsynchronously(RotMC.getInstance());


        return true;
    }
}
