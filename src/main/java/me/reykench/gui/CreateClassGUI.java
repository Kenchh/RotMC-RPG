package me.reykench.gui;

import me.reykench.RotMC;
import me.reykench.game.GameClass;
import me.reykench.player.PlayerClass;
import me.reykench.player.PlayerData;
import me.reykench.player.Stats;
import me.reykench.game.ClassCategory;
import me.reykench.gui.items.ClassCategoryItem;
import me.reykench.gui.items.ClassItem;
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

import java.util.UUID;

public class CreateClassGUI implements Listener {

    private PlayerData pd;
    private Inventory inv;

    public CreateClassGUI() {

    }

    public CreateClassGUI(PlayerData pd) {
        this.pd = pd;
        inv = Bukkit.createInventory(null, 9*6, "Choose your class");

        ItemStack red = new ItemStack(Material.BARRIER);
        ItemMeta redmeta = red.getItemMeta();
        redmeta.setDisplayName(ChatColor.RED + "Cancel");
        red.setItemMeta(redmeta);

        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackmeta = black.getItemMeta();
        blackmeta.setDisplayName(" ");
        black.setItemMeta(blackmeta);

        ItemStack blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta bluemeta = blue.getItemMeta();
        bluemeta.setDisplayName(" ");
        blue.setItemMeta(bluemeta);

        /* black panes */
        for(int i=0;i<=9*2-1;i++) {
            inv.setItem(i, black);
        }

        for(int i=0;i<28;i+=9) {
            inv.setItem(i+18, black);
        }

        for(int i=0;i<28;i+=9) {
            inv.setItem(i+26, black);
        }

        /* blue panes */
        for(int ii=0;ii<4;ii++) {
            for (int i = 19 + 9 * ii; i <= 25 + 9 * ii; i++) {
                inv.setItem(i, blue);
            }
        }

        inv.setItem(0+9*5, red);

        /* categories */
        inv.setItem(1, new ClassCategoryItem(ClassCategory.DAGGER));
        inv.setItem(3, new ClassCategoryItem(ClassCategory.BOW));
        inv.setItem(5, new ClassCategoryItem(ClassCategory.STAFF));
        inv.setItem(7, new ClassCategoryItem(ClassCategory.SWORD));

        /* classes */
        inv.setItem(28, new ClassItem(new GameClass("Rogue")));
        inv.setItem(30, new ClassItem(new GameClass("Huntress")));
        inv.setItem(32, new ClassItem(new GameClass("Necromancer")));
        inv.setItem(34, new ClassItem(new GameClass("Warrior")));
        inv.setItem(46, new ClassItem(new GameClass("Assassin")));
        inv.setItem(52, new ClassItem(new GameClass("Knight")));

    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView() != null && e.getView().getTitle() != "Choose your class") {
            return;
        }

        e.setCancelled(true);

        if(e.getCurrentItem() == null) {
            return;
        }

        if(e.getCurrentItem().getType() == Material.BARRIER) {
            e.getWhoClicked().closeInventory();
            return;
        }

        if(e.getCurrentItem().getType() != Material.CARROT_ON_A_STICK || e.getSlot() < 26) {
            return;
        }

        PlayerData pld = RotMC.getPlayerData((Player) e.getWhoClicked());

        switch(e.getSlot()) {
            case 28:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Rogue"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Rogue!");
                break;
            case 30:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Huntress"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Huntress!");
                break;
            case 32:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Necromancer"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Necromancer!");
                break;
            case 34:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Warrior"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Warrior!");
                break;
            case 46:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Assassin"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Assassin!");
                break;
            case 52:
                pld.classes.add(new PlayerClass(UUID.randomUUID(), pld.getPlayer(), new GameClass("Knight"), 0, 1, new Stats()));
                pld.getPlayer().sendMessage(ChatColor.GREEN + "You have selected Knight!");
                break;
        }

        pld.selectClass(pld.classes.get(pld.classes.size()-1));
        for(int i=0;i<3;i++)
            pld.getPlayer().playSound(pld.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
        e.getWhoClicked().closeInventory();

    }

}
