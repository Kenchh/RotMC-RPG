package me.kench.items.stats;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class Essence {
    private final EssenceType type;

    public Essence(EssenceType type) {
        this.type = type;
    }

    public EssenceType getType() {
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
