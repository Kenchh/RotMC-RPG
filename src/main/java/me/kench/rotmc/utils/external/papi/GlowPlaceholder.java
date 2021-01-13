package me.kench.rotmc.utils.external.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kench.rotmc.utils.GlowUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlowPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "rotmcglow";
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
        if (params.equalsIgnoreCase("glowcolor")) {
            if (GlowUtils.getGlowType(player) == null) {
                return "";
            }

            return GlowUtils.getGlowType(player) + "";
        }

        return "";
    }
}
