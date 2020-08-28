package me.kench.commands;

import me.kench.RotMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class AccountCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /account top");
            return true;
        }

        if(args[0].equalsIgnoreCase("top")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sender.sendMessage(ChatColor.GOLD + "Top 10 accounts with highest fame");

                    HashMap<Integer, List<String>> topaccounts = RotMC.getInstance().getDatabase().getTopProfiles();
                    for (int i : topaccounts.keySet()) {
                        List<String> data = topaccounts.get(i);
                        sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + data.get(0) + " - " + data.get(1));
                    }
                }
            }.runTaskAsynchronously(RotMC.getInstance());
            return true;
        }

        return true;
    }
}
