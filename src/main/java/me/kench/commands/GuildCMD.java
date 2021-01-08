package me.kench.commands;

import me.kench.RotMC;
import me.kench.utils.Messaging;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class GuildCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        RotMC.getInstance().getDataManager().getPlayerData()
                .getTop10Guilds()
                .syncLast(top10 -> {
                    Messaging.sendMessage(sender, "<gold>Top 10 Guilds with Highest Fame");
                    final int[] count = { 1 };
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
