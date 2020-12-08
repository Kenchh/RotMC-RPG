package me.kench.gui;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.gui.items.StatItem;
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

public class SkillsGUI implements Listener {

    private Inventory inv;

    public SkillsGUI() {

    }

    public SkillsGUI(Player p) {
        inv = Bukkit.createInventory(null, 5*9, "Stats");

        PlayerClass pc = RotMC.getPlayerData(p).getMainClass();

        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackmeta = black.getItemMeta();
        blackmeta.setDisplayName(" ");
        black.setItemMeta(blackmeta);

        ItemStack red = new ItemStack(Material.BARRIER);
        ItemMeta redmeta = red.getItemMeta();
        redmeta.setDisplayName(ChatColor.RED + "Exit");
        red.setItemMeta(redmeta);

        /* black panes */
        for(int i=0;i<inv.getSize();i++) {
            inv.setItem(i, black);
        }

        inv.setItem(4, new StatItem(pc, "Attack"));
        inv.setItem(6+9*2, new StatItem(pc, "Defense"));
        inv.setItem(4+9*2, new StatItem(pc, "Health"));
        inv.setItem(2+9*2, new StatItem(pc, "Speed"));
        inv.setItem(4+9*4, new StatItem(pc, "Dodge"));

    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView() != null && e.getView().getTitle() != "Stats") {
            return;
        }

        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        e.setCancelled(true);

        if(e.getCurrentItem().getType() == Material.BARRIER) {
            e.getWhoClicked().closeInventory();
            return;
        }
    }

}
