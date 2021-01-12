package me.kench.gui.skills.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.player.stat.PlayerStats;
import me.kench.player.stat.Stat;

public class SkillsGuiStatItem extends GuiItem {
    public SkillsGuiStatItem(PlayerStats stats, Stat stat) {
        super(
                stats.getDescriptiveItemFor(stat),
                event -> event.setCancelled(true)
        );
    }
}
