package me.kench.rotmc.items.stats.essenceanimations;

import me.kench.rotmc.RotMcPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EssenceAnimation extends BukkitRunnable {
    Player player;

    String name;

    int ticks = 0;
    int maxticks;

    float ratio = 0;

    long speed;

    public EssenceAnimation(String name, int maxticks, long speed) {
        this.name = name;
        this.maxticks = maxticks;
        this.speed = speed;
    }

    public void start(Player player) {
        this.player = player;
        this.runTaskTimer(RotMcPlugin.getInstance(), 1L, speed);
    }

    @Override
    public void run() {
        if (ticks < maxticks) {
            ticks++;
        } else {
            ticks = 0;
        }
        ratio = (float) ticks / (float) maxticks;
    }


}
