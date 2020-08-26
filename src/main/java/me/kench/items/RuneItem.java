package me.kench.items;

import me.kench.items.stats.Rune;
import me.kench.utils.ItemUtils;
import me.kench.utils.TextUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuneItem {

    ItemStack item;
    Rune rune;

    public int successChance = 0;

    public RuneItem(ItemStack item) {
        this.item = item;

        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();

        rune = ItemUtils.getRuneFromString(name, 0);

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();

        int line = 0;
        for(String s : lore) {
            if(s.contains("Success Chance:")) {
                if(s.contains("XXX")) {
                    createSuccessChance();
                    lore.set(line, s.replace("XXX", successChance + ""));
                } else {
                    successChance = Integer.parseInt(TextUtils.getLastWord(s, 1));
                }
            }
            line++;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void update() {

        if(item == null) return;

        ItemMeta meta = item.getItemMeta();

        if(rune != null)
            meta.setCustomModelData(rune.getType().getModeldata());

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();

        for(int i=0;i<lore.size();i++) {
            String s = lore.get(i);
            if(s.contains("Success Chance:")) {
                if(!s.contains("XXX")) {
                    String value = TextUtils.getLastWord(s, 1);
                    lore.set(i, s.replace("" + value, successChance + ""));
                }
            }
            if(s.contains("ZZZ")) {
                String runetype = TextUtils.constantToName(rune.getType().toString());
                lore.set(i, s.replace("ZZZ", runetype.substring(0, 1).toUpperCase() + runetype.substring(1).toLowerCase()));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public int getSuccessChance() {
        return successChance;
    }

    public Rune getRune() {
        return rune;
    }

    public ItemStack getItem() {
        return item;
    }

    private void createSuccessChance() {
        successChance = new Random().nextInt(100) + 1;
    }

}
