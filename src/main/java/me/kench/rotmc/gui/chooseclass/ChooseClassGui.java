package me.kench.rotmc.gui.chooseclass;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.chooseclass.item.ClassSlotUnavailableItem;
import me.kench.rotmc.gui.chooseclass.item.CreateNewClassItem;
import me.kench.rotmc.gui.chooseclass.item.PlayerClassItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.utils.RankUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class ChooseClassGui {
    private final StaticPane background;

    public ChooseClassGui() {
        StaticPane background = new StaticPane(0, 0, 9, 6);
        background.fillWith(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));
        this.background = background;
    }

    public void display(Player player) {
        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    List<PlayerClass> classes = data.getClasses();

                    ChestGui gui = new ChestGui(
                            6,
                            String.format(
                                    "%s✦ %sSelect your profile %d/30",
                                    RankUtils.getStarColor(data),
                                    ChatColor.WHITE,
                                    data.getOverallRank()
                            )
                    );

                    OutlinePane slots = new OutlinePane(0, 1, 9, 4);
                    for (int i = 0; i < 36; i++) slots.insertItem(new ClassSlotUnavailableItem(), i);
                    for (int i = 0; i < data.getMaxSlots(); i++) slots.insertItem(new CreateNewClassItem(), i);
                    for (int i = 0; i < classes.size(); i++)
                        slots.insertItem(new PlayerClassItem(data, classes.get(i)), i);

                    gui.addPane(background);
                    gui.addPane(slots);

                    return gui;
                })
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
