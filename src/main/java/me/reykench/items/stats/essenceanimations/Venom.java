package me.reykench.items.stats.essenceanimations;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Venom extends EssenceAnimation {

    Vector offset = new Vector(0, 0, 0);

    public Venom() {
        super("Venom", 40, 1);
    }

    @Override
    public void start(Player p) {
        super.start(p);
    }

    @Override
    public void run() {

        if(ratio >= 0 && ratio < 0.25) {
            offset.setX(offset.getX() + 0.1);
            offset.setY(offset.getY() + 0.03);
            offset.setZ(offset.getZ() - 0.1);
        }

        if(ratio >= 0.25 && ratio < 0.5) {
            offset.setX(offset.getX() - 0.1);
            offset.setY(offset.getY() + 0.03);
            offset.setZ(offset.getZ() - 0.1);
        }

        if(ratio >= 0.5 && ratio < 0.75) {
            offset.setX(offset.getX() - 0.1);
            offset.setY(offset.getY() - 0.03);
            offset.setZ(offset.getZ() + 0.1);
        }

        if(ratio >= 0.75 && ratio < 1) {
            offset.setX(offset.getX() + 0.1);
            offset.setY(offset.getY() - 0.03);
            offset.setZ(offset.getZ() + 0.1);
        }

        if(ratio == 1) {
            offset.setX(0);
            offset.setY(0);
            offset.setZ(0);
        }

        Vector offset2 = offset.clone();
        offset2.setX(offset2.getX() * -1);
        offset2.setZ(offset2.getZ() * -1);

        Location ploc = p.getLocation();
        p.getWorld().spawnParticle(Particle.REDSTONE, ploc.getX() + offset.getX(), ploc.getY() + offset.getY(), ploc.getZ() + offset.getZ() + 1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.OLIVE, 1));
        p.getWorld().spawnParticle(Particle.REDSTONE, ploc.getX() + offset2.getX(), ploc.getY() + offset2.getY(), ploc.getZ() + offset2.getZ() - 1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.OLIVE, 1));

        p.getWorld().spawnParticle(Particle.REDSTONE, ploc.getX() + offset.getX(), ploc.getY() + offset.getY(), ploc.getZ() + offset.getZ() + 1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.fromBGR(55, 190, 190), 1));
        p.getWorld().spawnParticle(Particle.REDSTONE, ploc.getX() + offset2.getX(), ploc.getY() + offset2.getY(), ploc.getZ() + offset2.getZ() - 1, 0, 0.001, 1, 0, 1, new Particle.DustOptions(Color.fromBGR(55, 190, 190), 1));
        super.run();
    }
}
