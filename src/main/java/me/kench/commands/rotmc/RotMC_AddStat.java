package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import me.kench.player.stat.Stat;
import me.kench.utils.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_AddStat extends SubCommand {
    public RotMC_AddStat() {
        super("addstat", 1, "rotmc.admin", false, "addstats", "givestat", "givestats");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            Messaging.sendMessage(sender, "<red>That player does not exist or is not online!");
            return true;
        }

        Stat stat = Stat.getByName(args[2]);
        if (stat == null) {
            Messaging.sendMessage(sender, String.format("<red>Could not find a Stat called \"%s\"", args[2]));
            return true;
        }

        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(target.getUniqueId())
                .asyncLast(data -> data.getSelectedClass().addPotionStat(stat))
                .sync(() -> Messaging.sendMessage(sender, String.format("<green>Added +1 %s to %s!", stat.getName(), target.getName())))
                .execute();

        return true;
    }

}
