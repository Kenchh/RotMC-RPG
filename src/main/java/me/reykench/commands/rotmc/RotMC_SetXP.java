package me.reykench.commands.rotmc;

import me.reykench.RotMC;
import me.reykench.commands.subcommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_SetXP extends SubCommand {

    public RotMC_SetXP() {
        super("setxp", 1, "rotmc.admin", false);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

        Player tp = Bukkit.getPlayer(args[1]);

        if(tp == null || !tp.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        if(RotMC.getPlayerData(tp).getMainClass() == null) {
            sender.sendMessage(ChatColor.RED + "Error: This player does not have a class selected.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + tp.getName() + "'s xp has been set to " + ChatColor.GOLD + amount + ChatColor.GREEN + "!");
        RotMC.getPlayerData(tp).getMainClass().setXp(amount);
        return true;
    }

}
