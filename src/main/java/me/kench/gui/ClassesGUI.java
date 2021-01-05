package me.kench.gui;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.gui.items.CreateClassItem;
import me.kench.gui.items.LockedItem;
import me.kench.gui.items.PlayerClassItem;
import me.kench.player.PlayerClass;
import me.kench.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassesGUI implements Listener {

    private Inventory inv;
    private PlayerData playerData;

    public ClassesGUI() {

    }

    public ClassesGUI(Player player) {
        playerData = RotMC.getPlayerData(player);

        inv = Bukkit.createInventory(null, 6 * 9, RankUtils.getStarColor(playerData) + "✦ " + ChatColor.WHITE + "Select your profile " + RankUtils.getOverallRank(player) + "/30");

        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackmeta = black.getItemMeta();
        blackmeta.setDisplayName(" ");
        black.setItemMeta(blackmeta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, black);
        }

        for (int i = 0; i < 9 * 4; i++) {
            inv.setItem(i + 9, new LockedItem());
        }

        for (int i = 0; i < playerData.getMaxSlots(); i++) {
            inv.setItem(i + 9, new CreateClassItem());
        }

        for (int i = 0; i < playerData.getClasses().size(); i++) {
            PlayerClass pc = playerData.getClasses().get(i);
            inv.setItem(i + 9, new PlayerClassItem(pc));
        }
    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView() == null || !e.getView().getTitle().contains("Select your profile")) {
            return;
        }

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        PlayerData pd = RotMC.getPlayerData((Player) e.getWhoClicked());

        ItemStack clickeditem = e.getCurrentItem();

        if (clickeditem == null) return;

        if (clickeditem.getType() == Material.WHITE_STAINED_GLASS) {
            for (int i = 0; i < 3; i++)
                pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
            p.openInventory(new CreateClassGUI(pd).getInv());
            return;
        }

        if (clickeditem.getType() == Material.CARROT_ON_A_STICK) {

            PlayerClass pc = pd.classes.get(e.getSlot() - 9);

            if (e.getClick() == ClickType.RIGHT) {

                if (pd.currentClass.getUuid().equals(pc.getUuid())) {
                    p.sendMessage(ChatColor.RED + "You need to switch to another profile to delete your current one!");
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                    p.closeInventory();
                    return;
                }

                p.closeInventory();
                p.openInventory(new ConfirmationGUI(pc).getInv());
                for (int i = 0; i < 3; i++)
                    pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
                return;
            }

            if (!pc.getUuid().equals(pd.getMainClass().getUuid())) {
                pd.selectClass(pc, false);
                for (int i = 0; i < 3; i++)
                    pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
                p.sendMessage(ChatColor.GREEN + "You have selected " + ChatColor.GOLD + pc.getLevel() + " " + pc.getData().getName() + ChatColor.GREEN + "!");
                p.closeInventory();
            } else {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                p.sendMessage(ChatColor.RED + "You already have this class selected!");
            }

        }

    }

}
