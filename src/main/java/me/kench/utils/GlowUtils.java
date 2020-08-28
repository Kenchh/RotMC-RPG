package me.kench.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class GlowUtils {

    public static void clearGlow(Player p) {
        p.getScoreboard().getTeam("").removeEntry(p.getName());
    }

    public static void setGlow(Player p, ChatColor c) {
        p.getScoreboard().getTeam("").removeEntry(p.getName());
    }

    public static ChatColor getCurrentGlow(Player p) {

        if(p.getScoreboard() == null) return null;

        for(Team t : p.getScoreboard().getTeams()) {

        }

        return null;
    }

}
