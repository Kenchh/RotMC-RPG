package me.kench.rotmc.items;

import me.kench.rotmc.items.stats.Rune;
import me.kench.rotmc.utils.ItemUtils;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RuneItem {
    private final Rune rune;
    private ItemStack item;

    private int successChance = 0;

    public RuneItem(ItemStack item) {
        ItemBuilder builder = ItemBuilder.of(item);

        int line = 0;
        for (String loreLine : builder.lore()) {
            if (loreLine.contains("Success Chance:")) {
                if (loreLine.contains("XXX")) {
                    createSuccessChance();
                    builder = builder.lore(line, loreLine.replace("XXX", String.format("%d", getSuccessChance())));
                } else {
                    setSuccessChance(Integer.parseInt(TextUtils.getLastNumber(loreLine, 1)));
                }
            }

            line++;
        }

        this.rune = ItemUtils.getRuneFromString(builder.name());
        this.item = builder.build();
    }

    public void update() {
        if (item == null) {
            return;
        }

        ItemBuilder builder = ItemBuilder.of(item);

        if (rune != null) {
            builder = builder.modelData(rune.getType().getModelData());
        }

        int line = 0;
        for (String loreLine : builder.lore()) {
            if (loreLine.contains("Success Chance:")) {
                if (!loreLine.contains("XXX")) {
                    builder = builder.lore(line, loreLine.replace(TextUtils.getLastNumber(loreLine, 0), String.format("%d", getSuccessChance())));
                }
            }

            if (loreLine.contains("ZZZ")) {
                builder = builder.lore(line, loreLine.replace("ZZZ", rune.getType().getNiceConstant()));
            }
        }

        item = builder.build();
    }

    public ItemStack getItem() {
        return item;
    }

    public Rune getRune() {
        return rune;
    }

    public int getSuccessChance() {
        return successChance;
    }

    private void createSuccessChance() {
        setSuccessChance(new Random().nextInt(100) + 1);
    }

    public void setSuccessChance(int successChance) {
        this.successChance = successChance;
    }
}
