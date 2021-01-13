package me.kench.rotmc.commands;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadTop10Guilds()
                .syncLast(top10 -> {
                    Messaging.sendMessage(sender, "<gold>Top 10 Guilds with Highest Fame");
                    final int[] count = {1};
                    top10.forEach((guild, fame) -> {
                        Messaging.sendMessage(sender, String.format(
                                "<yellow>%d. %s - %d",
                                count[0],
                                guild.getName(),
                                fame
                        ));
                        count[0]++;
                    });
                })
                .execute();

        return true;
    }
}
