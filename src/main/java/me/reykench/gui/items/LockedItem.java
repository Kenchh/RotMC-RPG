package me.reykench.gui.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LockedItem extends ItemStack {

    public LockedItem() {
        this.setType(Material.IRON_BARS);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Locked");

        this.setItemMeta(meta);
    }

}
