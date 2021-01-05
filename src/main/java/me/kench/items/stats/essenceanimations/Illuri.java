package me.kench.items.stats.essenceanimations;

import me.kench.RotMC;
import me.kench.player.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Illuri extends EssenceAnimation {

    public Illuri() {
        super("Illuri", 40, 1);
    }

    double lastX;
    double lastY;
    double lastZ;

    PlayerData pd;

    @Override
    public void start(Player p) {
        pd = RotMC.getPlayerData(p);

        lastX = p.getLocation().getBlockX();
        lastY = p.getLocation().getBlockY();
        lastZ = p.getLocation().getBlockZ();
        super.start(p);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {

        ArrayList<Block> toremove = new ArrayList<>();
        for (Block b : pd.iceblocks) {
            b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
            toremove.add(b);
        }

        for (Block tr : toremove) {
            pd.iceblocks.remove(tr);
        }
        super.cancel();
    }

    @Override
    public void run() {

        Location ploc = p.getLocation().clone();

        Block bu = ploc.getBlock().getRelative(BlockFace.DOWN);

        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(bu);
        blocks.add(bu.getRelative(BlockFace.NORTH));
        blocks.add(bu.getRelative(BlockFace.EAST));
        blocks.add(bu.getRelative(BlockFace.SOUTH));
        blocks.add(bu.getRelative(BlockFace.WEST));

        if (!(lastX == p.getLocation().getBlockX() && lastY == p.getLocation().getBlockY() && lastZ == p.getLocation().getBlockZ())) {

            for (Block b : blocks) {
                if (b.getType() == Material.WATER) {
                    if (b.getBlockData().getAsString().contains("level=0")) {
                        b.setType(Material.BLUE_ICE);

                        if (!pd.iceblocks.contains(b)) {
                            pd.iceblocks.add(b);
                        }

                    } else {
                        b.setType(Material.ICE);

                        if (!pd.iceblocks.contains(b)) {
                            pd.iceblocks.add(b);
                        }
                    }
                }
            }

            new BukkitRunnable() {
                int lx = (int) lastX;
                int ly = (int) lastY;
                int lz = (int) lastZ;

                @Override
                public void run() {

                    Block lastBU = p.getWorld().getBlockAt(lx, ly, lz).getRelative(BlockFace.DOWN);

                    ArrayList<Block> lastblocks = new ArrayList<>();
                    lastblocks.add(lastBU);
                    lastblocks.add(lastBU.getRelative(BlockFace.NORTH));
                    lastblocks.add(lastBU.getRelative(BlockFace.EAST));
                    lastblocks.add(lastBU.getRelative(BlockFace.SOUTH));
                    lastblocks.add(lastBU.getRelative(BlockFace.WEST));

                    for (Block lastB : lastblocks) {

                        if (sameLoc(lastB.getLocation(), bu.getLocation())) continue;

                        if (pd.iceblocks.contains(lastB)) {
                            pd.iceblocks.remove(lastB);
                            if (lastB.getType() == Material.ICE) {
                                lastB.setType(Material.AIR);
                            }

                            if (lastB.getType() == Material.BLUE_ICE) {
                                lastB.setType(Material.WATER);
                            }
                        }
                    }

                }
            }.runTaskLater(RotMC.getInstance(), 15L);

            lastX = p.getLocation().getBlockX();
            lastY = p.getLocation().getBlockY();
            lastZ = p.getLocation().getBlockZ();
        }

        super.run();
    }

    private boolean sameLoc(Location loc1, Location loc2) {
        if (loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ()) return true;

        return false;
    }

}
