package me.kench.utils;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockUtils {
	
	private static HashSet<Byte> blockAirFoliageSet = new HashSet<>();
	private static HashSet<Byte> blockPassSet = new HashSet<>();
	
	public static boolean solid(int block) {
		return solid((byte)block);
	}
	  
	public static boolean solid(byte block) {
	  if (blockPassSet.isEmpty()) {
	      blockPassSet.add(Byte.valueOf((byte)0));
	      blockPassSet.add(Byte.valueOf((byte)6));
	      blockPassSet.add(Byte.valueOf((byte)8));
	      blockPassSet.add(Byte.valueOf((byte)9));
	      blockPassSet.add(Byte.valueOf((byte)10));
	      blockPassSet.add(Byte.valueOf((byte)11));
	      blockPassSet.add(Byte.valueOf((byte)26));
	      blockPassSet.add(Byte.valueOf((byte)27));
	      blockPassSet.add(Byte.valueOf((byte)28));
	      blockPassSet.add(Byte.valueOf((byte)30));
	      blockPassSet.add(Byte.valueOf((byte)31));
	      blockPassSet.add(Byte.valueOf((byte)32));
	      blockPassSet.add(Byte.valueOf((byte)37));
	      blockPassSet.add(Byte.valueOf((byte)38));
	      blockPassSet.add(Byte.valueOf((byte)39));
	      blockPassSet.add(Byte.valueOf((byte)40));
	      blockPassSet.add(Byte.valueOf((byte)50));
	      blockPassSet.add(Byte.valueOf((byte)51));
	      blockPassSet.add(Byte.valueOf((byte)55));
	      blockPassSet.add(Byte.valueOf((byte)59));
	      blockPassSet.add(Byte.valueOf((byte)63));
	      blockPassSet.add(Byte.valueOf((byte)64));
	      blockPassSet.add(Byte.valueOf((byte)65));
	      blockPassSet.add(Byte.valueOf((byte)66));
	      blockPassSet.add(Byte.valueOf((byte)68));
	      blockPassSet.add(Byte.valueOf((byte)69));
	      blockPassSet.add(Byte.valueOf((byte)70));
	      blockPassSet.add(Byte.valueOf((byte)71));
	      blockPassSet.add(Byte.valueOf((byte)72));
	      blockPassSet.add(Byte.valueOf((byte)75));
	      blockPassSet.add(Byte.valueOf((byte)76));
	      blockPassSet.add(Byte.valueOf((byte)77));
	      blockPassSet.add(Byte.valueOf((byte)78));
	      blockPassSet.add(Byte.valueOf((byte)83));
	      blockPassSet.add(Byte.valueOf((byte)90));
	      blockPassSet.add(Byte.valueOf((byte)92));
	      blockPassSet.add(Byte.valueOf((byte)93));
	      blockPassSet.add(Byte.valueOf((byte)94));
	      blockPassSet.add(Byte.valueOf((byte)96));
	      blockPassSet.add(Byte.valueOf((byte)101));
	      blockPassSet.add(Byte.valueOf((byte)102));
	      blockPassSet.add(Byte.valueOf((byte)104));
	      blockPassSet.add(Byte.valueOf((byte)105));
	      blockPassSet.add(Byte.valueOf((byte)106));
	      blockPassSet.add(Byte.valueOf((byte)107));
	      blockPassSet.add(Byte.valueOf((byte)111));
	      blockPassSet.add(Byte.valueOf((byte)115));
	      blockPassSet.add(Byte.valueOf((byte)116));
	      blockPassSet.add(Byte.valueOf((byte)117));
	      blockPassSet.add(Byte.valueOf((byte)118));
	      blockPassSet.add(Byte.valueOf((byte)119));
	      blockPassSet.add(Byte.valueOf((byte)120));
	      blockPassSet.add(Byte.valueOf((byte)-85));
	  }
	    
	  return !blockPassSet.contains(Byte.valueOf(block));
	}

	public static Block getTargetBlock(Player player, int range) {
		Location loc = player.getEyeLocation();
		Vector dir = loc.getDirection().normalize();

		Block b = null;

		for (int i = 0; i <= range; i++) {

			b = loc.add(dir).getBlock();
		}
		return b;
	}

	public static boolean atBlockGap(Player p, Block block) {

		/*
		 * Sketch https://imgur.com/a/H1BUrBQ
		 */

		double yaw = p.getLocation().getYaw();
		double angle = Math.toRadians(yaw);

		/* South - West */
		if (angle >= 0 + 0.3 && angle <= Math.PI / 2 - 0.3
				|| angle >= -2 * Math.PI + 0.3 && angle <= -3 * (Math.PI / 2) - 0.3) {

			Location locAtX = block.getLocation();
			locAtX.setX(locAtX.getX() - 1);

			Location locAtZ = block.getLocation();
			locAtZ.setZ(locAtZ.getZ() + 1);

			if (locAtX.getBlock().getType().isSolid() && locAtZ.getBlock().getType().isSolid()) {
				return true;
			}

		}

		/* North - West */
		if (angle >= Math.PI / 2 + 0.3 && angle <= Math.PI - 0.3
				|| angle >= -3 * (Math.PI / 2) + 0.3 && angle <= -Math.PI - 0.3) {

			Location locAtX = block.getLocation();
			locAtX.setX(locAtX.getX() - 1);

			Location locAtZ = block.getLocation();
			locAtZ.setZ(locAtZ.getZ() - 1);

			if (locAtX.getBlock().getType().isSolid() && locAtZ.getBlock().getType().isSolid()) {
				return true;
			}

		}

		/* North - East */
		if (angle >= Math.PI + 0.3 && angle <= 3 * (Math.PI / 2) - 0.3
				|| angle >= -Math.PI + 0.3 && angle <= -1 * (Math.PI / 2) - 0.3) {

			Location locAtX = block.getLocation();
			locAtX.setX(locAtX.getX() + 1);

			Location locAtZ = block.getLocation();
			locAtZ.setZ(locAtZ.getZ() - 1);

			if (locAtX.getBlock().getType().isSolid() && locAtZ.getBlock().getType().isSolid()) {
				return true;
			}

		}

		/* South - East */
		if (angle >= 3 * (Math.PI / 2) + 0.3 && angle <= 2 * Math.PI - 0.3
				|| angle >= -1 * (Math.PI / 2) + 0.3 && angle <= 0 - 0.3) {

			Location locAtX = block.getLocation();
			locAtX.setX(locAtX.getX() + 1);

			Location locAtZ = block.getLocation();
			locAtZ.setZ(locAtZ.getZ() + 1);

			if (locAtX.getBlock().getType().isSolid() && locAtZ.getBlock().getType().isSolid()) {
				return true;
			}

		}

		return false;

	}

	public static Block getBlockUnderneath(Block b) {
		Location loc = new Location(b.getWorld(), b.getLocation().getX(),
				b.getLocation().getY() - 1.0, b.getLocation().getZ());
		Block bu = Bukkit.getWorld(b.getWorld().getName()).getBlockAt(loc);
		return bu;
	}

	public static Block getBlockAbove(Block b) {
		Location loc = new Location(b.getWorld(), b.getLocation().getX(),
				b.getLocation().getY() + 1.0, b.getLocation().getZ());
		Block ba = Bukkit.getWorld(b.getWorld().getName()).getBlockAt(loc);
		return ba;
	}

	public static BlockFace getClosestFace(float direction) {

		direction = direction % 360;

		if (direction < 0)
			direction += 360;

		direction = Math.round(direction / 90);

		switch ((int) direction) {

		case 0:
			return BlockFace.WEST;
		case 1:
			return BlockFace.NORTH;
		case 2:
			return BlockFace.EAST;
		case 3:
			return BlockFace.SOUTH;
		default:
			return BlockFace.WEST;

		}
	}

	public static double[] getXZCordsFromDegree(Player p, double radius, double degree) {
		double radian = Math.toRadians(degree);

		double xMultiplier = Math.cos(radian);
		double zMultiplier = Math.sin(radian);

		double addX = xMultiplier * radius;
		double addZ = zMultiplier * radius;

		double[] xzCords = new double[2];

		double x = p.getLocation().getX() + addX;
		double z = p.getLocation().getZ() + addZ;

		xzCords[0] = x;
		xzCords[1] = z;

		return xzCords;
	}

	public static double[] getXZCordsFromDegree(Location loc, double radius, double degree) {
		double radian = Math.toRadians(degree);

		double xMultiplier = Math.cos(radian);
		double zMultiplier = Math.sin(radian);

		double addX = xMultiplier * radius;
		double addZ = zMultiplier * radius;

		double[] xzCords = new double[2];

		double x = loc.getX() + addX;
		double z = loc.getZ() + addZ;

		xzCords[0] = x;
		xzCords[1] = z;

		return xzCords;
	}

	public static double[] getXZCordsMultipliersFromDegree(double degree) {
		double radian = Math.toRadians(degree);

		double xMultiplier = Math.cos(radian);
		double zMultiplier = Math.sin(radian);

		double[] xzCords = new double[2];

		xzCords[0] = xMultiplier;
		xzCords[1] = zMultiplier;

		return xzCords;
	}

	public static double[] getXZCordsFromDegree(Location loc, boolean rotated, double addAngle, double radius,
			double degree) {
		double radian = Math.toRadians(degree);

		double xMultiplier = Math.cos(radian);
		double zMultiplier = Math.sin(radian + addAngle);

		double addX = xMultiplier * radius;
		double addZ = zMultiplier * radius;

		double[] xzCords = new double[2];

		double x = loc.getX();
		double z = loc.getZ();

		if (rotated) {
			x += addX;
			z -= addZ;
		} else {
			x += addX;
			z += addZ;
		}

		xzCords[0] = x;
		xzCords[1] = z;

		return xzCords;
	}

	public static double getYCordsMultiplierByPitch(double pitchdegree) {
		return Math.sin(Math.toRadians(-pitchdegree));
	}

	public static Location getMidPoint(Location loc1, Location loc2) {
		Location midpoint = loc1.clone();

		double midx = (loc1.getX() + loc2.getX()) / 2;
		double midy = (loc1.getY() + loc2.getY()) / 2;
		double midz = (loc1.getZ() + loc2.getZ()) / 2;

		midpoint.setX(midx);
		midpoint.setY(midy);
		midpoint.setZ(midz);

		return midpoint;
	}

	public static Location highestLocation(Location locat) {
		double blockY = locat.getBlockY();

		Location tplocation = null;
		for (int i = (int) blockY; i > 1; i--) {
			Location loc = new Location(locat.getWorld(), locat.getX(), i, locat.getZ(), locat.getYaw(),
					locat.getPitch());

			if (loc.getBlock().getType().isSolid()) {
				tplocation = new Location(locat.getWorld(), locat.getX(), i + 1.5, locat.getZ(), locat.getYaw(),
						locat.getPitch());
				break;
			}
		}

		return tplocation;
	}

	public static ArrayList<Location> circleLocations(Location loc, double radius) {
		ArrayList<Location> cl = new ArrayList<Location>();

		for(int degree=0; degree<=360; degree++) {

			double x = getXZCordsFromDegree(loc, radius, degree)[0];
			double z = getXZCordsFromDegree(loc, radius, degree)[1];

			cl.add(new Location(loc.getWorld(), x, loc.getY(), z));
		}

		return cl;
	}

	public static ArrayList<Location> circleLocations(Location loc, double radius, int iteratecount) {
		ArrayList<Location> cl = new ArrayList<Location>();

		for(int degree=0; degree<=360; degree += iteratecount) {

			double x = getXZCordsFromDegree(loc, radius, degree)[0];
			double z = getXZCordsFromDegree(loc, radius, degree)[1];

			cl.add(new Location(loc.getWorld(), x, loc.getY(), z));
		}

		return cl;
	}

	public static ArrayList<Location> circleLocations(Location loc, double radius, int iteratecount, double y) {
		ArrayList<Location> cl = new ArrayList<Location>();

		for(int degree=0; degree<=360; degree += iteratecount) {

			double x = getXZCordsFromDegree(loc, radius, degree)[0];
			double z = getXZCordsFromDegree(loc, radius, degree)[1];

			cl.add(new Location(loc.getWorld(), x, y, z));
		}

		return cl;
	}

	public static ArrayList<Location> circleLocations(Location loc, double radius, double y) {
		ArrayList<Location> cl = new ArrayList<Location>();

		for(int degree=0; degree<=360; degree++) {

			double x = getXZCordsFromDegree(loc, radius, degree)[0];
			double z = getXZCordsFromDegree(loc, radius, degree)[1];

			cl.add(new Location(loc.getWorld(), x, y, z));
		}

		return cl;
	}
}
