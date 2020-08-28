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

}
