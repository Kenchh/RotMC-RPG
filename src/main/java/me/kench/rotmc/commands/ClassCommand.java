package me.kench.rotmc.commands;

import me.kench.rotmc.gui.chooseclass.ChooseClassGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        new ChooseClassGui().display((Player) sender);

        return true;
    }
}