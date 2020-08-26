package me.kench.events;

import me.kench.gui.ExtractorGUI;
import me.kench.items.*;
import me.kench.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InvEvents implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if(!(e.getCursor().hasItemMeta())) return;
        if(!(e.getCursor().getItemMeta().hasDisplayName())) return;
        if(e.getCurrentItem() == null) return;

        boolean isGem = ItemUtils.isGem(e.getCursor().getItemMeta().getDisplayName());
        boolean isRune = ItemUtils.isRune(e.getCursor().getItemMeta().getDisplayName());
        boolean isEssence = ItemUtils.isEssence(e.getCursor().getItemMeta().getDisplayName());
        boolean isMythicDust = e.getCursor().getItemMeta().getDisplayName().contains("Mythic Dust");
        boolean isExtractor = e.getCursor().getItemMeta().getDisplayName().contains("Extractor");

        if(!isGem && !isRune && !isEssence && !isMythicDust && !isExtractor) return;

        if(e.getClick() != ClickType.LEFT) return;

        if(isExtractor) {

            if(
                    e.getCurrentItem().getType() == Material.ELYTRA ||
                            e.getCurrentItem().getType() == Material.CARROT_ON_A_STICK ||
                            e.getCurrentItem().getType().toString().toUpperCase().contains("HELMET") ||
                            e.getCurrentItem().getType().toString().toUpperCase().contains("CHESTPLATE") ||
                            e.getCurrentItem().getType().toString().toUpperCase().contains("LEGGINGS") ||
                            e.getCurrentItem().getType().toString().toUpperCase().contains("BOOTS")
            ) {

                ItemStack item = e.getCurrentItem();
                GameItem gameItem = new GameItem(item);

                if(gameItem.getStats().gems.isEmpty() && gameItem.getStats().getRune() == null && gameItem.getStats().getEssence() == null) return;

                e.setCancelled(true);

                ItemStack extractor = e.getCursor();
                p.setItemOnCursor(null);
                p.getInventory().addItem(extractor);

                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);

                p.openInventory(new ExtractorGUI(p, gameItem, extractor).getInv());

            }
            return;
        }

        if(isMythicDust) {

            MythicDustItem mythicDust = new MythicDustItem(e.getCursor());

            boolean isGem2 = ItemUtils.isGem(e.getCurrentItem().getItemMeta().getDisplayName());
            boolean isRune2 = ItemUtils.isRune(e.getCurrentItem().getItemMeta().getDisplayName());

            if(!isGem2 && !isRune2) return;

            if(isGem2) {

                e.setCancelled(true);

                GemItem gemItem = new GemItem(e.getCurrentItem());

                if(gemItem.successChance == 100) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                }

                if(gemItem.successChance + mythicDust.getLevel() * 2 >= 100) {
                    gemItem.successChance = 100;
                } else {
                    gemItem.successChance += mythicDust.getLevel() * 2;
                }

                if(mythicDust.getLevel() == 6) {
                    gemItem.successChance = 100;
                }

                gemItem.update();

                p.sendMessage(ChatColor.GREEN + "Your gem has been upgraded!");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

                ItemStack consumable = p.getItemOnCursor();
                consumable.setAmount(consumable.getAmount() - 1);
                p.getInventory().addItem(consumable);

                p.setItemOnCursor(null);

                p.closeInventory();
            }

            if(isRune2) {

                e.setCancelled(true);

                RuneItem runeItem = new RuneItem(e.getCurrentItem());

                if(runeItem.successChance == 100) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                }

                if(runeItem.successChance + mythicDust.getLevel() * 2 >= 100) {
                    runeItem.successChance = 100;
                } else {
                    runeItem.successChance += mythicDust.getLevel() * 2;
                }

                if(mythicDust.getLevel() == 6) {
                    runeItem.successChance = 100;
                }
                
                runeItem.update();

                p.sendMessage(ChatColor.GREEN + "Your rune has been upgraded!");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

                ItemStack consumable = p.getItemOnCursor();
                consumable.setAmount(consumable.getAmount() - 1);
                p.getInventory().addItem(consumable);

                p.setItemOnCursor(null);

                p.closeInventory();
            }

            return;
        }

        if(
                e.getCurrentItem().getType() == Material.ELYTRA ||
                e.getCurrentItem().getType() == Material.CARROT_ON_A_STICK ||
                e.getCurrentItem().getType().toString().toUpperCase().contains("HELMET") ||
                e.getCurrentItem().getType().toString().toUpperCase().contains("CHESTPLATE") ||
                e.getCurrentItem().getType().toString().toUpperCase().contains("LEGGINGS") ||
                e.getCurrentItem().getType().toString().toUpperCase().contains("BOOTS")
        ) {

            ItemStack item = e.getCurrentItem();
            GameItem gameItem = new GameItem(item);

            if(isGem) {

                if(gameItem.getStats().gemsockets <= 0) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    e.setCancelled(true);
                }

                GemItem gemItem = new GemItem(e.getCursor());
                new Socketing(p, gameItem, gemItem);
            }

            if(isRune) {

                if(!gameItem.getStats().hasRuneSocket) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    e.setCancelled(true);
                }

                RuneItem runeItem = new RuneItem(e.getCursor());
                new Socketing(p, gameItem, runeItem);
            }

            if(isEssence) {

                if(!gameItem.getStats().hasEssenceSocket) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    e.setCancelled(true);
                }

                EssenceItem essenceItem = new EssenceItem(e.getCursor());
                new Socketing(p, gameItem, essenceItem);
            }

            ItemStack consumable = p.getItemOnCursor();
            consumable.setAmount(consumable.getAmount() - 1);
            p.getInventory().addItem(consumable);

            p.setItemOnCursor(null);
            p.closeInventory();

        }

    }

}
