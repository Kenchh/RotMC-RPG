package me.kench.items;

import me.kench.items.stats.Gem;
import me.kench.items.stats.GemType;
import me.kench.utils.ItemUtils;
import me.kench.utils.TextUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GemItem {

    ItemStack item;
    Gem gem;

    public int successChance = 0;

    public GemItem(ItemStack item) {
        this.item = item;

        ItemMeta meta = item.getItemMeta();
        String name = meta.getDisplayName();

        gem = ItemUtils.getGemFromString(name, 0);

        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        int line = 0;
        for (String s : lore) {
            if (s.contains("Success Chance:")) {
                if (s.contains("XXX")) {
                    createSuccessChance();
                    lore.set(line, s.replace("XXX", successChance + ""));
                } else {
                    successChance = Integer.parseInt(TextUtils.getLastNumber(s, 1));
                }
            }
            if (s.contains("YYY:")) {
                String gemtype = gem.getType().toString();

                String value = "+" + ItemUtils.getValueFromGemType(gem.getType(), gem.getLevel(), true);

                if (gem.getType() != GemType.HEALTH && gem.getType() != GemType.VITALITY) value += "%";

                lore.set(line, s.replace("YYY", gemtype.substring(0, 1).toUpperCase() + gemtype.substring(1).toLowerCase()).replace("ZZZ", value));
            }
            line++;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void update() {

        if (item == null) return;

        ItemMeta meta = item.getItemMeta();

        if (gem != null)
            meta.setCustomModelData(gem.getType().getModelData() + gem.getLevel());

        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if (s.contains("Success Chance:")) {
                if (!s.contains("XXX")) {
                    String value = TextUtils.getLastNumber(s, 1);
                    lore.set(i, s.replace("" + value, successChance + ""));
                }
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public int getSuccessChance() {
        return successChance;
    }

    public Gem getGem() {
        return gem;
    }

    public ItemStack getItem() {
        return item;
    }

    private void createSuccessChance() {
        successChance = new Random().nextInt(100) + 1;
    }

}
