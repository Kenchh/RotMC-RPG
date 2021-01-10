package me.kench.utils;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.entity.Player;

public class WorldGuardUtils {
    public static boolean notInPvpRegion(Player player) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        return !WorldGuard.getInstance().getPlatform().getRegionContainer()
                .createQuery()
                .getApplicableRegions(localPlayer.getLocation())
                .testState(localPlayer, Flags.PVP);
    }
}