package me.reykench.commands.rotmc;

import me.reykench.RotMC;
import me.reykench.commands.subcommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_DeleteSlot extends SubCommand {

    public RotMC_DeleteSlot() {
        super("deleteslot", 1, "rotmc.admin", false, "removeslot", "removeslots", "deleteslots");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

        Player tp = Bukkit.getPlayer(args[1]);

        if(tp == null || !tp.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        if(RotMC.getPlayerData(tp).maxSlots - amount < 2) {
            RotMC.getPlayerData(tp).maxSlots = 2;
        } else {
            RotMC.getPlayerData(tp).maxSlots -= amount;
        }
        RotMC.getInstance().getDatabase().update(tp, null);
        sender.sendMessage(ChatColor.GREEN + "Deleted " + amount + " profile slot for " + tp.getName() + " !");
        return true;
    }

}
