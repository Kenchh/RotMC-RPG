package me.kench.gui.items;

import me.kench.player.PlayerClass;
import me.kench.player.Stats;
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
                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + s);

                String vh = "&6" + stats.health;

                if(stats.health >= pc.getLevel()) {
                    vh.replace("&6","&2");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7(" + vh + "/&2" + stats.getCap(pc.getData().getName(), "Health", pc.getLevel()) + "&7)"),
                        ChatColor.translateAlternateColorCodes('&',"&7▸ " + s + ": " + ChatColor.WHITE + "+" + stats.getHealth(stats.health))
                ));

                meta.setCustomModelData(205);
                break;
            case "Attack":
                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + s);

                String va = "&6" + stats.attack;

                if(stats.attack >= pc.getLevel()) {
                    va.replace("&6","&2");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7(" + va + "/&2" + stats.getCap(pc.getData().getName(), "Attack", pc.getLevel()) + "&7)"),
                        ChatColor.translateAlternateColorCodes('&',"&7▸ " + s + ": " + ChatColor.WHITE + "+" + stats.getAttack(stats.attack, true))
                ));

                meta.setCustomModelData(201);
                break;
            case "Defense":
                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.BLUE + s);

                String vd = "&6" + stats.defense;

                if(stats.defense >= pc.getLevel()) {
                    vd.replace("&6","&2");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7(" + vd + "/&2" + stats.getCap(pc.getData().getName(), "Defense", pc.getLevel()) + "&7)"),
                        ChatColor.translateAlternateColorCodes('&',"&7▸ " + s + ": " + ChatColor.WHITE + "+" + stats.getDefense(stats.defense, true))
                ));

                meta.setCustomModelData(203);
                break;
            case "Speed":
                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + s);

                String vs = "&6" + stats.speed;

                if(stats.speed >= pc.getLevel()) {
                    vs.replace("&6","&2");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7(" + vs + "/&2" + stats.getCap(pc.getData().getName(), "Speed", pc.getLevel()) + "&7)"),
                        ChatColor.translateAlternateColorCodes('&',"&7▸ " + s + ": " + ChatColor.WHITE + "+" + stats.getSpeed(stats.speed, true))
                ));

                meta.setCustomModelData(202);
                break;
            case "Dodge":
                meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Evasion");

                String vdo = "&6" + stats.dodge;

                if(stats.health >= pc.getLevel()) {
                    vdo.replace("&6","&2");
                }

                meta.setLore(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&',"&7(" + vdo + "/&2" + stats.getCap(pc.getData().getName(), "Dodge", pc.getLevel()) + "&7)"),
                        ChatColor.translateAlternateColorCodes('&',"&7▸ " + s + ": " + ChatColor.WHITE + "+" + stats.getDodge(stats.dodge, true))
                ));

                meta.setCustomModelData(204);
                break;
        }

        this.setItemMeta(meta);
    }

}
