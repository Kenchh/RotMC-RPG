package me.kench.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder {
    private final ItemStack stack;
    private final ItemMeta meta;

    private ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    private ItemBuilder(ItemStack stack) {
        this.stack = stack;
        meta = stack.getItemMeta();
    }

    public static ItemBuilder create(Material type) {
        return new ItemBuilder(type);
    }

    public static ItemBuilder of(ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(String... lines) {
        meta.setLore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder modelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }

    public ItemBuilder applyMeta(Consumer<ItemMeta> metaConsumer) {
        metaConsumer.accept(meta);
        return this;
    }

    public ItemStack build() {
        stack.setItemMeta(meta);
        return stack;
    }
}
