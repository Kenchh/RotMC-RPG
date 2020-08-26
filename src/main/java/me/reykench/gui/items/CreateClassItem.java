package me.reykench.gui.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateClassItem extends ItemStack {

    public CreateClassItem() {
        this.setType(Material.WHITE_STAINED_GLASS);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Create Class");

        this.setItemMeta(meta);
    }

}
