package me.reykench.events;

import me.reykench.RotMC;
import me.reykench.player.PlayerClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(RotMC.getPlayerData(e.getPlayer()) != null && RotMC.getPlayerData(e.getPlayer()).getMainClass() != null) {
            PlayerClass pc = RotMC.getPlayerData(e.getPlayer()).getMainClass();
            e.setFormat(e.getFormat().replace("{sap_default_currentlevel}", pc.getLevel() + "").replace("{sap_default_currentprefix}", pc.getData().getName()));
        }
    }

}
