package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import me.kench.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_GiveXP extends SubCommand {

    public RotMC_GiveXP() {
        super("givexp", 1, "rotmc.admin", false);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

        Player tp = Bukkit.getPlayer(args[1]);

        if(tp == null || !tp.isOnline()) {
            if(sender instanceof Player)
                sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        PlayerData pd = RotMC.getPlayerData(tp);

        if(pd.getMainClass() == null) {
            if(sender instanceof Player)
                sender.sendMessage(ChatColor.RED + "Error: This player does not have a class selected.");
            return true;
        }

        if(amount <= 0) {
            sender.sendMessage("Amount has to be over 0xp!");
            return true;
        }

        if(sender instanceof Player)
            sender.sendMessage(ChatColor.GREEN + tp.getName() + " has received " + ChatColor.GOLD + amount + ChatColor.GREEN + " xp!");

        pd.getMainClass().giveXP(amount, false);
        return true;
    }

}
