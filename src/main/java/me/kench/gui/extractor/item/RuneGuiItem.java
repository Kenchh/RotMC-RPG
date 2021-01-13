package me.kench.gui.extractor.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.items.GameItem;
import me.kench.items.ItemBuilder;
import me.kench.items.RuneItem;
import me.kench.items.stats.Rune;
import me.kench.utils.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class RuneGuiItem extends GuiItem {
    public RuneGuiItem(GameItem givenItem, Rune givenRune) {
        super(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(String.format("%s%s", givenRune.getType().getPrefix(), givenRune.getType().getName()))
                        .modelData(givenRune.getType().getModelData())
                        .build(),
                event -> {
                    event.setCancelled(true);

                    givenItem.setItem(
                            ItemBuilder.of(givenItem.getItem())
                                    .lore(
                                            ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 50%",
                                            ChatColor.GRAY + "▸ Permanent Effect:" + ChatColor.GOLD + " ZZZ"
                                    )
                                    .build()
                    );

                    RuneItem runeItem = new RuneItem(givenItem.getItem());
                    runeItem.setSuccessChance(50);
                    runeItem.update();

                    Rune rune = runeItem.getRune();

                    givenItem.getStats().setRune(null);
                    givenItem.getStats().setHasRuneSocket(false);
                    givenItem.update();

                    Player player = (Player) event.getWhoClicked();

                    for (ItemStack it : player.getInventory().getContents()) {
                        if (!it.hasItemMeta() || !Objects.requireNonNull(it.getItemMeta()).hasDisplayName()) continue;
                        if (it.getItemMeta().getDisplayName().contains("Extractor")) {
                            it.setAmount(it.getAmount() - 1);
                            break;
                        }
                    }

                    Messaging.sendMessage(player, String.format("<green>You have successfully extracted %s%s!", rune.getType().getPrefix(), rune.getType().getName()));
                    player.playSound(player.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
                    player.getInventory().addItem(runeItem.getItem());
                    player.closeInventory();
                }
        );
    }
}
