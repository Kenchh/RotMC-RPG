package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_GiveSlot extends SubCommand {
    public RotMC_GiveSlot() {
        super("giveslot", 1, "rotmc.admin", false, "addslot", "addslots", "giveslots");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        Player tp = Bukkit.getPlayer(args[1]);

        if (tp == null || !tp.isOnline()) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            }

            return true;
        }

        int amount = Integer.parseInt(args[2]);

        RotMC.getInstance().getDataManager().getAccessor().getPlayerData().modify(tp.getUniqueId(), data -> {
            data.setMaxSlots(Math.min(data.getMaxSlots() + amount, 36));
            return data;
        });

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " profile slot for " + tp.getName() + " !");
        }

        return true;
    }
}
