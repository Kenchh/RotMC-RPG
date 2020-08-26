package me.kench.items.stats.essenceanimations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class Fire extends EssenceAnimation {

    public Fire() {
        super("Fire", 40, 3);
    }

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {

        Location ploc = p.getLocation().clone();

        double offxfire = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;
        double offyfire = (((double) (new Random().nextInt(10) + 1)) / 10D);
        double offzfire = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;

        Vector vfire = new Vector(offxfire, offyfire, offzfire);

        p.getWorld().spawnParticle(Particle.FLAME, ploc.add(vfire), 0, 0, 0, 0);

        double offxsmoke = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;
        double offzsmoke = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;

        Vector vsmoke = new Vector(offxsmoke, -0.5, offzsmoke);

        p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, ploc.add(vsmoke), 0, 0, 0, 0);
        super.run();
    }

}
