package me.kench.listener;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // This is already called async, so it's okay that this is not in a chain. Should still be cached.

        PlayerClass selectedClass = RotMC.getInstance().getDataManager().getPlayerData()
                .load(event.getPlayer().getUniqueId())
                .getSelectedClass();

        if (selectedClass != null) {
            event.setFormat(event.getFormat()
                    .replace("{sap_default_currentlevel}", String.format("%d", selectedClass.getLevel()))
                    .replace("{sap_default_currentprefix}", selectedClass.getRpgClass().getName())
            );
        }
    }
}
