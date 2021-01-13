package me.kench.rotmc.utils;

import me.glaremasters.guilds.Guilds;
import me.glaremasters.guilds.guild.Guild;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.database.playerdata.PlayerData;
import me.kench.rotmc.database.playerdata.PlayerDataDam;
import me.kench.rotmc.player.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GlowUtils {
    /**
     * Gets the current {@link GlowType} of the given {@link Player}, or null if the player is not glowing.
     *
     * @param targetPlayer the target player
     * @return the {@link ChatColor} describing the glow, or null if the player is not glowing
     */
    public static GlowType getGlowType(Player targetPlayer) {
        for (Team team : targetPlayer.getScoreboard().getTeams()) {
            if (team.hasEntry(targetPlayer.getName())) {
                return GlowType.getByGlowColor(ChatColor.getByChar(team.getName().replace("GLOW_COLOR_§", "")));
            }
        }

        return null;
    }

    /**
     * Sets the glow of the given {@link Player} to the glow color specified by the given {@link ChatColor}.
     *
     * @param targetPlayer the target player
     * @param glowColor    the desired glow color
     */
    public static void setGlow(Player targetPlayer, ChatColor glowColor) {
        clearGlow(targetPlayer);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Team team = onlinePlayer.getScoreboard().getTeam("GLOW_COLOR_" + glowColor.toString());

            if (team == null) {
                team = onlinePlayer.getScoreboard().registerNewTeam("GLOW_COLOR_" + glowColor.toString());
            }

            team.addEntry(targetPlayer.getName());
            team.setDisplayName(glowColor + ChatColor.BOLD.toString() + glowColor);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setColor(glowColor);
        }

        targetPlayer.setGlowing(true);
    }

    /**
     * Checks whether the given {@link Player}'s current glow color, if any, is still permitted.
     *
     * @param data the given Player's {@link PlayerData}
     * @return true or false
     * @apiNote This should ONLY be called from an async context. Sync contexts will cause too much lag.
     * @implNote If not called from an async context, this method will throw an exception. Seriously. Async.
     */
    public static boolean checkPlayerGlowPermitted(PlayerData data) {
        // TODO: this is really heavy and not desired to run every 3 seconds for every player online. But I tried to fix it a little bit for now.

        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("Tried to call GlowUtils#checkPlayerGlowPermitted from the main game thread. This is a bug.");
        }

        PlayerDataDam dam = RotMcPlugin.getInstance().getDataManager().getPlayerData();
        Set<PlayerClass> topClasses = dam.loadTop10Classes().keySet();
        Set<Guild> topGuilds = dam.loadTop10Guilds().keySet();

        return isPermitted(data, getGlowType(data.getPlayer()), topClasses, topGuilds);
    }

    /**
     * Clears the glow of the given {@link Player}.
     *
     * @param targetPlayer the target player
     */
    public static void clearGlow(Player targetPlayer) {
        if (targetPlayer.isGlowing()) {
            targetPlayer.setGlowing(false);
            if (getGlowType(targetPlayer) != null) {
                targetPlayer.getScoreboard().getTeam("GLOW_COLOR_" + getGlowType(targetPlayer).toString()).removeEntry(targetPlayer.getName());
            }
        }
    }

    /**
     * Checks that a {@link Player} is permitted to "glow" with the specified {@link ChatColor}.
     *
     * @param playerData the given Player
     * @param glowType   the given ChatColor
     * @return true if color not specified, player has color override permission, or checks pass; false otherwise.
     * @apiNote This should ONLY be called from an async context. Sync contexts will cause too much lag.
     * @implNote If not called from an async context, this method will throw an exception. Seriously. Async.
     */
    public static boolean isPermitted(PlayerData playerData, GlowType glowType, Set<PlayerClass> topClasses, Set<Guild> topGuilds) {
        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("Tried to call GlowUtils#isPermitted from the main game thread. This is a bug.");
        }

        if (glowType == null) {
            return true;
        }

        if (playerData.getPlayer().hasPermission(glowType.getPermission())) {
            return true;
        }

        if (!playerData.hasSelectedClass()) {
            return false;
        }

        PlayerClass selectedClass = playerData.getSelectedClass();

        List<GlowType> topClassColors = Arrays.asList(GlowType.TOP_FIVE_CHARACTER_GLOW, GlowType.TOP_CHARACTER_GLOW);
        List<GlowType> topGuildColors = Arrays.asList(GlowType.TOP_THREE_GUILD_GLOW, GlowType.TOP_GUILD_GLOW);

        if (topClassColors.contains(glowType)) {
            List<PlayerClass> topFiveClasses = topClasses.stream().limit(5).collect(Collectors.toList());

            switch (glowType) {
                case TOP_FIVE_CHARACTER_GLOW:
                    // Is class in top five fame?
                    return topFiveClasses.stream().anyMatch(clazz -> clazz.getUniqueId().equals(selectedClass.getUniqueId()));
                case TOP_CHARACTER_GLOW:
                    // Is class the number one fame?
                    return topFiveClasses.get(0).getUniqueId().equals(selectedClass.getUniqueId());
                default:
                    return false;
            }
        } else if (topGuildColors.contains(glowType)) {
            Guild memberGuild = Guilds.getApi().getGuild(playerData.getPlayer());
            if (memberGuild == null) {
                return false;
            }

            List<Guild> topThreeGuilds = topGuilds.stream().limit(3).collect(Collectors.toList());

            switch (glowType) {
                case TOP_THREE_GUILD_GLOW:
                    // Is member's guild in top three guilds?
                    return topThreeGuilds.stream().anyMatch(guild -> guild.getId().equals(memberGuild.getId()));
                case TOP_GUILD_GLOW:
                    // Is member's guild the top guild?
                    return topThreeGuilds.get(0).getId().equals(memberGuild.getId());
                default:
                    return false;
            }
        }

        return false;
    }
}