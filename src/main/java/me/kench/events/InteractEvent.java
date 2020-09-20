package me.kench.events;

import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.items.GameItem;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        ItemStack clickedItem = e.getItem();

        if(clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasLore()) return;

        checkIfArmorPutOn(e, p, clickedItem);
    }

    private void checkIfArmorPutOn(PlayerInteractEvent e, Player p, ItemStack clickedItem) {

        if(isWearable(clickedItem)) {

            if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                GameItem gameItem = new GameItem(clickedItem);

                PlayerData pd = RotMC.getPlayerData(p);
                if (pd == null) return;

                PlayerClass pc = pd.getMainClass();
                if (pc == null) return;

                if (gameItem.getLevel() != 0) {
                    int level = gameItem.getLevel();

                    if (pc.getLevel() < level) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        e.setCancelled(true);
                        return;
                    }
                }

                boolean foundClass = false;
                if (gameItem.getGameClasses().isEmpty() == false) {
                    for(GameClass gameClass : gameItem.getGameClasses()) {
                        String className = gameClass.getName();

                        if (className.equalsIgnoreCase(pc.getData().getName())) {
                            foundClass = true;
                        }
                    }
                }

                if(!foundClass) {
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
