package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import me.kench.utils.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RotMC_Invis extends SubCommand {
    public RotMC_Invis() {
        super("invis", 1, "rotmc.admin", false);
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
        RotMC.newChain()
                .sync(() -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.hidePlayer(RotMC.getInstance(), target);
                    }

                    target.addPotionEffect(new PotionEffect(
                            PotionEffectType.INVISIBILITY,
                            20 * finalAmount,
                            0
                    ));
                })
                .delay(20 * finalAmount)
                .sync(() -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.showPlayer(RotMC.getInstance(), target);
                    }

                    target.removePotionEffect(PotionEffectType.INVISIBILITY);
                })
                .execute();

        return true;
    }

}
