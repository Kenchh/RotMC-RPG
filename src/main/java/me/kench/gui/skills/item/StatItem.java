package me.kench.gui.skills.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.player.PlayerClass;
import me.kench.player.stat.Stat;
import me.kench.player.stat.Stats;
import me.kench.utils.ItemUtils;
import me.kench.utils.StatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import scala.Tuple3;

import java.util.Arrays;

public class StatItem extends GuiItem {
    public StatItem(PlayerClass playerClass, Stat stat, Tuple3<Stats, Stats, Stats> stats) {
        super(

        );
        Stats stats = pc.getStats();
        this.setType(Material.CARROT_ON_A_STICK);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();

        switch (s) {
            case "Health":
                float healthGemStat = StatUtils.getHealth(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.HEALTH), true, true);
                float healthItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.HEALTH);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + s + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getHealth(stats.getStat(Stat.HEALTH), true, false) + healthItemStat + healthGemStat)));

                String vh = "&e" + (int) stats.getStat(Stat.HEALTH);

                if (stats.getStat(Stat.HEALTH) >= pc.getLevel()) {
                    vh.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + vh + "&7/&6" + stats.getCap(pc.getRpgClass(), Stat.HEALTH) + "&7) = &c" + StatUtils.getHealth(stats.getStat(Stat.HEALTH), true, false)),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.RED + healthItemStat),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.RED + healthGemStat)
                ));

                meta.setCustomModelData(205);
                break;
            case "Attack":
                float attackGemStat = StatUtils.getAttack(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.ATTACK), true, true);
                float attackItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.ATTACK);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + s + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getAttack(stats.getStat(Stat.ATTACK), true, false) + attackItemStat + attackGemStat) + "%"));

                String va = "&e" + (int) stats.getStat(Stat.ATTACK);

                if (stats.getStat(Stat.ATTACK) >= pc.getLevel()) {
                    va.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + va + "&7/&6" + stats.getCap(pc.getRpgClass(), Stat.ATTACK) + "&7) = &b" + StatUtils.getAttack(stats.getStat(Stat.ATTACK), true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.AQUA + attackItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.AQUA + attackGemStat + "%")
                ));

                meta.setCustomModelData(201);
                break;
            case "Defense":
                float defenseGemStat = StatUtils.getDefense(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.DEFENSE), true, true);
                float defenseItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.DEFENSE);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + s + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getDefense(stats.getStat(Stat.DEFENSE), true, false) + defenseItemStat + defenseGemStat) + "%"));

                String vd = "&e" + (int) stats.getStat(Stat.DEFENSE);

                if (stats.getStat(Stat.DEFENSE) >= pc.getLevel()) {
                    vd.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + vd + "&7/&6" + stats.getCap(pc.getRpgClass(), Stat.DEFENSE) + "&7) = &9" + StatUtils.getDefense(stats.getStat(Stat.DEFENSE), true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.BLUE + defenseItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.BLUE + defenseGemStat + "%")
                ));

                meta.setCustomModelData(203);
                break;
            case "Speed":
                float speedGemStat = StatUtils.getSpeed(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.SPEED), true, true);
                float speedItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.SPEED);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + s + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getSpeed(stats.getStat(Stat.SPEED), true, false) + speedItemStat + speedGemStat) + "%"));

                String vs = "&e" + (int) stats.getStat(Stat.SPEED);

                if (stats.getStat(Stat.SPEED) >= pc.getLevel()) {
                    vs.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + vs + "&7/&6" + stats.getCap(pc.getRpgClass(), Stat.SPEED) + "&7) = &a" + StatUtils.getSpeed(stats.getStat(Stat.SPEED), true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.GREEN + speedItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.GREEN + speedGemStat + "%")
                ));

                meta.setCustomModelData(202);
                break;
            case "Dodge":
                float dodgeGemStat = StatUtils.getDodge(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.EVASION), true, true);
                float dodgeItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.EVASION);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Evasion" + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getDodge(stats.getStat(Stat.EVASION), true, false) + dodgeItemStat + dodgeGemStat) + "%"));

                String vdo = "&e" + (int) stats.getStat(Stat.EVASION);

                if (stats.getStat(Stat.EVASION) >= pc.getLevel()) {
                    vdo.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + vdo + "&7/&6" + stats.getCap(pc.getRpgClass(), Stat.EVASION) + "&7) = &e" + StatUtils.getDodge(stats.getStat(Stat.EVASION), true, false) + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.YELLOW + dodgeItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.YELLOW + dodgeGemStat + "%")
                ));

                meta.setCustomModelData(204);
                break;
            case "Vitality":
                float vitalityGemStat = StatUtils.getDodge(ItemUtils.getGemStatsFromEquipment(pc.getPlayer()).getStat(Stat.VITALITY), true, true);
                float vitalityItemStat = ItemUtils.getItemStatsFromEquipment(pc.getPlayer()).getStat(Stat.VITALITY);

                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Vitality" + ChatColor.translateAlternateColorCodes('&', "&7▸ " + ChatColor.WHITE + "+" + (StatUtils.getDodge(stats.getStat(Stat.VITALITY), true, false) + vitalityItemStat + vitalityGemStat)));

                String vv = "&e" + (int) stats.getStat(Stat.VITALITY);

                if (stats.getStat(Stat.VITALITY) >= pc.getLevel()) {
                    vv.replace("&e", "&6");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7From Potions: (" + vv + "&7/&d" + stats.getCap(pc.getRpgClass(), Stat.VITALITY) + "&7) = &d" + StatUtils.getDodge(stats.getStat(Stat.VITALITY), true, false)),
                        ChatColor.translateAlternateColorCodes('&', "&7From Item Stats: " + ChatColor.LIGHT_PURPLE + vitalityItemStat + "%"),
                        ChatColor.translateAlternateColorCodes('&', "&7From Gems: " + ChatColor.LIGHT_PURPLE + vitalityGemStat + "%")
                ));

                meta.setCustomModelData(206);
                break;
        }

        this.setItemMeta(meta);
    }

}
