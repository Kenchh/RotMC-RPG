package me.kench.rotmc.gui.createclass;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.database.playerdata.PlayerData;
import me.kench.rotmc.gui.createclass.item.CreateClassGuiCancelButton;
import me.kench.rotmc.gui.createclass.item.CreateClassGuiClassItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.player.RpgClass;
import me.kench.rotmc.player.RpgWeapon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateClassGui {
    private final StaticPane background;
    private final StaticPane classCategories;
    private final StaticPane classBackground;
    private final StaticPane cancelButton;

    private PlayerData playerData;
    private Inventory inv;

    public CreateClassGui() {
        StaticPane background = new StaticPane(0, 0, 9, 6);
        background.fillWith(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));
        this.background = background;

        StaticPane classCategories = new StaticPane(1, 0, 7, 1);
        classCategories.addItem(RpgWeapon.DAGGER.getCreateClassGuiItem(), 0, 0);
        classCategories.addItem(RpgWeapon.BOW.getCreateClassGuiItem(), 2, 0);
        classCategories.addItem(RpgWeapon.STAFF.getCreateClassGuiItem(), 4, 0);
        classCategories.addItem(RpgWeapon.SWORD.getCreateClassGuiItem(), 6, 0);
        this.classCategories = classCategories;

        StaticPane classBackground = new StaticPane(1, 2, 7, 4);
        classBackground.fillWith(ItemBuilder.create(Material.BLUE_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));
        this.classBackground = classBackground;

        StaticPane cancelButton = new StaticPane(0, 5, 1, 1);
        cancelButton.addItem(new CreateClassGuiCancelButton(), 0, 0);
        this.cancelButton = cancelButton;
    }

    public void display(Player player) {
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    ChestGui gui = new ChestGui(6, "Choose your class");

                    StaticPane classForeground = new StaticPane(1, 3, 7, 3);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.ROGUE, data), 0, 0);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.HUNTRESS, data), 2, 0);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.NECROMANCER, data), 4, 0);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.WARRIOR, data), 6, 0);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.ASSASSIN, data), 0, 2);
                    classForeground.addItem(new CreateClassGuiClassItem(RpgClass.KNIGHT, data), 6, 2);

                    gui.addPane(background);
                    gui.addPane(classCategories);
                    gui.addPane(classBackground);
                    gui.addPane(classForeground);
                    gui.addPane(cancelButton);

                    gui.setOnClose(event -> {
                        RotMcPlugin.newChain()
                                .delay(1)
                                .sync(() -> {
                                    if (data.getSelectedClass() == null) {
                                        new CreateClassGui().display(player);
                                    }
                                })
                                .execute();
                    });

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
