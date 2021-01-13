package me.kench.commands;

import me.kench.RotMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RotMcCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            return RotMC.getInstance().getSubCommandManager().getSubCommandAndExecute(sender, cmd, label, args);
        } else {
            return sendCommandHelp(sender);
        }
    }

    public static boolean sendCommandHelp(CommandSender sender) {
        List<String> help = Arrays.asList(
                ChatColor.GREEN + "/rotmc help - Shows this",
                ChatColor.AQUA + "/rotmc givexp <player> <amount> - Gives a player's xp.",
                ChatColor.AQUA + "/rotmc setxp <player> <amount> - Sets a player's xp.",
                ChatColor.AQUA + "/rotmc invis <player> <duration> - Makes a player go invis.",
                ChatColor.AQUA + "/rotmc giveslots <player> <amount> - Gives profile slots",
                ChatColor.AQUA + "/rotmc deleteslots <player> <amount> - Deletes profile slots",
                ChatColor.AQUA + "/rotmc addstats <player> <stat> - Adds a stat to a player",
                ChatColor.GOLD + "/rotmc item addsocket <gem|rune|essence>",
                ChatColor.GOLD + "/rotmc giveitem <gem|rune|essence> <type> [successchance]",
                ChatColor.GOLD + "/rotmc giveitem <extractor|mythicdust>"
        );

        for (String s : help) {
            sender.sendMessage(s);
        }

        return true;
    }

}
