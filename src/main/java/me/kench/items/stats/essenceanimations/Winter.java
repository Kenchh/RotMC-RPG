package me.kench.items.stats.essenceanimations;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class Winter extends EssenceAnimation {

    public Winter() {
        super("Winter", 40, 1);
    }

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {

        Location ploc = player.getLocation().clone();

        double offxSnow = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;
        double offySnow = (((double) (new Random().nextInt(10) + 1)) / 10D);
        double offzSnow = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;

        Vector vfire = new Vector(offxSnow, offySnow, offzSnow);

        player.getWorld().spawnParticle(Particle.SNOWBALL, ploc.add(vfire), 0, 0, 0, 0);

        double offxsmoke = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;
        double offzsmoke = (((double) (new Random().nextInt(10) + 1)) / 10D) - 0.5D;

        Vector vsmoke = new Vector(offxsmoke, -0.5, offzsmoke);

        player.getWorld().spawnParticle(Particle.REDSTONE, ploc.add(vsmoke), 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.fromBGR(255, 255, 255), 1));

        super.run();
    }
}
