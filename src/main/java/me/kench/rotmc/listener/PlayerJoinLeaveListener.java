package me.kench.rotmc.listener;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.database.playerdata.PlayerDataDam;
import me.kench.rotmc.gui.createclass.CreateClassGui;
import me.kench.rotmc.items.stats.EssenceType;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.session.PlayerSession;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerJoinLeaveListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        RotMcPlugin.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .syncLast(data -> {
                    RotMcPlugin.getInstance().getSessionManager().addSession(data.getUniqueId());

                    PlayerClass selectedClass = data.getSelectedClass();
                    if (selectedClass != null) {
                        RotMcPlugin.getInstance().getLevelProgression().displayLevelProgression(player);

                        Messaging.sendMessage(player, String.format("<green>Your current profile: <yellow>%s <gold>%d", selectedClass.getRpgClass().getName(), selectedClass.getLevel()));

                        selectedClass.applyStats();
                        data.ensureClassPermissions();
                    } else {
                        new CreateClassGui().display(player);
                    }

                    data.getSession().startTicker();
                })
                .execute();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerDataDam dam = RotMcPlugin.getInstance().getDataManager().getPlayerData();

        dam.chainLoadSafe(player.getUniqueId())
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

                    RotMcPlugin.getInstance().getSessionManager().removeSession(session.getUniqueId());
                })
                .async(() -> dam.invalidate(player.getUniqueId()))
                .execute();
    }
}
