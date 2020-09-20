package me.kench.commands.rotmc;

import me.kench.RotMC;
import me.kench.commands.subcommand.SubCommand;
import me.kench.items.GameItem;
import me.kench.player.PlayerClass;
import org.bukkit.ChatColor;
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

        Player p = (Player) sender;

        PlayerClass pc;

        try {
            pc = RotMC.getPlayerData(p).getMainClass();
        } catch (NullPointerException e) {
            p.sendMessage(ChatColor.RED + "You need to have a class selected!");
            return true;
        }

        if(p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) return true;

        GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());

        if(args[1].equalsIgnoreCase("addsocket")) {

            if(args[2].equalsIgnoreCase("gem")) {
                gameItem.getStats().gemsockets++;
            }

            if(args[2].equalsIgnoreCase("rune")) {
                gameItem.getStats().hasRuneSocket = true;
            }

            if(args[2].equalsIgnoreCase("essence")) {
                gameItem.getStats().hasEssenceSocket = true;
            }

        }

        if(args[1].equalsIgnoreCase("removesocket")) {

            if(args[2].equalsIgnoreCase("gem")) {
                gameItem.getStats().gemsockets--;
            }

            if(args[2].equalsIgnoreCase("rune")) {
                gameItem.getStats().hasRuneSocket = false;
            }

            if(args[2].equalsIgnoreCase("essence")) {
                gameItem.getStats().hasEssenceSocket = false;
            }

        }

        gameItem.update();
        return true;
    }

}
