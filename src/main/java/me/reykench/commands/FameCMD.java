package me.reykench.commands;

import me.reykench.RotMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class FameCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /fame top");
            return true;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage(ChatColor.GOLD + "Top 10 characters with highest fame");

                HashMap<Integer, List<String>> topclasses = RotMC.getInstance().getDatabase().getTopClasses();
                for(int i : topclasses.keySet()) {
                    List<String> data = topclasses.get(i);
                    sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + data.get(0) + " - " + ChatColor.GOLD + data.get(1) + " " + data.get(2));
                }
            }
        }.runTaskAsynchronously(RotMC.getInstance());

        return true;
    }
}
