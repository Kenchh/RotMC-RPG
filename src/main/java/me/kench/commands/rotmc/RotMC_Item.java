package me.kench.commands.rotmc;

import me.kench.commands.subcommand.SubCommand;
import me.kench.items.GameItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMC_Item extends SubCommand {

    public RotMC_Item() {
        super("item", 1, "rotmc.admin", true);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        Player senderPlayer = (Player) sender;
        if (senderPlayer.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return true;
        }

        GameItem gameItem = new GameItem(senderPlayer.getInventory().getItemInMainHand());

        if (args[1].equalsIgnoreCase("addsocket")) {
            if (args[2].equalsIgnoreCase("gem")) {
                gameItem.getStats().incrementGemSockets();
            } else if (args[2].equalsIgnoreCase("rune")) {
                gameItem.getStats().setHasEssenceSocket(true);
            } else if (args[2].equalsIgnoreCase("essence")) {
                gameItem.getStats().setHasEssenceSocket(true);
            }
        } else if (args[1].equalsIgnoreCase("removesocket")) {
            if (args[2].equalsIgnoreCase("gem")) {
                gameItem.getStats().decrementGemSockets();
            } else if (args[2].equalsIgnoreCase("rune")) {
                gameItem.getStats().setHasRuneSocket(false);
            } else if (args[2].equalsIgnoreCase("essence")) {
                gameItem.getStats().setHasEssenceSocket(false);
            }
        }

        gameItem.update();
        return true;
    }
}
