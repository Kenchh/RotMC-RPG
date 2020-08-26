package me.reykench.commands;

import me.reykench.gui.ClassesGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        p.openInventory(new ClassesGUI(p).getInv());

        return true;
    }

}
