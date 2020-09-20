package me.kench.commands.rotmc;

import me.kench.commands.subcommand.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RotMC_Help extends SubCommand {

    public RotMC_Help() {
        super("help", 1, "rotmc.admin", false);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

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

        for(String s : help) {
            sender.sendMessage(s);
        }

        return true;
    }

}
