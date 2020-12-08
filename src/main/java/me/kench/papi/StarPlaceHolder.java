package me.kench.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kench.RotMC;
import me.kench.player.PlayerData;
import me.kench.utils.GlowUtils;
import me.kench.utils.RankUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StarPlaceHolder extends PlaceholderExpansion {
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

        if(params.equalsIgnoreCase("color")) {
            PlayerData pd = RotMC.getPlayerData(player);
            return RankUtils.getStarColor(pd) + "";
        }

        return "";
    }
}
