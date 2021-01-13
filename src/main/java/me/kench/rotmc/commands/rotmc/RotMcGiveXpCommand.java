package me.kench.rotmc.commands.rotmc;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.commands.subcommand.Subcommand;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMcGiveXpCommand extends Subcommand {
    public RotMcGiveXpCommand() {
        super("givexp", 1, "rotmc.admin", false);
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
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(target.getUniqueId())
                .syncLast(data -> {
                    PlayerClass playerClass = data.getSelectedClass();
                    if (playerClass != null) {
                        Messaging.sendMessage(sender, String.format("<green>%s has received <gold>%d <green>fame!", target.getName(), finalAmount));
                        playerClass.giveFame(finalAmount, false);
                    }
                })
                .execute();

        return true;
    }
}
