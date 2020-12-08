package me.kench.gui;

import me.kench.RotMC;
import me.kench.gui.items.CreateClassItem;
import me.kench.gui.items.GlowItem;
import me.kench.gui.items.LockedItem;
import me.kench.gui.items.PlayerClassItem;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.utils.GlowUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlowGUI implements Listener {

    private Inventory inv;

    public GlowGUI() {

    }

    public GlowGUI(Player p) {
        inv = Bukkit.createInventory(null, 6*9, "Select your glow color");

        ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackmeta = black.getItemMeta();
        blackmeta.setDisplayName(" ");
        black.setItemMeta(blackmeta);

        ItemStack pink = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta pinkmeta = pink.getItemMeta();
        pinkmeta.setDisplayName(" ");
        pink.setItemMeta(pinkmeta);

        /** DESIGN */
        for(int i=0; i<9; i++) {
            inv.setItem(i, black);
        }

        inv.setItem(9, pink);
        inv.setItem(17, pink);

        for(int i=18; i<27; i++) {
            inv.setItem(i, black);
        }

        inv.setItem(27, pink);
        inv.setItem(35, pink);

        for(int i=36; i<45; i++) {
            inv.setItem(i, black);
        }

        inv.setItem(45, pink);
        inv.setItem(53, pink);
        /** DESIGN */

        /** Glows **/
        inv.setItem(11, new GlowItem(p, "&c&k!&e[&6Bronze Glow&e]&c&k!", Material.ORANGE_DYE));
        inv.setItem(12, new GlowItem(p, "&f&k!&8[&7Silver Glow&8]&f&k", Material.GRAY_DYE));
        inv.setItem(13, new GlowItem(p, "&4&k!&6[&eGold Glow&6]&4&k!", Material.YELLOW_DYE));
        inv.setItem(14, new GlowItem(p, "&7&k!&8[&fPlatinum Glow&8]&7&k!", Material.WHITE_DYE));
        inv.setItem(15, new GlowItem(p, "&3&k!&f[&bDiamond Glow&f]&3&k!", Material.LIGHT_BLUE_DYE));

        inv.setItem(11+9*2, new GlowItem(p, "&2&k!&f[&a#5 Character Glow&f]&2&k!", Material.LIME_DYE));
        inv.setItem(12+9*2, new GlowItem(p, "&a&k!&f[&2#1 Character Glow&f]&a&k!", Material.GREEN_DYE));
        inv.setItem(14+9*2, new GlowItem(p, "&5&k!&f[&d#3 Guild Glow&f]&5&k!", Material.PINK_DYE));
        inv.setItem(15+9*2, new GlowItem(p, "&d&k!&f[&5#1 Guild Glow&f]&d&k!", Material.PURPLE_DYE));

        inv.setItem(12+9*4, new GlowItem(p, "&0&k!&8[&9Interstellar Glow&8]&0&k!", Material.LAPIS_LAZULI));
        inv.setItem(14+9*4, new GlowItem(p, "&0&k!&8[&7S&fh&7a&fd&7o&fw &7G&fl&7o&fw&8]&0&k!", Material.BLACK_DYE));

        ItemStack red = new ItemStack(Material.BARRIER);
        ItemMeta redmeta = red.getItemMeta();
        redmeta.setDisplayName(ChatColor.RED + "Exit");
        red.setItemMeta(redmeta);

        inv.setItem(45, red);

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
        
        ItemStack clickeditem = e.getCurrentItem();

        if(clickeditem == null) return;

        if(clickeditem.getType() == Material.BARRIER) {
            p.closeInventory();
            return;
        }

        if(!clickeditem.getType().name().toUpperCase().contains("DYE") && !clickeditem.getType().name().toUpperCase().contains("LAPIS")) return;

        ChatColor c = GlowUtils.getColorFromMaterial(clickeditem.getType());

        if(!GlowUtils.isPermitted(p, c)) {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            return;
        }

        if(e.getClick() == ClickType.RIGHT) {
            if(GlowUtils.getGlowColor(p) != null && c.name().toLowerCase().equalsIgnoreCase(GlowUtils.getGlowColor(p).name().toLowerCase())) {
                GlowUtils.clearGlow(p);
                p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1.0F);
            }
        } else {
            if(GlowUtils.getGlowColor(p) == null || !c.name().toLowerCase().equalsIgnoreCase(GlowUtils.getGlowColor(p).name().toLowerCase())) {
                GlowUtils.setGlow(p, c);
                p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1.5F);
            }
        }

        p.openInventory(new GlowGUI(p).getInv());

    }

}
