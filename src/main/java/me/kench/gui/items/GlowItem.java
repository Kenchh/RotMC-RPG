package me.kench.gui.items;

import me.kench.utils.GlowUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GlowItem extends ItemStack {

    public GlowItem(Player p, String displayname, Material material) {
        this.setType(material);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));

        List<String> lore = Arrays.asList("", "", "", "");

        ChatColor c = GlowUtils.getColorFromMaterial(material);

        if(GlowUtils.isPermitted(p, c)) {
            lore.set(0, ChatColor.GREEN + ChatColor.BOLD.toString() + "UNLOCKED");
        } else {
            lore.set(0, ChatColor.RED + ChatColor.BOLD.toString() + "LOCKED");
        }

        lore.set(1, ChatColor.DARK_GRAY + "----------");
        lore.set(2, ChatColor.YELLOW + "Left-Click to select glow.");
        lore.set(3, ChatColor.YELLOW + "Right-Click to " + ChatColor.RED + "remove" + ChatColor.YELLOW + " glow.");

        if(GlowUtils.getGlowColor(p) != null) {
            if (GlowUtils.getGlowColor(p).toString().equalsIgnoreCase(GlowUtils.getColorFromMaterial(material).toString())) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            }
        }

        meta.setLore(lore);
        this.setItemMeta(meta);
    }

}
