package me.kench.commands;

import me.kench.gui.GlowGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlowCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }

        Player p = (Player) sender;

        p.openInventory(new GlowGUI(p).getInv());

        return true;
    }
}
