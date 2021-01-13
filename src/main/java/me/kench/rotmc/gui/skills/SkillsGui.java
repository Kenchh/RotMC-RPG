package me.kench.rotmc.gui.skills;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.skills.item.SkillsGuiStatItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.player.stat.PlayerStats;
import me.kench.rotmc.player.stat.Stat;
import me.kench.rotmc.player.stat.Stats;
import me.kench.rotmc.utils.ItemUtils;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SkillsGui {
    private final StaticPane background;

    public SkillsGui() {
        StaticPane background = new StaticPane(0, 0, 9, 6);
        background.fillWith(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build(), event -> event.setCancelled(true));
        this.background = background;
    }

    public void display(Player player) {
        final Stats gemStats = ItemUtils.getGemStatsFromEquipment(player);
        final Stats itemStats = ItemUtils.getItemStatsFromEquipment(player);

        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
                    if (!data.hasSelectedClass()) {
                        return null;
                    }

                    PlayerStats stats = data.getSelectedClass().getStats();

                    ChestGui gui = new ChestGui(6, "Stats");

                    StaticPane statPane = new StaticPane(2, 0, 5, 5);
                    statPane.addItem(new SkillsGuiStatItem(stats, Stat.ATTACK), 2, 0);
                    statPane.addItem(new SkillsGuiStatItem(stats, Stat.SPEED), 0, 2);
                    statPane.addItem(new SkillsGuiStatItem(stats, Stat.HEALTH), 2, 2);
                    statPane.addItem(new SkillsGuiStatItem(stats, Stat.DEFENSE), 4, 2);
                    statPane.addItem(new SkillsGuiStatItem(stats, Stat.EVASION), 2, 4);

                    gui.addPane(background);
                    gui.addPane(statPane);

                    return gui;
                })
                .syncLast(gui -> {
                    if (gui == null) {
                        Messaging.sendMessage(player, "<red>You need to select a class before you can view your stats.");
                        return;
                    }

                    gui.show(player);
                })
                .execute();
    }
}
