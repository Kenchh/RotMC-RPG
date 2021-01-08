package me.kench.events;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(event.getPlayer().getUniqueId())
                .asyncLast(data -> {
                    PlayerClass selectedClass = data.getSelectedClass();
                    if (selectedClass != null) {
                        event.setFormat(event.getFormat()
                                .replace("{sap_default_currentlevel}", selectedClass.getLevel() + "")
                                .replace("{sap_default_currentprefix}", selectedClass.getRpgClass().getName())
                        );
                    }
                })
                .execute();
    }
}
