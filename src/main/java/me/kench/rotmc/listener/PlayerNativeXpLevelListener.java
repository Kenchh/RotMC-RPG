package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class PlayerNativeXpLevelListener implements Listener {
    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
        RotMcPlugin.getInstance().getLevelProgression().displayLevelProgression(event.getPlayer());
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {
        RotMcPlugin.getInstance().getLevelProgression().displayLevelProgression(event.getPlayer());
    }
}
