package me.reykench.commands;

import me.reykench.RotMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RotMCCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return RotMC.getInstance().getSubCommandManager().getSubCommandAndExecute(sender, cmd, label, args);
    }

}
