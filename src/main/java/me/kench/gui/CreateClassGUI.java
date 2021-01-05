package me.kench.gui;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.game.ClassCategory;
import me.kench.game.GameClass;
import me.kench.gui.items.ClassCategoryItem;
import me.kench.gui.items.ClassItem;
import me.kench.player.PlayerClass;
import me.kench.player.Stats;
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
    private PlayerData playerData;
    private Inventory inv;

    public CreateClassGUI() {

    }

    public CreateClassGUI(PlayerData playerData) {
        this.playerData = playerData;
        inv = Bukkit.createInventory(null, 9 * 6, "Choose your class");

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
        for (int i = 0; i <= 9 * 2 - 1; i++) {
            inv.setItem(i, black);
        }

        for (int i = 0; i < 28; i += 9) {
            inv.setItem(i + 18, black);
        }

        for (int i = 0; i < 28; i += 9) {
            inv.setItem(i + 26, black);
        }

        /* blue panes */
        for (int ii = 0; ii < 4; ii++) {
            for (int i = 19 + 9 * ii; i <= 25 + 9 * ii; i++) {
                inv.setItem(i, blue);
            }
        }

        inv.setItem(0 + 9 * 5, red);

        /* categories */
        inv.setItem(1, new ClassCategoryItem(ClassCategory.DAGGER));
        inv.setItem(3, new ClassCategoryItem(ClassCategory.BOW));
        inv.setItem(5, new ClassCategoryItem(ClassCategory.STAFF));
        inv.setItem(7, new ClassCategoryItem(ClassCategory.SWORD));

        /* classes */
        inv.setItem(28, new ClassItem(playerData.getPlayer(), new GameClass("Rogue")));
        inv.setItem(30, new ClassItem(playerData.getPlayer(), new GameClass("Huntress")));
        inv.setItem(32, new ClassItem(playerData.getPlayer(), new GameClass("Necromancer")));
        inv.setItem(34, new ClassItem(playerData.getPlayer(), new GameClass("Warrior")));
        inv.setItem(46, new ClassItem(playerData.getPlayer(), new GameClass("Assassin")));
        inv.setItem(52, new ClassItem(playerData.getPlayer(), new GameClass("Knight")));

    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Choose your class")) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getType() == Material.BARRIER) {
            event.getWhoClicked().closeInventory();
            return;
        }

        if (event.getCurrentItem().getType() != Material.CARROT_ON_A_STICK || event.getSlot() < 26) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(event.getWhoClicked().getUniqueId())
                .syncLast(data -> {
                    PlayerClass selected = null;

                    switch (event.getSlot()) {
                        case 28:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Rogue"), 0, 1, new Stats());
                            break;
                        case 30:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Huntress"), 0, 1, new Stats());
                            break;
                        case 32:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Necromancer"), 0, 1, new Stats());
                            break;
                        case 34:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Warrior"), 0, 1, new Stats());
                            break;
                        case 46:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Assassin"), 0, 1, new Stats());
                            break;
                        case 52:
                            selected = new PlayerClass(UUID.randomUUID(), player, new GameClass("Knight"), 0, 1, new Stats());
                            break;
                    }

                    if (selected != null) {
                        player.sendMessage(String.format(ChatColor.GREEN + "You have selected %s!", selected.getData().getName()));

                        data.changeSelectedClass(selected.getUuid(), true);

                        for (int i = 0; i < 3; i++) {
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "I'm sorry, but an error occurred while selecting your new class. Please report this.");
                    }

                    event.getWhoClicked().closeInventory();
                })
                .execute();
    }
}
