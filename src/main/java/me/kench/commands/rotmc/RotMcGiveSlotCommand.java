package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.Subcommand;
import me.kench.utils.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMcGiveSlotCommand extends Subcommand {
    public RotMcGiveSlotCommand() {
        super("giveslot", 1, "rotmc.admin", false, "addslot", "addslots", "giveslots");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            Messaging.sendMessage(sender, "<red>That player does not exist or is not online!");
            return true;
        }

        int amount = -1;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException ignored) {
            Messaging.sendMessage(sender, "<red>Please enter a number.");
            return true;
        }

        if (amount < 1) {
            Messaging.sendMessage(sender, "<red>Please use a positive number.");
            return true;
        }

        final int finalAmount = amount;
        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(target.getUniqueId())
                .asyncLast(data -> data.setMaxSlots(Math.min(data.getMaxSlots() + finalAmount, 36)))
                .sync(() -> {
                    Messaging.sendMessage(sender, String.format("<green>Added %d profile slots for %s!", finalAmount, target.getName()));
                })
                .execute();

        return true;
    }
}
