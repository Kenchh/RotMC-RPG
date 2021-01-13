package me.kench.items.stats.essenceanimations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Aqua extends EssenceAnimation {

    public Aqua() {
        super("Aqua", 40, 1);
    }

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {

        Location ploc = player.getLocation().clone();
        player.getWorld().spawnParticle(Particle.WATER_SPLASH, ploc, 5, 0.25, 0, 0.25);
        player.getWorld().spawnParticle(Particle.WATER_BUBBLE, ploc, 10, 0.25, 0.25, 0.25);
        super.run();
    }

}
