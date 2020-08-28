package me.kench.events;

import me.kench.RotMC;
import me.kench.gui.ExtractorGUI;
import me.kench.items.*;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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

        ItemStack item = e.getCurrentItem();
        ItemStack cursorItem = e.getCurrentItem();

        Bukkit.broadcastMessage("Inv-Event");
        if(isWearable(cursorItem)) {
            Bukkit.broadcastMessage("Is wearable");
            if(e.getSlotType() == InventoryType.SlotType.ARMOR) {
                Bukkit.broadcastMessage("Is armor-slot");
                GameItem gameItem = new GameItem(cursorItem);

                PlayerData pd = RotMC.getPlayerData(p);
                if (pd == null) return;

                PlayerClass pc = pd.getMainClass();
                if (pc == null) return;

                Bukkit.broadcastMessage("Checking level req...");
                if (gameItem.getLevel() != 0) {
                    Bukkit.broadcastMessage("Level not 0");
                    int level = gameItem.getLevel();

                    if (pc.getLevel() < level) {
                        Bukkit.broadcastMessage("Cancelled Armor-Equip");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        e.setCancelled(true);
                        return;
                    }
                }

                Bukkit.broadcastMessage("Checking class req...");
                if (gameItem.getGameClass() != null) {
                    Bukkit.broadcastMessage("Class not null");
                    String className = gameItem.getGameClass().getName();

                    if (!className.equalsIgnoreCase(pc.getData().getName())) {
                        Bukkit.broadcastMessage("Cancelled Armor-Equip");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if(isExtractor) {

            if(isGameItem(item)) {

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

            boolean isGem2 = ItemUtils.isGem(item.getItemMeta().getDisplayName());
            boolean isRune2 = ItemUtils.isRune(item.getItemMeta().getDisplayName());

            if(!isGem2 && !isRune2) return;

            if(isGem2) {

                e.setCancelled(true);

                GemItem gemItem = new GemItem(item);

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

                RuneItem runeItem = new RuneItem(item);

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

        if(isGameItem(item)) {

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

    private boolean isGameItem(ItemStack item) {
        String typename = item.getType().toString().toUpperCase();

        return item.getType() == Material.ELYTRA || item.getType() == Material.CARROT_ON_A_STICK ||
                typename.contains("HELMET") || typename.contains("CHESTPLATE") || typename.contains("LEGGINGS") || typename.contains("BOOTS");
    }

    private boolean isWearable(ItemStack item) {
        String typename = item.getType().toString().toUpperCase();

        return item.getType() == Material.ELYTRA ||
                typename.contains("HELMET") || typename.contains("CHESTPLATE") || typename.contains("LEGGINGS") || typename.contains("BOOTS");
    }

}
