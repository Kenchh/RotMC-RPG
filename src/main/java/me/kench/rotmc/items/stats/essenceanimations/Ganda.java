package me.kench.rotmc.items.stats.essenceanimations;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.session.PlayerSession;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Ganda extends EssenceAnimation {
    public Ganda() {
        super("Ganda", 40, 1);
    }

    private double lastX;
    private double lastY;
    private double lastZ;
    private PlayerSession session;

    @Override
    public void start(Player player) {
        session = RotMcPlugin.getInstance().getSessionManager().getSession(player.getUniqueId());

        lastX = player.getLocation().getBlockX();
        lastY = player.getLocation().getBlockY();
        lastZ = player.getLocation().getBlockZ();

        super.start(player);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        List<Block> toRemove = new ArrayList<>();

        for (Block block : session.getGoldBlocks()) {
            block.getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
            toRemove.add(block);
        }

        for (Block block : toRemove) {
            session.getGoldBlocks().remove(block);
        }

        super.cancel();
    }

    @Override
    public void run() {
        Location playerLocation = player.getLocation().clone();

        Block blockBelow = playerLocation.getBlock().getRelative(BlockFace.DOWN);

        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(blockBelow);
        blocks.add(blockBelow.getRelative(BlockFace.NORTH));
        blocks.add(blockBelow.getRelative(BlockFace.EAST));
        blocks.add(blockBelow.getRelative(BlockFace.SOUTH));
        blocks.add(blockBelow.getRelative(BlockFace.WEST));

        if (!(lastX == player.getLocation().getBlockX() && lastY == player.getLocation().getBlockY() && lastZ == player.getLocation().getBlockZ())) {
            for (Block block : blocks) {
                if (block.getType() == Material.CAVE_AIR || block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
                    block.setType(Material.GOLD_BLOCK);

                    if (!session.getGoldBlocks().contains(block)) {
                        session.getGoldBlocks().add(block);
                    }

                }
            }

            new BukkitRunnable() {
                final int lx = (int) lastX;
                final int ly = (int) lastY;
                final int lz = (int) lastZ;

                @Override
                public void run() {
                    Block lastBlockBelow = player.getWorld().getBlockAt(lx, ly, lz).getRelative(BlockFace.DOWN);

                    ArrayList<Block> lastBlocks = new ArrayList<>();
                    lastBlocks.add(lastBlockBelow);
                    lastBlocks.add(lastBlockBelow.getRelative(BlockFace.NORTH));
                    lastBlocks.add(lastBlockBelow.getRelative(BlockFace.EAST));
                    lastBlocks.add(lastBlockBelow.getRelative(BlockFace.SOUTH));
                    lastBlocks.add(lastBlockBelow.getRelative(BlockFace.WEST));

                    for (Block lastBlock : lastBlocks) {
                        if (sameLoc(lastBlock.getLocation(), blockBelow.getLocation())) {
                            continue;
                        }

                        if (session.getGoldBlocks().contains(lastBlock)) {
                            session.getGoldBlocks().remove(lastBlock);
                            if (lastBlock.getType() == Material.GOLD_BLOCK) {
                                lastBlock.setType(Material.AIR);
                            }
                        }
                    }

                }
            }.runTaskLater(RotMcPlugin.getInstance(), 15L);

            lastX = player.getLocation().getBlockX();
            lastY = player.getLocation().getBlockY();
            lastZ = player.getLocation().getBlockZ();
        }

        super.run();
    }

    private boolean sameLoc(Location loc1, Location loc2) {
        return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
    }
}
