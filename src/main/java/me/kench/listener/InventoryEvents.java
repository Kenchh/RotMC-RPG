package me.kench.listener;

import me.kench.gui.extractor.ExtractorGui;
import me.kench.items.*;
import me.kench.items.stats.Gem;
import me.kench.utils.EventUtils;
import me.kench.utils.ItemUtils;
import me.kench.utils.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) {
            if (event.getCurrentItem() != null) {
                checkIfArmorPutOn(event, player, event.getCurrentItem(), true);
            }
        } else {
            if (event.getCursor() != null) {
                checkIfArmorPutOn(event, player, event.getCursor(), false);
            }
        }

        if (!(event.getCursor().hasItemMeta())) {
            return;
        }

        if (!(event.getCursor().getItemMeta().hasDisplayName())) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        boolean isGem = ItemUtils.isGem(event.getCursor().getItemMeta().getDisplayName());
        boolean isRune = ItemUtils.isRune(event.getCursor().getItemMeta().getDisplayName());
        boolean isEssence = ItemUtils.isEssence(event.getCursor().getItemMeta().getDisplayName());
        boolean isMythicDust = event.getCursor().getItemMeta().getDisplayName().contains("Mythic Dust");
        boolean isExtractor = event.getCursor().getItemMeta().getDisplayName().contains("Extractor");

        if (!isGem && !isRune && !isEssence && !isMythicDust && !isExtractor) {
            return;
        }

        if (event.getClick() != ClickType.LEFT) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (!(item.hasItemMeta())) {
            return;
        }

        if (isExtractor) {
            extract(event, player, item);
            return;
        }

        if (isMythicDust) {
            mythicDust(event, player, item);
            return;
        }

        checkForSocketing(event, player, item, isGem, isRune, isEssence);
    }

    private void extract(InventoryClickEvent event, Player player, ItemStack item) {
        if (!event.getView().getTitle().equalsIgnoreCase("Crafting") || event.getInventory().getHolder() != player) {
            player.sendMessage(ChatColor.RED + "You can only extract from your inventory!");
            return;
        }

        if (EventUtils.isGameItem(item)) {
            GameItem gameItem = new GameItem(item);
            if (gameItem.getStats().getGems().isEmpty() && gameItem.getStats().getRune() == null && gameItem.getStats().getEssence() == null) {
                return;
            }

            event.setCancelled(true);

            ItemStack extractor = event.getCursor();
            player.setItemOnCursor(null);
            player.getInventory().addItem(extractor);

            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);

            new ExtractorGui().display(player, gameItem, extractor);
        }
    }

    private void mythicDust(InventoryClickEvent event, Player player, ItemStack item) {
        ItemBuilder dust = ItemBuilder.of(item);
        if (dust == null) return;

        boolean isGem = ItemUtils.isGem(dust.name());
        boolean isRune = ItemUtils.isRune(dust.name());

        if (!isGem && !isRune) {
            return;
        }

        MythicDustItem mythicDust = new MythicDustItem(dust.build());

        event.setCancelled(true);

        if (isGem) {
            GemItem gemItem = new GemItem(item);

            if (gemItem.getSuccessChance() == 100) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                return;
            }

            if (gemItem.getSuccessChance() + mythicDust.getLevel() * 2 >= 100) {
                gemItem.setSuccessChance(100);
            } else {
                gemItem.setSuccessChance(gemItem.getSuccessChance() + mythicDust.getLevel() * 2);
            }

            if (mythicDust.getLevel() == 6) {
                gemItem.setSuccessChance(100);
            }

            gemItem.update();

            player.sendMessage(ChatColor.GREEN + "Your gem has been upgraded!");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

            ItemStack consumable = player.getItemOnCursor();
            consumable.setAmount(consumable.getAmount() - 1);
            player.getInventory().addItem(consumable);

            player.setItemOnCursor(null);

            player.closeInventory();
        }

        if (isRune) {
            RuneItem runeItem = new RuneItem(item);

            if (runeItem.getSuccessChance() == 100) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                return;
            }

            if (runeItem.getSuccessChance() + mythicDust.getLevel() * 2 >= 100) {
                runeItem.setSuccessChance(100);
            } else {
                runeItem.setSuccessChance(runeItem.getSuccessChance() + mythicDust.getLevel() * 2);
            }

            if (mythicDust.getLevel() == 6) {
                runeItem.setSuccessChance(100);
            }

            runeItem.update();

            player.sendMessage(ChatColor.GREEN + "Your rune has been upgraded!");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

            ItemStack consumable = player.getItemOnCursor();
            consumable.setAmount(consumable.getAmount() - 1);
            player.getInventory().addItem(consumable);

            player.setItemOnCursor(null);

            player.closeInventory();
        }
    }

    private void checkForSocketing(InventoryClickEvent event, Player player, ItemStack stack, boolean isGem, boolean isRune, boolean isEssence) {
        if (EventUtils.isGameItem(stack)) {
            GameItem gameItem = new GameItem(stack);

            if (gameItem.getStats().getGemSockets() == 0 && !gameItem.getStats().hasRuneSocket() && !gameItem.getStats().hasEssenceSocket()) {
                return;
            }

            if (isGem) {
                if (gameItem.getStats().getGemSockets() == 0) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    event.setCancelled(true);
                }

                ItemBuilder cursor = ItemBuilder.of(event.getCursor());
                if (cursor == null || cursor.lore().isEmpty()) return;

                for (String loreLine : cursor.lore()) {
                    if (loreLine.contains("XXX") || loreLine.contains("YYY") || loreLine.contains("ZZZ")) {
                        Messaging.sendMessage(player, "<red>You need to right-click the gem in your main hand to reveal its success chance before socketing!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        return;
                    }
                }

                GemItem gemItem = new GemItem(cursor.build());

                for (Gem gem : gameItem.getStats().getGems()) {
                    if (gem.getType().equals(gemItem.getGem().getType())) {
                        Messaging.sendMessage(player, "<red>You can't use two gems of the same type on one item!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        return;
                    }
                }

                new Socketing(player, gameItem, gemItem);
            }

            if (isRune) {
                if (!gameItem.getStats().hasRuneSocket()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    event.setCancelled(true);
                }

                ItemBuilder cursor = ItemBuilder.of(event.getCursor());
                if (cursor == null || cursor.lore().isEmpty()) return;

                for (String loreLine : cursor.lore()) {
                    if (loreLine.contains("XXX") || loreLine.contains("YYY") || loreLine.contains("ZZZ")) {
                        Messaging.sendMessage(player, "<red>You need to right-click the rune in your main hand to reveal its success chance before socketing!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        return;
                    }
                }

                RuneItem runeItem = new RuneItem(cursor.build());
                new Socketing(player, gameItem, runeItem);
            }

            if (isEssence) {
                if (!gameItem.getStats().hasEssenceSocket()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    event.setCancelled(true);
                }

                ItemBuilder cursor = ItemBuilder.of(event.getCursor());
                if (cursor == null) return;

                EssenceItem essenceItem = new EssenceItem(cursor.build());
                new Socketing(player, gameItem, essenceItem);
            }

            ItemStack consumable = player.getItemOnCursor();
            consumable.setAmount(consumable.getAmount() - 1);
            player.getInventory().addItem(consumable);

            player.setItemOnCursor(null);
            player.closeInventory();
        }
    }

    private void checkIfArmorPutOn(InventoryClickEvent event, Player player, ItemStack stack, boolean shift) {
        if (!event.getView().getTitle().contains("Crafting")) return;

        if (EventUtils.isWearable(stack)) {
            if (!shift) {
                if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    EventUtils.checkLevelAndClass(event, player, stack);
                }
            } else {
                if (event.getSlotType() != InventoryType.SlotType.ARMOR) {
                    EventUtils.checkLevelAndClass(event, player, stack);
                }
            }
        }
    }
}
