package me.kench.rotmc.gui.skills.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.player.stat.PlayerStats;
import me.kench.rotmc.player.stat.Stat;

public class SkillsGuiStatItem extends GuiItem {
    public SkillsGuiStatItem(PlayerStats stats, Stat stat) {
        super(
                stats.getDescriptiveItemFor(stat),
                event -> event.setCancelled(true)
        );
    }
}
