package me.kench.commands;

import me.kench.RotMC;
import me.kench.game.LevelProgression;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class FameCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only for players!");
            return true;
        }

        Player p = (Player) sender;

        if(args.length == 1 || cmd.getName().equalsIgnoreCase("ftop")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.sendMessage(ChatColor.GOLD + "Top 10 characters with highest fame");

                    HashMap<Integer, List<String>> topclasses = RotMC.getInstance().getSqlManager().getTopClasses();
                    for (int i : topclasses.keySet()) {
                        List<String> data = topclasses.get(i);
                        p.sendMessage(ChatColor.YELLOW + "" + i + ". " + data.get(0) + " - " + ChatColor.GOLD + data.get(1) + " " + TextUtils.getDecimalFormat().format(Integer.parseInt(data.get(2))));
                    }
                }
            }.runTaskAsynchronously(RotMC.getInstance());

            return true;
        } else {
            p.performCommand("bal");
            return true;
        }

        /*
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("extract") || args[0].equalsIgnoreCase("exchange")) {
                p.sendMessage(ChatColor.RED + "Usage: /fame extract <amount>");
                return true;
            }
        }

        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("extract") || args[0].equalsIgnoreCase("exchange")) {
                int fame = 0;
                try {
                    fame = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "You need to enter a valid number!");
                    return true;
                }

                PlayerData pd = RotMC.getPlayerData(p);
                PlayerClass playerClass = pd.getMainClass();

                if(RotMC.getPlayerData(p).getMainClass().getLevel() < 20) {
                    p.sendMessage(ChatColor.RED + "You need to be level 20 or higher to extract fame!");
                    return true;
                }

                if(RotMC.getInstance().getLevelProgression().getLevelByXP(playerClass.getXp() - fame) < 20) {
                    p.sendMessage(ChatColor.RED + "You can only extract max. " + TextUtils.getDecimalFormat().format(playerClass.getXp() - RotMC.getInstance().getLevelProgression().getXPByLevel(20)) + " fame!");
                    return true;
                }

                if(fame <= 0) {
                    sender.sendMessage(ChatColor.RED + "Amount has to be over 0xp!");
                    return true;
                }

                playerClass.giveXP(fame * -1, true);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + (fame*-1));

                return true;
            }
        }


        p.sendMessage(ChatColor.RED + "Usage: /fame extract <amount>");
        return true;
         */
    }
}
