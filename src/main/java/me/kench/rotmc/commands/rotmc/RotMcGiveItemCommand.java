package me.kench.rotmc.commands.rotmc;

import me.kench.rotmc.commands.subcommand.Subcommand;
import me.kench.rotmc.items.EssenceItem;
import me.kench.rotmc.items.GemItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.items.RuneItem;
import me.kench.rotmc.items.stats.EssenceType;
import me.kench.rotmc.items.stats.GemType;
import me.kench.rotmc.items.stats.RuneType;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotMcGiveItemCommand extends Subcommand {
    public RotMcGiveItemCommand() {
        super("giveitem", 1, "rotmc.admin", true, "giveitems");
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        Player senderPlayer = (Player) sender;

        if (args[1].equalsIgnoreCase("gem")) {
            boolean exists = false;
            for (GemType gt : GemType.values()) {
                if (args[2].equalsIgnoreCase(gt.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                senderPlayer.sendMessage(ChatColor.RED + "That gem doesn't exist!");
                return true;
            }

            GemItem gemItem = new GemItem(
                    ItemBuilder.create(Material.CARROT_ON_A_STICK)
                            .name(GemType.valueOf(args[2].toUpperCase()).getPrefix() + GemType.valueOf(args[2].toUpperCase()).getName() + " V")
                            .lore(
                                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " " + args[3] + "%",
                                    ChatColor.GRAY + "▸ YYY:" + ChatColor.GOLD + " ZZZ"
                            )
                            .modelData(GemType.valueOf(args[2].toUpperCase()).getModelData())
                            .build()
            );

            senderPlayer.getInventory().addItem(gemItem.getItem());
        } else if (args[1].equalsIgnoreCase("rune")) {
            boolean exists = false;
            for (RuneType rt : RuneType.values()) {
                if (args[2].equalsIgnoreCase(rt.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                senderPlayer.sendMessage(ChatColor.RED + "That rune doesn't exist!");
                return true;
            }

            RuneItem runeItem = new RuneItem(
                    ItemBuilder.create(Material.CARROT_ON_A_STICK)
                            .name(RuneType.valueOf(args[2].toUpperCase()).getPrefix() + RuneType.valueOf(args[2].toUpperCase()).getName())
                            .lore(
                                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " " + args[3] + "%",
                                    ChatColor.GRAY + "▸ Permanent Effect:" + ChatColor.GOLD + " ZZZ"
                            )
                            .modelData(RuneType.valueOf(args[2].toUpperCase()).getModelData())
                            .build()
            );

            senderPlayer.getInventory().addItem(runeItem.getItem());
        } else if (args[1].equalsIgnoreCase("essence")) {
            boolean exists = false;
            for (EssenceType et : EssenceType.values()) {
                if (args[2].equalsIgnoreCase(et.toString())) {
                    exists = true;
                }
            }

            if (!exists) {
                senderPlayer.sendMessage(ChatColor.RED + "That essence doesn't exist!");
                return true;
            }

            EssenceItem essenceItem = new EssenceItem(
                    ItemBuilder.create(Material.CARROT_ON_A_STICK)
                            .name(EssenceType.valueOf(args[2].toUpperCase()).getPrefix() + EssenceType.valueOf(args[2].toUpperCase()).getName())
                            .lore(
                                    ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 100%",
                                    ChatColor.GRAY + "▸ Vanity Effect:" + ChatColor.GOLD + " ZZZ"
                            )
                            .modelData(EssenceType.valueOf(args[2].toUpperCase()).getModelData())
                            .build()
            );

            senderPlayer.getInventory().addItem(essenceItem.getItem());
        } else if (args[1].equalsIgnoreCase("mythicdust")) {
            senderPlayer.getInventory().addItem(
                    ItemBuilder.create(Material.GLOWSTONE_DUST)
                            .name(ChatColor.LIGHT_PURPLE + "Mythiuc Dust " + (args.length >= 3 ? TextUtils.getRomanFromNumber(Integer.parseInt(args[2])) : "VI"))

                            .build()
            );
        } else if (args[1].equalsIgnoreCase("extractor")) {
            senderPlayer.getInventory().addItem(
                    ItemBuilder.create(Material.BLAZE_POWDER)
                            .name(ChatColor.GREEN + "Extractor")
                            .build()
            );
        }

        return true;
    }
}
