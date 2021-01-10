package me.kench.commands;

import me.kench.gui.glow.GlowGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlowCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(new GlowGui(player).getInv());

        return true;
    }
}
