package me.kench.commands.rotmc;

import me.kench.commands.subcommand.SubCommand;
import me.kench.items.EssenceItem;
import me.kench.items.GemItem;
import me.kench.items.RuneItem;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.GemType;
import me.kench.items.stats.RuneType;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RotMC_GiveItem extends SubCommand {

    public RotMC_GiveItem() {
        super("giveitem", 1, "rotmc.admin", true, "giveitems");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {

        Player p = (Player) sender;

        if (args[1].equalsIgnoreCase("gem")) {

            boolean exists = false;
            for (GemType gt : GemType.values()) {
                if (args[2].equalsIgnoreCase(gt.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                p.sendMessage(ChatColor.RED + "That gem doesn't exist!");
                return true;
            }

            ItemStack gem = new ItemStack(Material.CARROT_ON_A_STICK);

            ItemMeta meta = gem.getItemMeta();
            meta.setCustomModelData(GemType.valueOf(args[2].toUpperCase()).getModeldata());
            meta.setDisplayName(GemType.valueOf(args[2].toUpperCase()).getPrefix() + GemType.valueOf(args[2].toUpperCase()).getName() + " V");

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " " + args[3] + "%",
                    ChatColor.GRAY + "▸ YYY:" + ChatColor.GOLD + " ZZZ"
            ));

            gem.setItemMeta(meta);

            GemItem gemItem = new GemItem(gem);

            p.getInventory().addItem(gemItem.getItem());
        }

        if (args[1].equalsIgnoreCase("rune")) {

            boolean exists = false;
            for (RuneType rt : RuneType.values()) {
                if (args[2].equalsIgnoreCase(rt.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                p.sendMessage(ChatColor.RED + "That rune doesn't exist!");
                return true;
            }

            ItemStack gem = new ItemStack(Material.CARROT_ON_A_STICK);

            ItemMeta meta = gem.getItemMeta();
            meta.setCustomModelData(RuneType.valueOf(args[2].toUpperCase()).getModeldata());
            meta.setDisplayName(RuneType.valueOf(args[2].toUpperCase()).getPrefix() + RuneType.valueOf(args[2].toUpperCase()).getName());

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " " + args[3] + "%",
                    ChatColor.GRAY + "▸ Permanent Effect:" + ChatColor.GOLD + " ZZZ"
            ));

            gem.setItemMeta(meta);

            RuneItem runeItem = new RuneItem(gem);

            p.getInventory().addItem(runeItem.getItem());
        }

        if (args[1].equalsIgnoreCase("essence")) {

            boolean exists = false;
            for (EssenceType et : EssenceType.values()) {
                if (args[2].equalsIgnoreCase(et.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                p.sendMessage(ChatColor.RED + "That essence doesn't exist!");
                return true;
            }

            ItemStack essence = new ItemStack(Material.CARROT_ON_A_STICK);

            ItemMeta meta = essence.getItemMeta();
            meta.setCustomModelData(EssenceType.valueOf(args[2].toUpperCase()).getModeldata());
            meta.setDisplayName(EssenceType.valueOf(args[2].toUpperCase()).getPrefix() + EssenceType.valueOf(args[2].toUpperCase()).getName());

            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 100%",
                    ChatColor.GRAY + "▸ Vanity Effect:" + ChatColor.GOLD + " ZZZ"
            ));

            essence.setItemMeta(meta);

            EssenceItem essenceItem = new EssenceItem(essence);

            p.getInventory().addItem(essenceItem.getItem());
        }

        if (args[1].equalsIgnoreCase("mythicdust")) {
            ItemStack dust = new ItemStack(Material.GLOWSTONE_DUST);
            ItemMeta meta = dust.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mythic Dust VI");
            if (args.length >= 3) {
                meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mythic Dust " + TextUtils.getRomanFromNumber(Integer.parseInt(args[2])));
            }
            dust.setItemMeta(meta);

            p.getInventory().addItem(dust);
        }

        if (args[1].equalsIgnoreCase("extractor")) {
            ItemStack dust = new ItemStack(Material.BLAZE_POWDER);
            ItemMeta meta = dust.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Extractor");
            dust.setItemMeta(meta);

            p.getInventory().addItem(dust);
        }

        return true;
    }

}
