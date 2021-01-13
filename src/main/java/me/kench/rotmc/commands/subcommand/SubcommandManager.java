package me.kench.rotmc.commands.subcommand;

import me.kench.rotmc.commands.rotmc.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SubcommandManager {
    ArrayList<Subcommand> subcommands = new ArrayList<>();

    public SubcommandManager() {
        subcommands.add(new RotMcHelpCommand());
        subcommands.add(new RotMcAddStatCommand());
        subcommands.add(new RotMcDeleteSlotCommand());
        subcommands.add(new RotMcGiveItemCommand());
        subcommands.add(new RotMcGiveSlotCommand());
        subcommands.add(new RotMcGiveXpCommand());
        subcommands.add(new RotMcHelpCommand());
        subcommands.add(new RotMcInvisCommand());
        subcommands.add(new RotMcItemCommand());
        subcommands.add(new RotMcSetXpCommand());
    }

    public boolean getSubCommandAndExecute(CommandSender sender, Command cmd, String label, String[] args) {
        for (int i = 0; i < args.length; i++) {
            Subcommand subCommand = getSubCommandByName(args[i]);
            if (getSubCommandByName(args[i]) != null && getSubCommandByName(args[i]).getIndex() == i + 1) {

                if (subCommand.isPlayeronly() && !(sender instanceof Player)) {
                    sender.sendMessage("Only for players!");
                    return true;
                }

                if (sender instanceof Player && !sender.hasPermission(subCommand.getPermission())) {
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

    private Subcommand getSubCommandByName(String name) {
        for (Subcommand subcmd : subcommands) {
            if (subcmd.getName().equalsIgnoreCase(name)) {
                return subcmd;
            }
            for (String alias : subcmd.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return subcmd;
                }
            }
        }
        return null;
    }
}
