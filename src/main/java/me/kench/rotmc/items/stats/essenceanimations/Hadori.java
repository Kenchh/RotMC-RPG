package me.kench.rotmc.items.stats.essenceanimations;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.utils.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Hadori extends EssenceAnimation {

    public Hadori() {
        super("Hadori", 40, 5);
    }

    Location lastloc;

    @Override
    public void start(Player p) {
        lastloc = p.getLocation().clone();
        super.start(p);
    }

    @Override
    public void run() {

        Location ploc = player.getLocation().clone();

        bloom(ploc);

        super.run();
    }

    public void bloom(Location loc) {

        ArrayList<Material> flowers = new ArrayList<>();
        flowers.add(Material.CORNFLOWER);
        flowers.add(Material.POPPY);
        flowers.add(Material.BLUE_ORCHID);
        flowers.add(Material.ORANGE_TULIP);
        flowers.add(Material.PINK_TULIP);
        flowers.add(Material.RED_TULIP);
        flowers.add(Material.WHITE_TULIP);
        flowers.add(Material.AZURE_BLUET);
        flowers.add(Material.OXEYE_DAISY);
        flowers.add(Material.DANDELION);
        flowers.add(Material.ALLIUM);
        flowers.add(Material.LILY_OF_THE_VALLEY);

        ArrayList<Material> suitable = new ArrayList<>();
        suitable.add(Material.DIRT);
        suitable.add(Material.COARSE_DIRT);
        suitable.add(Material.GRASS_BLOCK);
        suitable.add(Material.PODZOL);
        suitable.add(Material.MYCELIUM);

        ArrayList<Location> clocs = BlockUtils.circleLocations(loc, 3, 5);

        for (Location l : clocs) {
            Block b = l.getBlock();

            if (!b.getType().isSolid() && !b.isLiquid() && !flowers.contains(b.getType())) {
                if (suitable.contains(b.getRelative(BlockFace.DOWN).getType())) {

                    if (new Random().nextInt(100) >= 20) {
                        b.setType(Material.GRASS);
                    } else {
                        Material m = flowers.get(new Random().nextInt(flowers.size()));
                        b.setType(m);
                    }

                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, l, 1);

                }
            }
        }

        if (sameLoc(loc)) return;

        lastloc = loc.clone();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!sameLoc(loc)) {
                    for (Location l : clocs) {
                        if (suitable.contains(l.getBlock().getType())) {
                            l.getBlock().setType(Material.AIR);
                            cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(RotMcPlugin.getInstance(), 1L, 40L);

    }

    public boolean sameLoc(Location loc) {
        return loc.getBlockX() == lastloc.getBlockX() && loc.getBlockY() == lastloc.getBlockY() && loc.getBlockZ() == lastloc.getBlockZ();
    }

}
