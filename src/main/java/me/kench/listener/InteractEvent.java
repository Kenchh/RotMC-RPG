package me.kench.listener;

import me.kench.RotMC;
import me.kench.items.*;
import me.kench.player.PlayerClass;
import me.kench.player.RpgClass;
import me.kench.session.PlayerSession;
import me.kench.utils.EventUtils;
import me.kench.utils.ItemUtils;
import me.kench.utils.Messaging;
import me.kench.utils.armor.ArmorEquipEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        PlayerSession session = RotMC.getInstance().getSessionManager().getSession(event.getPlayer().getUniqueId());
        if (session == null) return;

        // TODO: make this prettier later

        Block brokenBlock = event.getBlock();

        for (Block block : session.getGoldBlocks()) {
            if (sameLoc(block.getLocation(), brokenBlock.getLocation()) && brokenBlock.getType() == Material.GOLD_BLOCK) {
                event.setCancelled(true);
            }
        }

        for (Block block : session.getIceBlocks()) {
            if (sameLoc(block.getLocation(), brokenBlock.getLocation()) && (brokenBlock.getType() == Material.ICE || brokenBlock.getType() == Material.BLUE_ICE)) {
                event.setCancelled(true);
            }
        }

        for (Block block : session.getObsidianBlocks()) {
            if (sameLoc(block.getLocation(), brokenBlock.getLocation()) && (brokenBlock.getType() == Material.OBSIDIAN || brokenBlock.getType() == Material.COAL_BLOCK)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean sameLoc(Location firstLocation, Location secondLocation) {
        return firstLocation.getX() == secondLocation.getX() && firstLocation.getY() == secondLocation.getY() && firstLocation.getZ() == secondLocation.getZ();
    }

    @EventHandler
    public void onCancel(ArmorEquipEvent event) {
        ItemStack clickedItem = event.getNewArmorPiece();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        checkLevelAndClass(event, event.getPlayer(), clickedItem);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack clickedItem = event.getItem();

        if (event.getAction() == Action.PHYSICAL || clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemBuilder builder = ItemBuilder.of(clickedItem);

            if (ItemUtils.isGem(builder.name())) {
                new GemItem(clickedItem).update();
                return;
            }

            if (ItemUtils.isRune(builder.name())) {
                new RuneItem(clickedItem).update();
                return;
            }

            if (ItemUtils.isEssence(builder.name())) {
                new EssenceItem(clickedItem).update();
                return;
            }
        }

        checkLevelAndClass(event, event.getPlayer(), clickedItem);
    }

    private void checkLevelAndClass(Cancellable event, Player player, ItemStack clickedItem) {
        if (!EventUtils.isGameItem(clickedItem)) return;

        Action action = event instanceof PlayerInteractEvent ? ((PlayerInteractEvent) event).getAction() : Action.RIGHT_CLICK_AIR;
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            EventUtils.checkLevelAndClass(event, player, clickedItem);
        }
    }
}
