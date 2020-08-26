package me.reykench.items.stats.essenceanimations;

import me.reykench.RotMC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EssenceAnimation extends BukkitRunnable {

    Player p;

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

    public void start(Player p) {
        this.p = p;
        this.runTaskTimer(RotMC.getInstance(), 1L, speed);
    }

    @Override
    public void run() {
        if(ticks < maxticks) {
            ticks++;
        } else {
            ticks = 0;
        }
        ratio = (float) ticks / (float) maxticks;
    }



}
