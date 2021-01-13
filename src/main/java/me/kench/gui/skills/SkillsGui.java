package me.kench.gui.skills;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.RotMC;
import me.kench.gui.skills.item.SkillsGuiStatItem;
import me.kench.items.ItemBuilder;
import me.kench.player.stat.PlayerStats;
import me.kench.player.stat.Stat;
import me.kench.player.stat.Stats;
import me.kench.utils.ItemUtils;
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

        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> {
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
                .syncLast(gui -> gui.show(player))
                .execute();
    }
}
