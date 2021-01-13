package me.kench.rotmc.commands;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for players!");
            return true;
        }

        Player senderPlayer = (Player) sender;
        if (args.length == 1 || cmd.getName().equalsIgnoreCase("ftop")) {
            RotMcPlugin.getInstance().getDataManager().getPlayerData().chainLoadTop10Classes()
                    .syncLast(top10 -> {
                        Messaging.sendMessage(sender, "<gold>Top 10 Characters with Highest Fame");

                        final int[] count = {1};
                        top10.forEach((playerClass, fame) -> {
                            Messaging.sendMessage(sender, String.format(
                                    "<yellow>%d. %s - <gold>%s %d",
                                    count[0],
                                    playerClass.getOfflinePlayer(),
                                    playerClass.getRpgClass().getName(),
                                    fame
                            ));

                            count[0]++;
                        });
                    })
                    .execute();
        } else {
            senderPlayer.performCommand("bal");
        }

        return true;
    }
}
