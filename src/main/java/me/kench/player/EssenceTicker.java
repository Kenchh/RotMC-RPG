package me.kench.player;

import com.andrebreves.tuple.Tuple;
import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.utils.GlowUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EssenceTicker extends BukkitRunnable {
    private final Player player;

    public EssenceTicker(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        RotMC.getInstance().getDataManager().getPlayerData()
                .chainLoadSafe(player.getUniqueId())
                .async(data -> Tuple.of(data, GlowUtils.checkPlayerGlowPermitted(data)))
                .syncLast(tuple -> {
                    PlayerData data = tuple.v1();
                    boolean glowPermitted = tuple.v2();

                    PlayerClass selectedClass = data.getSelectedClass();
                    if (selectedClass != null) {
                        selectedClass.tickEssences(data);
                        selectedClass.applyStats();
                    }

                    if (!glowPermitted) {
                        GlowUtils.clearGlow(player);
                    }
                })
                .execute();
    }
}
