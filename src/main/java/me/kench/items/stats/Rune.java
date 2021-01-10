package me.kench.items.stats;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class Rune {
    private final RuneType type;

    public Rune(RuneType type) {
        this.type = type;
    }

    public RuneType getType() {
        return type;
    }

    public GuiItem getGuiItem(Consumer<InventoryClickEvent> event) {
        return new GuiItem(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(String.format("%s%s", type.getPrefix(), type.getName()))
                        .modelData(type.getModelData())
                        .build(),
                event
        );
    }
}
