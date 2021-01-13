package me.kench.rotmc.commands;

import me.kench.rotmc.gui.glow.GlowGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }

        new GlowGui().display((Player) sender);
        return true;
    }
}