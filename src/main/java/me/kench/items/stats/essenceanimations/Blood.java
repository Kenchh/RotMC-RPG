package me.kench.items.stats.essenceanimations;

import me.kench.utils.BlockUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Blood extends EssenceAnimation {

    public Blood() {
        super("Blood", 40, 1);
    }

    double lastY;

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {

        Location ploc = player.getLocation().clone();

        double xzcords1[] = BlockUtils.getXZCoordsFromDegree(ploc, 0.5D, 360D * ((double) ratio));
        Location loc1 = new Location(ploc.getWorld(), xzcords1[0], ploc.getY(), xzcords1[1]);
        player.getWorld().spawnParticle(Particle.REDSTONE, loc1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.RED, 1));

        double xzcords2[] = BlockUtils.getXZCoordsFromDegree(ploc, 0.75D, 360D * ((double) ratio));
        Location loc2 = new Location(ploc.getWorld(), xzcords2[0], ploc.getY() + 0.33, xzcords2[1]);
        player.getWorld().spawnParticle(Particle.REDSTONE, loc2, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.fromBGR(100, 100, 255), 1));

        super.run();
    }

}
