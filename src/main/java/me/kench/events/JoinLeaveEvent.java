package me.kench.events;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerDataDam;
import me.kench.gui.CreateClassGUI;
import me.kench.items.stats.EssenceType;
import me.kench.player.PlayerClass;
import me.kench.session.PlayerSession;
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

        RotMC.getInstance().getDataManager().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    RotMC.getInstance().getSessionManager().addSession(data.getUniqueId());

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

                    data.getSession().startTicker();
                })
                .execute();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerDataDam dam = RotMC.getInstance().getDataManager().getPlayerData();

        dam.loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    PlayerSession session = data.getSession();

                    ArrayList<EssenceType> essenceTypes = new ArrayList<>();

                    for (EssenceType essenceType : session.getActiveEssences().keySet()) {
                        essenceTypes.add(essenceType);
                        session.getActiveEssences().get(essenceType).cancel();
                    }

                    for (EssenceType essenceType : essenceTypes) {
                        session.getActiveEssences().remove(essenceType);
                    }

                    session.cancelTicker();

                    RotMC.getInstance().getSessionManager().removeSession(session.getUniqueId());
                })
                .async(() -> dam.invalidate(player.getUniqueId()))
                .execute();
    }
}
