package me.reykench.items;

import me.reykench.game.GameClass;
import me.reykench.items.stats.Gem;
import me.reykench.player.PlayerClass;
import me.reykench.utils.ItemUtils;
import me.reykench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
