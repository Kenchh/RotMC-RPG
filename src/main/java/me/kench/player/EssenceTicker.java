package me.kench.player;

import me.kench.RotMC;
import me.kench.utils.GlowUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EssenceTicker extends BukkitRunnable {
    private Player player;

    public EssenceTicker(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                .loadSafe(player.getUniqueId())
                .syncLast(data -> {
                    RotMC.newChain()
                            .delay(1)
                            .sync(() -> {
                                PlayerClass selectedClass = data.getSelectedClass();
                                if (selectedClass != null) {
                                    selectedClass.tickEssences(data);
                                    selectedClass.applyStats();
                                }
                            })
                            .execute();

                    GlowUtils.clearWhenForbidden(player);
                });
    }
}
