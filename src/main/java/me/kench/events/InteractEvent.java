package me.kench.events;

import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.items.EssenceItem;
import me.kench.items.GameItem;
import me.kench.items.GemItem;
import me.kench.items.RuneItem;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import me.kench.utils.ItemUtils;
import me.kench.utils.armor.ArmorEquipEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractEvent implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block brokenblock = e.getBlock();

        PlayerData pd = RotMC.getPlayerData(e.getPlayer());

        for(Block b : pd.goldblocks) {
            if(sameLoc(b.getLocation(), brokenblock.getLocation()) && brokenblock.getType() == Material.GOLD_BLOCK) {
                e.setCancelled(true);
            }
        }

        for(Block b : pd.iceblocks) {
            if(sameLoc(b.getLocation(), brokenblock.getLocation()) &&
                    (brokenblock.getType() == Material.ICE || brokenblock.getType() == Material.BLUE_ICE)) {
                e.setCancelled(true);
            }
        }

        for(Block b : pd.obbyblocks) {
            if(sameLoc(b.getLocation(), brokenblock.getLocation()) &&
                    (brokenblock.getType() == Material.OBSIDIAN || brokenblock.getType() == Material.COAL_BLOCK)) {
                e.setCancelled(true);
            }
        }

    }

    private boolean sameLoc(Location loc1, Location loc2) {
        return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
    }

    @EventHandler
    public void onCancel(ArmorEquipEvent e) {

        ItemStack clickedItem = e.getNewArmorPiece();
        Player p = e.getPlayer();

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        ItemMeta meta = clickedItem.getItemMeta();

        if (!meta.hasLore()) return;

        checkIfArmorPutOn(e, p, clickedItem, meta);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack clickedItem = e.getItem();
        if (e.getAction() == Action.PHYSICAL || clickedItem == null || !clickedItem.hasItemMeta()) return;

        ItemMeta meta = clickedItem.getItemMeta();
        if (!meta.hasLore()) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (ItemUtils.isGem(meta.getDisplayName())) {
                GemItem gemItem = new GemItem(clickedItem);
                gemItem.update();
                return;
            }

            if (ItemUtils.isRune(meta.getDisplayName())) {
                RuneItem runeItem = new RuneItem(clickedItem);
                runeItem.update();
                return;
            }

            if (ItemUtils.isEssence(meta.getDisplayName())) {
                EssenceItem essenceItem = new EssenceItem(clickedItem);
                essenceItem.update();
                return;
            }

        }

        if (clickedItem.getType() == Material.CARROT_ON_A_STICK) {

            PlayerData pd = RotMC.getPlayerData(p);
            if (pd == null) return;

            PlayerClass pc = pd.getMainClass();
            if (pc == null) return;

            GameItem gameItem;

            if(pd.gameItem == null || !pd.gameItem.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(meta.getDisplayName())) {
                gameItem = new GameItem(clickedItem);
            } else {
                gameItem = pd.gameItem;
            }

            if (gameItem.getLevel() != 0) {
                int level = gameItem.getLevel();

                if (pc.getLevel() < level) {

                    p.sendMessage(ChatColor.RED + "You need to be level " + level + " to use this item!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                    return;
                }
            } else {
                return;
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
            } else {
                return;
            }

            if(!foundClass) {
                p.sendMessage(ChatColor.RED + "That item is not suitable for " + currentclass + "!");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                return;
            }

            return;
        }

        checkIfArmorPutOn(e, p, clickedItem, meta);
    }

    private void checkIfArmorPutOn(Cancellable e, Player p, ItemStack clickedItem, ItemMeta meta) {

        if (isWearable(clickedItem)) {

            Action action = e instanceof PlayerInteractEvent ? ((PlayerInteractEvent) e).getAction() : Action.RIGHT_CLICK_AIR;

            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {

                PlayerData pd = RotMC.getPlayerData(p);
                if (pd == null) return;

                PlayerClass pc = pd.getMainClass();
                if (pc == null) return;

                GameItem gameItem;

                if (pd.gameItem == null || !pd.gameItem.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(meta.getDisplayName())) {
                    gameItem = new GameItem(clickedItem);
                } else {
                    gameItem = pd.gameItem;
                }

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

                if (!foundClass) {
                    p.sendMessage(ChatColor.RED + "That item is not suitable for " + currentclass + "!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);

                    e.setCancelled(true);
                    return;
                }
            }
        }
    }


    private boolean isWearable(ItemStack item) {
        String typename = item.getType().toString().toUpperCase();

        return item.getType() == Material.ELYTRA ||
                typename.contains("HELMET") || typename.contains("CHESTPLATE") || typename.contains("LEGGINGS") || typename.contains("BOOTS");
    }

}
