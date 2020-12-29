package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import me.kench.player.PlayerData;
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

        if(tp == null || !tp.isOnline()) {
            if(sender instanceof Player)
                sender.sendMessage(ChatColor.RED + "That player does not exist or is not online!");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        PlayerData pd = RotMC.getPlayerData(tp);

        if(pd.maxSlots - amount < 2) {
            pd.maxSlots = 2;
        } else {
            pd.maxSlots -= amount;
        }
        RotMC.getInstance().getSqlManager().update(tp, null);

        if(sender instanceof Player)
            sender.sendMessage(ChatColor.GREEN + "Deleted " + amount + " profile slot for " + tp.getName() + " !");
        return true;
    }

}
