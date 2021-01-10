package me.kench.items.stats;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.ItemBuilder;
import me.kench.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class Gem {
    private final GemType type;
    private final int level;

    public Gem(GemType type, int level) {
        this.type = type;
        this.level = level;
    }

    public GemType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public GuiItem getGuiItem(Consumer<InventoryClickEvent> event) {
        return new GuiItem(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(String.format("%s%s %s", type.getPrefix(), type.getName(), TextUtils.getRomanFromNumber(level)))
                        .modelData(type.getModelData(level))
                        .build(),
                event
        );
    }
}
