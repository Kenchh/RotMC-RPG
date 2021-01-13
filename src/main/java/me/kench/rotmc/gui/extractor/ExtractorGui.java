package me.kench.rotmc.gui.extractor;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.extractor.item.EssenceGuiItem;
import me.kench.rotmc.gui.extractor.item.ExtractorGuiCancelButton;
import me.kench.rotmc.gui.extractor.item.GemGuiItem;
import me.kench.rotmc.gui.extractor.item.RuneGuiItem;
import me.kench.rotmc.items.GameItem;
import me.kench.rotmc.items.GameItemStats;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExtractorGui {
    private final StaticPane cancelButton;

    public ExtractorGui() {
        StaticPane cancelButton = new StaticPane(0, 2, 1, 1);
        cancelButton.addItem(new ExtractorGuiCancelButton(), 0, 0);
        this.cancelButton = cancelButton;
    }

    public void display(Player player, GameItem item, ItemStack extractor) {
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    ChestGui gui = new ChestGui(3, "Choose an item to extract");

                    OutlinePane itemPane = new OutlinePane(1, 1, 7, 1);
                    GameItemStats gameItemStats = item.getStats();
                    gameItemStats.getGems().forEach(gem -> itemPane.addItem(new GemGuiItem(item, gem)));
                    if (gameItemStats.getRune() != null)
                        itemPane.addItem(new RuneGuiItem(item, gameItemStats.getRune()));
                    if (gameItemStats.getEssence() != null)
                        itemPane.addItem(new EssenceGuiItem(item, gameItemStats.getEssence()));

                    gui.addPane(itemPane);
                    gui.addPane(cancelButton);

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
