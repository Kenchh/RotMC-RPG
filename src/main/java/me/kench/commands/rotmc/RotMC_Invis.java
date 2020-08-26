package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
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

        Player tp = Bukkit.getPlayer(args[1]);

        if(tp == null || !tp.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        for(Player pp : Bukkit.getOnlinePlayers()) {
            pp.hidePlayer(tp);
        }
        tp.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*amount, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player pp : Bukkit.getOnlinePlayers()) {
                    pp.showPlayer(tp);
                }
                tp.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }.runTaskLater(RotMC.getInstance(), 20*amount);
        return true;
    }

}
