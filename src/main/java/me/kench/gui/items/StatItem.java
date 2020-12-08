package me.kench.gui.items;

import me.kench.player.PlayerClass;
import me.kench.player.Stats;
import me.kench.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StatItem extends ItemStack {

    public StatItem(PlayerClass pc, String s) {
        Stats stats = pc.getStats();
        this.setType(Material.CARROT_ON_A_STICK);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();

        switch (s) {
            case "Health":
                float healthGemStat = stats.getHealth(ItemUtils.getOverallGemStatsFromEquipment(pc.getPlayer()).health, true, true);
                float healthItemStat = ItemUtils.getOverallItemStatsFromEquipment(pc.getPlayer()).health;

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + s + ChatColor.translateAlternateColorCodes('&',"&7▸ " + ChatColor.WHITE + "+" + (stats.getHealth(stats.health, true, false) + healthItemStat + healthGemStat)));

                String vh = "&e" + (int) stats.health;

                if(stats.health >= pc.getLevel()) {
                    vh.replace("&e","&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7From Potions: (" + vh + "&7/&6" + stats.getCap(pc.getData().getName(), "Health") + "&7) = &c" + stats.getDodge(stats.health, true, false)),
                        ChatColor.translateAlternateColorCodes('&',"&7From Item Stats: " + ChatColor.RED + healthItemStat),
                        ChatColor.translateAlternateColorCodes('&',"&7From Gems: " + ChatColor.RED + healthGemStat)
                        ));

                meta.setCustomModelData(205);
                break;
            case "Attack":
                float attackGemStat = stats.getAttack(ItemUtils.getOverallGemStatsFromEquipment(pc.getPlayer()).attack, true, true);
                float attackItemStat = ItemUtils.getOverallItemStatsFromEquipment(pc.getPlayer()).attack;

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + s + ChatColor.translateAlternateColorCodes('&',"&7▸ " + ChatColor.WHITE + "+" + (stats.getAttack(stats.attack, true, false) + attackItemStat + attackGemStat) + "%"));

                String va = "&e" + (int) stats.attack;

                if(stats.attack >= pc.getLevel()) {
                    va.replace("&e","&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7From Potions: (" + va + "&7/&6" + stats.getCap(pc.getData().getName(), "Attack") + "&7) = &b" + stats.getDodge(stats.attack, true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Item Stats: " + ChatColor.AQUA + attackItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Gems: " + ChatColor.AQUA + attackGemStat + "%")
                ));

                meta.setCustomModelData(201);
                break;
            case "Defense":
                float defenseGemStat = stats.getDefense(ItemUtils.getOverallGemStatsFromEquipment(pc.getPlayer()).defense, true, true);
                float defenseItemStat = ItemUtils.getOverallItemStatsFromEquipment(pc.getPlayer()).defense;

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + s + ChatColor.translateAlternateColorCodes('&',"&7▸ " + ChatColor.WHITE + "+" + (stats.getDefense(stats.defense, true, false) + defenseItemStat + defenseGemStat) + "%"));

                String vd = "&e" + (int) stats.defense;

                if(stats.defense >= pc.getLevel()) {
                    vd.replace("&e","&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7From Potions: (" + vd + "&7/&6" + stats.getCap(pc.getData().getName(), "Defense") + "&7) = &9" + stats.getDodge(stats.defense, true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Item Stats: " + ChatColor.BLUE + defenseItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Gems: " + ChatColor.BLUE + defenseGemStat + "%")
                ));

                meta.setCustomModelData(203);
                break;
            case "Speed":
                float speedGemStat = stats.getSpeed(ItemUtils.getOverallGemStatsFromEquipment(pc.getPlayer()).speed, true, true);
                float speedItemStat = ItemUtils.getOverallItemStatsFromEquipment(pc.getPlayer()).speed;

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + s + ChatColor.translateAlternateColorCodes('&',"&7▸ " + ChatColor.WHITE + "+" + (stats.getSpeed(stats.speed, true, false) + speedItemStat + speedGemStat) + "%"));

                String vs = "&e" + (int) stats.speed;

                if(stats.speed >= pc.getLevel()) {
                    vs.replace("&e","&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7From Potions: (" + vs + "&7/&6" + stats.getCap(pc.getData().getName(), "Speed") + "&7) = &a" + stats.getDodge(stats.speed, true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Item Stats: " + ChatColor.GREEN + speedItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Gems: " + ChatColor.GREEN + speedGemStat + "%")
                ));

                meta.setCustomModelData(202);
                break;
            case "Dodge":
                float dodgeGemStat = stats.getDodge(ItemUtils.getOverallGemStatsFromEquipment(pc.getPlayer()).dodge, true, true);
                float dodgeItemStat = ItemUtils.getOverallItemStatsFromEquipment(pc.getPlayer()).dodge;

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Evasion" + ChatColor.translateAlternateColorCodes('&',"&7▸ " + ChatColor.WHITE + "+" + (stats.getDodge(stats.dodge, true, false) + dodgeItemStat + dodgeGemStat) + "%"));

                String vdo = "&e" + (int) stats.dodge;

                if(stats.dodge >= pc.getLevel()) {
                    vdo.replace("&e","&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7From Potions: (" + vdo + "&7/&6" + stats.getCap(pc.getData().getName(), "Dodge") + "&7) = &e" + stats.getDodge(stats.dodge, true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Item Stats: " + ChatColor.YELLOW + dodgeItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&',"&7From Gems: " + ChatColor.YELLOW + dodgeGemStat + "%")
                ));

                meta.setCustomModelData(204);
                break;
        }

        this.setItemMeta(meta);
    }

}
