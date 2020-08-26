package me.kench.events;

import me.kench.items.GemItem;
import me.kench.items.RuneItem;
import me.kench.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickUpEvent implements Listener {

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player) || e.isCancelled()) return;

        if(!(e.getItem().getItemStack().hasItemMeta())) return;
        if(!(e.getItem().getItemStack().getItemMeta().hasDisplayName())) return;

        if(ItemUtils.isGem(e.getItem().getItemStack().getItemMeta().getDisplayName())) {
            new GemItem(e.getItem().getItemStack());
        }

        if(ItemUtils.isRune(e.getItem().getItemStack().getItemMeta().getDisplayName())) {
            new RuneItem(e.getItem().getItemStack());
        }

        /*
        if(ItemUtils.isEssence(e.getItem().getItemStack().getItemMeta().getDisplayName())) {
            new EssenceItem(e.getItem().getItemStack());
        }
        */

    }
}
