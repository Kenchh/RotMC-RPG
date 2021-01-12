package me.kench.gui.extractor.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.GameItem;
import me.kench.items.GemItem;
import me.kench.items.ItemBuilder;
import me.kench.items.stats.Gem;
import me.kench.utils.Messaging;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GemGuiItem extends GuiItem {
    public GemGuiItem(GameItem givenItem, Gem givenGem) {
        super(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(String.format("%s%s %s", givenGem.getType().getPrefix(), givenGem.getType().getName(), TextUtils.getRomanFromNumber(givenGem.getLevel())))
                        .modelData(givenGem.getType().getModelData(givenGem.getLevel()))
                        .build(),
                event -> {
                    event.setCancelled(true);

                    givenItem.setItem(
                            ItemBuilder.of(givenItem.getItem())
                                    .lore(
                                            ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 50%",
                                            ChatColor.GRAY + "▸ YYY:" + ChatColor.GOLD + " ZZZ"
                                    )
                                    .build()
                    );

                    GemItem gemItem = new GemItem(givenItem.getItem());
                    gemItem.successChance = 50;
                    gemItem.update();

                    givenItem.getStats().getGems().remove(event.getSlot() - 10);
                    givenItem.getStats().incrementGemSockets();
                    givenItem.update();

                    Player player = (Player) event.getWhoClicked();

                    for (ItemStack itemStack : player.getInventory().getContents()) {
                        if (!itemStack.hasItemMeta() || !Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName()) continue;
                        if (itemStack.getItemMeta().getDisplayName().contains("Extractor")) {
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            break;
                        }
                    }

                    Gem gem = gemItem.getGem();

                    Messaging.sendMessage(player, String.format("<green>You have successfully extracted %s%s %s!", gem.getType().getPrefix(), gem.getType().getName(), TextUtils.getRomanFromNumber(gem.getLevel())));
                    player.playSound(player.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
                    player.getInventory().addItem(gemItem.getItem());
                    player.closeInventory();
                }
        );
    }
}
