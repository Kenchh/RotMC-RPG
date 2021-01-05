package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_AddStat extends SubCommand {

    public RotMC_AddStat() {
        super("addstat", 1, "rotmc.admin", false, "addstats", "givestat", "givestats");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

        Player tp = Bukkit.getPlayer(args[1]);

        if (tp == null || !tp.isOnline()) {
            if (sender instanceof Player)
                sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        RotMC.getPlayerData(tp).getMainClass().addStat(args[2]);

        if (sender instanceof Player)
            sender.sendMessage(ChatColor.GREEN + "Added +1 " + args[2] + " to " + tp.getName() + "!");
        return true;
    }

}
