package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
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

        if (tp == null || !tp.isOnline()) {
            if (sender instanceof Player)
                sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        RotMC.getInstance().getDataManager().getAccessor().getPlayerData().modify(tp.getUniqueId(), data -> {
            data.setMaxSlots(Math.max(data.getMaxSlots() - amount, 2));
            return data;
        });

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + "Deleted " + amount + " profile slot for " + tp.getName() + " !");
        }

        return true;
    }
}
