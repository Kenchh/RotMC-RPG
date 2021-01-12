package me.kench.gui.skills;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kench.RotMC;
import me.kench.gui.skills.item.StatItem;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.player.stat.Stat;
import me.kench.player.stat.Stats;
import me.kench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillsGui implements Listener {
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
                    PlayerClass selectedClass = data.getSelectedClass();
                    Stats overallStats = selectedClass.getStats().clone();
                    overallStats.incrementStats(gemStats, itemStats);

                    ChestGui gui = new ChestGui(6, "Stats");

                    StaticPane statPane = new StaticPane(2, 0, 5, 5);
                    statPane.addItem(new StatItem(selectedClass, Stat.ATTACK, overallStats), 2, 0);
                    statPane.addItem(new StatItem(selectedClass, Stat.SPEED, overallStats), 0, 2);
                    statPane.addItem(new StatItem(selectedClass, Stat.HEALTH, overallStats), 2, 2);
                    statPane.addItem(new StatItem(selectedClass, Stat.DEFENSE, overallStats), 4, 2);
                    statPane.addItem(new StatItem(selectedClass, Stat.EVASION, overallStats), 2, 4);

                    gui.addPane(background);
                    gui.addPane(statPane);

                    return gui;
                })
                .execute();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() != null && e.getView().getTitle() != "Stats") {
            return;
        }

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        e.setCancelled(true);

        if (e.getCurrentItem().getType() == Material.BARRIER) {
            e.getWhoClicked().closeInventory();
            return;
        }
    }

}
