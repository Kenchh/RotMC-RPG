package me.kench.commands.subcommand;

import me.kench.commands.rotmc.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SubCommandManager {

    ArrayList<SubCommand> subCommands = new ArrayList<>();

    public SubCommandManager() {
        subCommands.add(new RotMC_Help());
        subCommands.add(new RotMC_AddStat());
        subCommands.add(new RotMC_DeleteSlot());
        subCommands.add(new RotMC_GiveItem());
        subCommands.add(new RotMC_GiveSlot());
        subCommands.add(new RotMC_GiveXP());
        subCommands.add(new RotMC_Help());
        subCommands.add(new RotMC_Invis());
        subCommands.add(new RotMC_Item());
        subCommands.add(new RotMC_SetXP());
    }

    public boolean getSubCommandAndExecute(CommandSender sender, Command cmd, String label, String[] args) {
        for(int i=0;i<args.length;i++) {
            SubCommand subCommand = getSubCommandByName(args[i]);
            if(getSubCommandByName(args[i]) != null && getSubCommandByName(args[i]).getIndex() == i+1) {

                if(subCommand.isPlayeronly() && !(sender instanceof Player)) {
                    sender.sendMessage("Only for players!");
                    return true;
                }

                if(sender instanceof Player && !((Player) sender).hasPermission(subCommand.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
                    return true;
                }

                return subCommand.execute(sender, cmd, args[i], label, args);
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown command. Type /rotmc help for help.");
            }
        }
        return false;
    }

    private SubCommand getSubCommandByName(String name) {
        for(SubCommand subcmd : subCommands) {
            if(subcmd.getName().equalsIgnoreCase(name)) {
                return subcmd;
            }
            for(String alias : subcmd.getAliases()) {
                if(alias.equalsIgnoreCase(name)) {
                    return subcmd;
                }
            }
        }
        return null;
    }

}
