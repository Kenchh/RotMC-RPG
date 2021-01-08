package me.kench.gui;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.RotMC;
import me.kench.gui.items.CreateClassItem;
import me.kench.gui.items.LockedItem;
import me.kench.gui.items.PlayerClassItem;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.utils.RankUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class ClassesGUI implements Listener {
    private final StaticPane background;

    public ClassesGUI() {
        StaticPane background = new StaticPane(0, 0, 9, 6);
        background.fillWith(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
        this.background = background;
    }

    public void display(Player player) {
        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
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
                    for (int i = 0; i < 36; i++) slots.insertItem(new LockedItem(), i);
                    for (int i = 0; i < data.getMaxSlots(); i++) slots.insertItem(new CreateClassItem(), i);
                    for (int i = 0; i < classes.size(); i++) slots.insertItem(new PlayerClassItem(data, classes.get(i)), i);

                    gui.addPane(background);
                    gui.addPane(slots);
                    gui.show(player);
                })
                .execute();
    }
}
