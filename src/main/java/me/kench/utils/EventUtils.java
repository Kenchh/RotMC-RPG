package me.kench.utils;

import me.kench.RotMC;
import me.kench.items.GameItem;
import me.kench.player.PlayerClass;
import me.kench.player.RpgClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class EventUtils {
    public static void checkLevelAndClass(Cancellable event, Player player, ItemStack clickedItem) {
        // Should hopefully be cached, but sync is unavoidable here without major rewrites
        PlayerClass selectedClass = RotMC.getInstance().getDataManager().getPlayerData().load(player.getUniqueId()).getSelectedClass();

        GameItem gameItem = new GameItem(clickedItem);
        if (gameItem.getLevel() > 0) {
            int level = gameItem.getLevel();

            if (selectedClass.getLevel() < level) {
                Messaging.sendMessage(player, String.format("<red>You need to be level %d to use this item!", level));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                event.setCancelled(true);
                return;
            }
        }

        RpgClass rpgClass = selectedClass.getRpgClass();
        if (!gameItem.getRpgClasses().contains(rpgClass)) {
            Messaging.sendMessage(player, String.format("<red>That item is not suitable for %s!", rpgClass.getName()));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            event.setCancelled(true);
        }
    }

    public static boolean isGameItem(ItemStack stack) {
        return stack.getType() == Material.CARROT_ON_A_STICK || isWearable(stack);
    }

    public static boolean isWearable(ItemStack stack) {
        Material type = stack.getType();
        String typeName = type.name();

        // TODO: fix this monstrosity
        return type == Material.ELYTRA ||
                typeName.contains("HELMET") ||
                typeName.contains("CHESTPLATE") ||
                typeName.contains("LEGGINGS") ||
                typeName.contains("BOOTS");
    }
}
