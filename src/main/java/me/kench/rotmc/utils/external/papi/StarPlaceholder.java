package me.kench.rotmc.utils.external.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.utils.RankUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StarPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "rotmcstar";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Kench";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("color")) {
            // unfortunately sync, but data should already be cached
            return ChatColor.GRAY.toString() + RankUtils.getStarColor(RotMcPlugin.getInstance().getDataManager().getPlayerData().load(player.getUniqueId())) + "";
        }

        return "";
    }
}
