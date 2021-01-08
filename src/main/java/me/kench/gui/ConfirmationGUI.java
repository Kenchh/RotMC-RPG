package me.kench.gui;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmationGUI implements Listener {

    private Inventory inv;

    public ConfirmationGUI() {

    }

    public ConfirmationGUI(PlayerClass pc) {
        RotMC.getPlayerData(pc.getPlayer()).clickedClass = pc;

        inv = Bukkit.createInventory(null, 9 * 3, "Delete this class?");

        ItemStack red = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta redmeta = red.getItemMeta();
        redmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCEL");
        red.setItemMeta(redmeta);

        ItemStack green = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta greenmeta = green.getItemMeta();
        greenmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRM");
        green.setItemMeta(greenmeta);

        inv.setItem(9 + 2, red);
        inv.setItem(9 + 6, green);
    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() == null || e.getView().getTitle() != "Delete this class?") {
            return;
        }

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
            new ClassesGUI().display(p);
            return;
        }

        if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
            PlayerData pd = RotMC.getPlayerData(p);

            for (PlayerClass pc : pd.classes) {
                if (pc.getUniqueId().equals(pd.clickedClass.getUniqueId())) {
                    if (pd.currentClass.getUniqueId().equals(pc.getUniqueId())) {
                        p.sendMessage(ChatColor.RED + "You need to switch to another profile to delete your current one!");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                        p.closeInventory();
                        break;
                    }

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + pc.getFame());
                    pd.classes.remove(pc);
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 0.75F);
                    RotMC.getInstance().getSqlManager().update(p, null);
                    new ClassesGUI().display(p);
                    break;
                }
            }

        }
    }

}
