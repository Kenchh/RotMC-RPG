package me.kench.events;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerDataDam;
import me.kench.gui.CreateClassGUI;
import me.kench.items.stats.EssenceType;
import me.kench.player.PlayerClass;
import me.kench.utils.Messaging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class JoinLeaveEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerClass selectedClass = data.getSelectedClass();

                    if (selectedClass != null) {
                        RotMC.getInstance().getLevelProgression().displayLevelProgression(player);

                        // TODO: display components
                        Messaging.sendMessage(player, String.format("<green>Your current profile: <yellow>%s <gold>%d", selectedClass.getRpgClass().getName(), selectedClass.getLevel()));

                        selectedClass.applyStats();
                        data.ensureClassPermissions();
                    } else {
                        player.openInventory(new CreateClassGUI(data).getInv());
                    }

                    data.startTicker();
                })
                .execute();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerDataDam dam = RotMC.getInstance().getDataManager().getAccessor().getPlayerData();

        dam.loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    ArrayList<EssenceType> essenceTypes = new ArrayList<>();

                    for (EssenceType essenceType : data.getActiveEssences().keySet()) {
                        essenceTypes.add(essenceType);
                        data.getActiveEssences().get(essenceType).cancel();
                    }

                    for (EssenceType essenceType : essenceTypes) {
                        data.getActiveEssences().remove(essenceType);
                    }

                    data.cancelTicker();
                })
                .async(() -> dam.invalidate(player.getUniqueId()))
                .execute();
    }
}
