package me.kench.events;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PlayerData pd = RotMC.getPlayerData(e.getPlayer());

        if(pd != null && pd.getMainClass() != null) {
            PlayerClass pc = pd.getMainClass();
            e.setFormat(e.getFormat().replace("{sap_default_currentlevel}", pc.getLevel() + "").replace("{sap_default_currentprefix}", pc.getData().getName()));
        }
    }

}
