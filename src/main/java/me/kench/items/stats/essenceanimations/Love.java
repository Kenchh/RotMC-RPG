package me.kench.items.stats.essenceanimations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Love extends EssenceAnimation {

    public Love() {
        super("Love", 40, 5);
    }

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {


        Location ploc = p.getLocation().clone();
        p.getWorld().spawnParticle(Particle.HEART, ploc, 1, 0.25, 0, 0.25);

        super.run();
    }

}
