package me.kench.gui.glow.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.database.playerdata.PlayerData;
import me.kench.items.ItemBuilder;
import me.kench.utils.GlowType;
import me.kench.utils.GlowUtils;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import static me.kench.utils.TextUtils.parseMini;

public class GlowGuiGlowButton extends GuiItem {
    public GlowGuiGlowButton(PlayerData data, GlowType glowType, boolean permitted) {
        super(
                ItemBuilder.create(glowType.getMaterial())
                        .name(glowType.getDisplayName())
                        .lore(
                                parseMini(String.format("**%s**", permitted ? "<green>UNLOCKED" : "<red>LOCKED")),
                                parseMini("<yellow>Left-Click to select glow."),
                                parseMini("<yellow>Right-Click to <red>remove <yellow>glow.")
                        )
                        .applyMeta(meta -> {
                            GlowType currentPlayerGlow = GlowUtils.getGlowType(data.getPlayer());
                            if (currentPlayerGlow != null && currentPlayerGlow == glowType) {
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                            }
                        })
                        .build(),
                event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();
                    GlowType currentPlayerGlow = GlowUtils.getGlowType(data.getPlayer());

                    if (!permitted) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
                        return;
                    }

                    if (event.getClick() == ClickType.RIGHT && glowType == currentPlayerGlow) {
                        GlowUtils.clearGlow(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1F, 1F);
                    } else if (event.getClick() == ClickType.LEFT && glowType != currentPlayerGlow) {
                        GlowUtils.setGlow(player, glowType.getGlowColor());
                        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1.5F);
                    }
                }
        );
    }
}
