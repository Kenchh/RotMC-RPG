package me.kench.gui.items;

import me.kench.game.ClassCategory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassCategoryItem extends ItemStack {

    public ClassCategoryItem(ClassCategory category) {
        this.setType(Material.CARROT_ON_A_STICK);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(ClassCategory.getName(category));
        meta.setCustomModelData(ClassCategory.getCustomModelData(category));

        this.setItemMeta(meta);
    }

}
