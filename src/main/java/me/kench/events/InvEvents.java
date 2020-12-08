package me.kench.events;

import me.kench.RotMC;
import me.kench.game.GameClass;
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

        if(e.getClick() == ClickType.SHIFT_RIGHT || e.getClick() == ClickType.SHIFT_LEFT) {
            if(e.getCurrentItem() != null)
                checkIfArmorPutOn(e, p, e.getCurrentItem(), true);
        } else {
            if(e.getCursor() != null)
                checkIfArmorPutOn(e, p, e.getCursor(), false);
        }

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

        if(!(item.hasItemMeta())) return;

        if(isExtractor) {
            extract(e, p, item);
            return;
        }

        if(isMythicDust) {
            mythicDust(e, p, item);
            return;
        }

        checkForSocketing(e, p, item, isGem, isRune, isEssence);

    }

    private void extract(InventoryClickEvent e, Player p, ItemStack item) {
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

    private void mythicDust(InventoryClickEvent e, Player p, ItemStack item) {

        MythicDustItem mythicDust = new MythicDustItem(e.getCursor());

        boolean isGem = ItemUtils.isGem(item.getItemMeta().getDisplayName());
        boolean isRune = ItemUtils.isRune(item.getItemMeta().getDisplayName());

        if(!isGem && !isRune) return;

        if(isGem) {

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

        if(isRune) {

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

    private void checkForSocketing(InventoryClickEvent e, Player p, ItemStack item, boolean isGem, boolean isRune, boolean isEssence) {

        if(isGameItem(item)) {

            GameItem gameItem = new GameItem(item);

            if(gameItem.getStats().gemsockets == 0 && gameItem.getStats().hasRuneSocket == false && gameItem.getStats().hasEssenceSocket == false) return;

            if(isGem) {

                if(gameItem.getStats().gemsockets <= 0) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                } else {
                    e.setCancelled(true);
                }

                if(!e.getCursor().getItemMeta().hasLore()) return;

                for(String s : e.getCursor().getItemMeta().getLore()) {
                    if(s.contains("XXX") || s.contains("YYY") || s.contains("ZZZ")) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        p.sendMessage(ChatColor.RED + "You need to right click the gem in your main hand to reveal it's success chance before socketing!");
                        return;
                    }
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

                if(!e.getCursor().getItemMeta().hasLore()) return;

                for(String s : e.getCursor().getItemMeta().getLore()) {
                    if(s.contains("XXX") || s.contains("YYY") || s.contains("ZZZ")) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        p.sendMessage(ChatColor.RED + "You need to right click the rune in your main hand to reveal it's success chance before socketing!");
                        return;
                    }
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

    private void checkIfArmorPutOn(InventoryClickEvent e, Player p, ItemStack item, boolean shift) {

        if(!e.getView().getTitle().contains("Crafting")) return;

        if(isWearable(item)) {
            if(!shift) {
                if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                    GameItem gameItem = new GameItem(item);

                    PlayerData pd = RotMC.getPlayerData(p);
                    if (pd == null) e.setCancelled(true);

                    PlayerClass pc = pd.getMainClass();
                    if (pc == null) e.setCancelled(true);

                    if (gameItem.getLevel() != 0) {
                        int level = gameItem.getLevel();

                        if (pc.getLevel() < level) {
                            p.sendMessage(ChatColor.RED + "You need to be level " + level + " to use this item!");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                            e.setCancelled(true);
                            return;
                        }
                    }

                    boolean foundClass = false;
                    String currentclass = pc.getData().getName();
                    if (gameItem.getGameClasses().isEmpty() == false) {
                        for(GameClass gameClass : gameItem.getGameClasses()) {
                            String className = gameClass.getName();

                            if (className.equalsIgnoreCase(currentclass)) {
                                foundClass = true;
                                break;
                            }
                        }
                    }

                    if(!foundClass) {
                        p.sendMessage(ChatColor.RED + "That item is not suitable for " + currentclass + "!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        e.setCancelled(true);
                        return;
                    }
                }
            } else {
                if (e.getSlotType() != InventoryType.SlotType.ARMOR) {
                    GameItem gameItem = new GameItem(item);

                    PlayerData pd = RotMC.getPlayerData(p);
                    if (pd == null) e.setCancelled(true);

                    PlayerClass pc = pd.getMainClass();
                    if (pc == null) e.setCancelled(true);

                    if (gameItem.getLevel() != 0) {
                        int level = gameItem.getLevel();

                        if (pc.getLevel() < level) {
                            p.sendMessage(ChatColor.RED + "You need to be level " + level + " to use this item!");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                            e.setCancelled(true);
                            return;
                        }
                    }

                    boolean foundClass = false;
                    String currentclass = pc.getData().getName();
                    if (gameItem.getGameClasses().isEmpty() == false) {
                        for(GameClass gameClass : gameItem.getGameClasses()) {
                            String className = gameClass.getName();

                            if (className.equalsIgnoreCase(currentclass)) {
                                foundClass = true;
                                break;
                            }
                        }
                    }

                    if(!foundClass) {
                        p.sendMessage(ChatColor.RED + "That item is not suitable for " + currentclass + "!");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        e.setCancelled(true);
                        return;
                    }

                }
            }
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
