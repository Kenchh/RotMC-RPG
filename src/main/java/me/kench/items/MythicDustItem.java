package me.kench.items;

import me.kench.utils.TextUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MythicDustItem {

    private ItemStack item;
    private int level;

    public MythicDustItem(ItemStack item) {
        this.item = item;

        ItemMeta meta = item.getItemMeta();
        level = TextUtils.getNumberFromRoman(TextUtils.getLastWord(meta.getDisplayName(), 0));

    }

    public int getLevel() {
        return level;
    }

    public ItemStack getItem() {
        return item;
    }

}
