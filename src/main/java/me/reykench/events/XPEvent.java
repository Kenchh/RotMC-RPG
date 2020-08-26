package me.reykench.events;

import me.reykench.RotMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class XPEvent implements Listener {

    @EventHandler
    public void onXPGain(PlayerExpChangeEvent e) {
        e.setAmount(0);
        RotMC.getInstance().getLevelProgression().displayLevelProgression(e.getPlayer());
    }

    @EventHandler
    public void onXPGain(PlayerLevelChangeEvent e) {
        RotMC.getInstance().getLevelProgression().displayLevelProgression(e.getPlayer());
    }

}
