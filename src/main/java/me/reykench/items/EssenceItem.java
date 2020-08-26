package me.reykench.items;

import me.reykench.items.stats.Essence;
import me.reykench.items.stats.Rune;
import me.reykench.utils.ItemUtils;
import me.reykench.utils.TextUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EssenceItem {

    ItemStack item;
    Essence essence;

    public EssenceItem(ItemStack item) {
        this.item = item;

        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();

        essence = ItemUtils.getEssenceFromString(name);

        item.setItemMeta(meta);
    }

    public void update() {
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();

        if(essence != null)
            meta.setCustomModelData(essence.getType().getModeldata());

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();

        for(int i=0;i<lore.size();i++) {
            String s = lore.get(i);
            if(s.contains("ZZZ")) {
                String essencetype = essence.getType().toString();
                lore.set(i, s.replace("ZZZ", essencetype.substring(0, 1).toUpperCase() + essencetype.substring(1).toLowerCase()));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    
    public Essence getEssence() {
        return essence;
    }

    public ItemStack getItem() {
        return item;
    }

}
