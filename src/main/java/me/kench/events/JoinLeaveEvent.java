package me.kench.events;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerDataDam;
import me.kench.gui.CreateClassGUI;
import me.kench.items.stats.EssenceType;
import me.kench.player.PlayerClass;
import org.bukkit.ChatColor;
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
                        RotMC.getInstance().getLevelProgression().displayLevelProgression(player, selectedClass);

                        // TODO: display components
                        player.sendMessage(ChatColor.GREEN + "Your current profile: " + ChatColor.YELLOW + selectedClass.getData().getName() + " " + ChatColor.GOLD + selectedClass.getLevel());

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
                    ArrayList<EssenceType> etToRemove = new ArrayList<>();

                    for (EssenceType et : data.getActiveEssences().keySet()) {
                        etToRemove.add(et);
                        data.getActiveEssences().get(et).cancel();
                    }

                    for (EssenceType et : etToRemove) {
                        data.getActiveEssences().remove(et);
                    }

                    data.cancelTicker();
                })
                .async(() -> dam.invalidate(player.getUniqueId()))
                .execute();
    }
}
