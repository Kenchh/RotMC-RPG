package me.kench.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kench.utils.GlowUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlowPlaceHolder extends PlaceholderExpansion {
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

        if(params.equalsIgnoreCase("glowcolor")) {

            if(GlowUtils.getGlowColor(player) == null) return "";

            return GlowUtils.getGlowColor(player) + "";
        }

        return "";
    }
}
