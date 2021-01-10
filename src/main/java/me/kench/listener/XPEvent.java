package me.kench.listener;

import me.kench.RotMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class XPEvent implements Listener {
    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
        RotMC.getInstance().getLevelProgression().displayLevelProgression(event.getPlayer());
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {
        RotMC.getInstance().getLevelProgression().displayLevelProgression(event.getPlayer());
    }
}
