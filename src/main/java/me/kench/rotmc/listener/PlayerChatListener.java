package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.database.playerdata.PlayerData;
import me.kench.rotmc.gui.chooseclass.ChooseClassGui;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.security.MessageDigest;

public class PlayerChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        // This is already called async, so it's okay that this is not in a chain. Should still be cached.
        Player player = event.getPlayer();

        PlayerData data = RotMcPlugin.getInstance().getDataManager().getPlayerData().load(player.getUniqueId());
        if (!data.hasSelectedClass()) {
            event.setCancelled(true);
            Messaging.sendMessage(player, "<red>Please select a class before trying to chat!");
            new ChooseClassGui().display(player);
            return;
        }

        PlayerClass selectedClass = data.getSelectedClass();

            event.setFormat(event.getFormat()
                    .replace("{sap_default_currentlevel}", String.format("%d", selectedClass.getLevel()))
                    .replace("{sap_default_currentprefix}", selectedClass.getRpgClass().getName())
            );
    }
}
