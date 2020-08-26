package me.kench.gui.items;

import me.kench.game.GameClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ClassItem extends ItemStack {

    public ClassItem(GameClass GameClass) {
        this.setType(Material.CARROT_ON_A_STICK);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + GameClass.getName());
        meta.setCustomModelData(0);

        switch (GameClass.getName()) {
            case "Knight":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "A slow hitting sword",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and shield user who",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "relies on their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "physical strength to",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "hinder opponents."
                ));
                meta.setCustomModelData(151);
                break;
            case "Necromancer":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Said to manipulate",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "the souls life force,",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "necromancer's use",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "magic staffs and",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "their enchanted skull",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "to drain their enemies",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "health."
                ));
                meta.setCustomModelData(101);
                break;
            case "Warrior":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "A swift swordsman in",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "which their speed",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and hasteful hands",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "allow them to strike",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "unscathed in any situation."
                ));
                meta.setCustomModelData(141);
                break;
            case "Huntress":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "These are precisive",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "archers who lay traps",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "to catch foes off",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "guard with large",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "amounts of instant damage."
                ));
                meta.setCustomModelData(111);
                break;
            case "Assassin":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Lethal dagger users",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "who induce their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "opponents with poison",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "and finish them off with",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "their nimble attack speed."
                ));
                meta.setCustomModelData(121);
                break;
            case "Rogue":
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "Dagger wielding",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "mercenaries, they gain",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "the advantage in combat",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "with their specialised",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "cloaks which grant",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "invisibility and their",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "daggers which",
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "grant fast attack speed."
                ));
                meta.setCustomModelData(131);
                break;
        }

        this.setItemMeta(meta);
    }

}
