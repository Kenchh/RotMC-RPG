package me.kench.gui;

import me.kench.RotMC;
import me.kench.gui.items.CreateClassItem;
import me.kench.gui.items.LockedItem;
import me.kench.gui.items.PlayerClassItem;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
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

public class GlowGUI implements Listener {

    private Inventory inv;

    public GlowGUI() {

    }

    public GlowGUI(Player p) {
        inv = Bukkit.createInventory(null, 6*9, "Select your glow color");

        ItemStack toggle = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta togglemeta = toggle.getItemMeta();

        /*
        boolean hasGlow = false;
        if(p.getScoreboard() != null) {
            for(Team t : Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {

            }
        }
        */

        togglemeta.setDisplayName(ChatColor.YELLOW + "Glow ");
        toggle.setItemMeta(togglemeta);

    }

    public Inventory getInv() {
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView() != null && e.getView().getTitle() != "Select your glow color") {
            return;
        }

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        PlayerData pld = RotMC.getPlayerData((Player) e.getWhoClicked());
        
        ItemStack clickeditem = e.getCurrentItem();

        if(clickeditem == null) return;

        if(clickeditem.getType() == Material.WHITE_STAINED_GLASS) {
            for(int i=0;i<3;i++)
                pld.getPlayer().playSound(pld.getPlayer().getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
            p.openInventory(new CreateClassGUI(RotMC.getPlayerData(p)).getInv());
            return;
        }

        if(clickeditem.getType() == Material.CARROT_ON_A_STICK) {

            PlayerClass pc = pld.classes.get(e.getSlot()-9);

            if(e.getClick() == ClickType.RIGHT) {

                if(pld.currentClass.getUuid().equals(pc.getUuid())) {
                    p.sendMessage(ChatColor.RED + "You need to switch to another profile to delete your current one!");
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                    return;
                }

                p.openInventory(new ConfirmationGUI(pc).getInv());
                for(int i=0;i<3;i++)
                    pld.getPlayer().playSound(pld.getPlayer().getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
                return;
            }

            if(!pc.getUuid().equals(pld.getMainClass().getUuid())) {
                pld.selectClass(pc);
                for(int i=0;i<3;i++)
                    pld.getPlayer().playSound(pld.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
                p.sendMessage(ChatColor.GREEN + "You have selected " + ChatColor.GOLD + pc.getLevel() + " " + pc.getData().getName() + ChatColor.GREEN + "!");
                p.closeInventory();
            } else {
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                p.sendMessage(ChatColor.RED + "You already have this class selected!");
            }

        }

    }

}
