package me.kench.rotmc.player.stat;

import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.player.stat.view.StatView;
import me.kench.rotmc.utils.ItemUtils;
import me.kench.rotmc.utils.StatUtils;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerStats {
    private final PlayerClass playerClass;
    private final Stats potionStats;

    public PlayerStats(PlayerClass playerClass, Stats potionStats) {
        this.playerClass = playerClass;
        this.potionStats = potionStats;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public Player getPlayer() {
        return getPlayerClass().getPlayer();
    }

    public Stats getPotionStats() {
        return potionStats;
    }

    public Stats getItemStats() {
        return ItemUtils.getItemStatsFromEquipment(getPlayer());
    }

    public Stats getGemStats() {
        return ItemUtils.getGemStatsFromEquipment(getPlayer());
    }

    public Stats getOverallStats() {
        return getPotionStats().clone().merge(getItemStats(), getGemStats());
    }

    /**
     * Gets an {@link ItemStack} with lore that describes a specific {@link Stat}
     * for the {@link Player}
     *
     * @param stat the stat to get an {@link ItemStack} for
     * @return the {@link ItemStack}
     */
    public ItemStack getDescriptiveItemFor(Stat stat) {
        ItemBuilder builder = ItemBuilder.create(Material.CARROT_ON_A_STICK);

        StatView potionView = getPotionStats().getStat(stat), itemView = getItemStats().getStat(stat), gemView = getGemStats().getStat(stat);
        StatView overallView = potionView.merge(itemView, gemView);

        int modelData = -1;
        String contextColor = "",
                nameFormat = "%s**%s**<gray>. <white>+%s",
                fromPotions = "<gray>From Potions: (<yellow>%d<gray>/<yellow>%d<gray>) = %s%.1f",
                fromItemStats = "<gray>From Item Stats: %s%.1f",
                fromGems = "<gray>From Gems: %s%.1f";

        switch (stat) {
            case HEALTH:
                modelData = 205;
                contextColor = "<red>";
                break;
            case ATTACK:
                modelData = 201;
                contextColor = "<aqua>";
                break;
            case DEFENSE:
                modelData = 203;
                contextColor = "<blue>";
                break;
            case SPEED:
                modelData = 202;
                contextColor = "<green>";
                break;
            case EVASION:
                modelData = 204;
                contextColor = "<yellow>";
                break;
            case VITALITY:
                modelData = 206;
                contextColor = "<yellow>";
                break;
        }

        return builder
                .name(TextUtils.parseMini(String.format(
                        nameFormat,
                        contextColor,
                        stat.getName(),
                        overallView.getDisplayValue()
                )))
                .lore(
                        TextUtils.parseMini(String.format(
                                fromPotions,
                                potionView.getStatPoints(),
                                StatUtils.getCap(
                                        getPlayerClass().getRpgClass(),
                                        stat
                                ),
                                contextColor,
                                potionView.getDisplayValue()
                        )),
                        TextUtils.parseMini(String.format(
                                fromItemStats,
                                contextColor,
                                itemView.getDisplayValue()
                        )),
                        TextUtils.parseMini(String.format(
                                fromGems,
                                contextColor,
                                gemView.getDisplayValue()
                        ))
                )
                .modelData(modelData)
                .build();

    }

}
