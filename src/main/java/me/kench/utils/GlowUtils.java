package me.kench.utils;

import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.api.GuildsAPI;
import me.glaremasters.guilds.guild.Guild;
import me.kench.RotMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlowUtils {

    public static ChatColor getGlowColor(Player p) {
        for (Team t : p.getScoreboard().getTeams()) {
            if (t.hasEntry(p.getName())) {
                return ChatColor.getByChar(t.getName().replace("GLOW_COLOR_§", ""));
            }
        }
        return null;
    }

    public static void clearGlow(Player p) {
        if (p.isGlowing()) {
            p.setGlowing(false);
            if (getGlowColor(p) != null) {
                p.getScoreboard().getTeam("GLOW_COLOR_" + getGlowColor(p).toString()).removeEntry(p.getName());
            }
        }
    }

    public static void setGlow(Player p, ChatColor c) {

        clearGlow(p);

        for (Player pp : Bukkit.getOnlinePlayers()) {
            Team team = pp.getScoreboard().getTeam("GLOW_COLOR_" + c.toString());
            if (team == null) {
                team = pp.getScoreboard().registerNewTeam("GLOW_COLOR_" + c.toString());
            }
            team.addEntry(p.getName());
            team.setDisplayName(c + ChatColor.BOLD.toString() + c);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setColor(c);
        }
        p.setGlowing(true);
    }

    public static boolean isPermitted(Player p, ChatColor c) {

        if (c == null) return false;

        if (p.hasPermission("rotmc.glow." + c.name().toLowerCase().replace("_", ""))) return true;

        ArrayList<ChatColor> topFame = new ArrayList<>();
        topFame.add(ChatColor.GREEN);
        topFame.add(ChatColor.DARK_GREEN);

        ArrayList<ChatColor> topGuildss = new ArrayList<>();
        topGuildss.add(ChatColor.LIGHT_PURPLE);
        topGuildss.add(ChatColor.DARK_PURPLE);

        if (!topFame.contains(c) && !topGuildss.contains(c)) {
            if (!p.hasPermission("rotmc.glow." + c.name().toLowerCase().replace("_", ""))) {
                return false;
            } else {
                return true;
            }
        } else {

            if (topFame.contains(c)) {
                HashMap<Integer, List<String>> topClasses = RotMC.getInstance().getSqlManager().getTopClasses();
                if (c == ChatColor.GREEN) {
                    boolean allow = false;
                    for (int i : topClasses.keySet()) {

                        if (topClasses.get(i).size() <= 1 || i > 5) break;

                        List<String> data = topClasses.get(i);
                        if (data != null && data.get(0) != null && data.get(0).equalsIgnoreCase(p.getName())) {
                            allow = true;
                            break;
                        }
                    }

                    return allow;
                } else if (c == ChatColor.DARK_GREEN) {
                    if (!topClasses.isEmpty()) {
                        if (topClasses.get(1) != null && topClasses.get(1).get(0) != null && topClasses.get(1).get(0).equalsIgnoreCase(p.getName())) {
                            return true;
                        }
                    }
                }
            } else if (topGuildss.contains(c)) {

                HashMap<Integer, List<String>> topGuilds = RotMC.getInstance().getSqlManager().getTopGuilds();
                if (c == ChatColor.LIGHT_PURPLE) {

                    GuildsAPI guildsAPI = Guilds.getApi();
                    Guild g = guildsAPI.getGuild(p);

                    if (g == null || g.getName() == null) return false;

                    boolean allow = false;
                    for (int i : topGuilds.keySet()) {

                        if (topGuilds.get(i).size() <= 1 || i > 3) break;

                        List<String> data = topGuilds.get(i);
                        if (data != null && data.get(0) != null && data.get(0).equalsIgnoreCase(g.getName())) {
                            allow = true;
                            break;
                        }
                    }

                    return allow;
                } else if (c == ChatColor.DARK_PURPLE) {

                    GuildsAPI guildsAPI = Guilds.getApi();
                    Guild g = guildsAPI.getGuild(p);

                    if (g == null || g.getName() == null) return false;

                    if (!topGuilds.isEmpty()) {

                        if (topGuilds.get(1) != null && topGuilds.get(1).get(0) != null && topGuilds.get(1).get(0).equalsIgnoreCase(g.getName())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void clearWhenForbidden(Player p) {
        ChatColor c = getGlowColor(p);

        if (!isPermitted(p, c)) {
            clearGlow(p);
            return;
        }
    }

    public static ChatColor getColorFromMaterial(Material m) {
        switch (m) {
            case ORANGE_DYE:
                return ChatColor.GOLD;
            case GRAY_DYE:
                return ChatColor.GRAY;

            case YELLOW_DYE:
                return ChatColor.YELLOW;

            case WHITE_DYE:
                return ChatColor.WHITE;

            case LIGHT_BLUE_DYE:
                return ChatColor.AQUA;


            case LIME_DYE:
                return ChatColor.GREEN;

            case GREEN_DYE:
                return ChatColor.DARK_GREEN;

            case PINK_DYE:
                return ChatColor.LIGHT_PURPLE;

            case PURPLE_DYE:
                return ChatColor.DARK_PURPLE;


            case LAPIS_LAZULI:
                return ChatColor.BLUE;

            case BLACK_DYE:
                return ChatColor.BLACK;

        }

        return ChatColor.RED;
    }

}
