package me.kench.rotmc.gui.extractor.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.items.EssenceItem;
import me.kench.rotmc.items.GameItem;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.items.stats.Essence;
import me.kench.rotmc.utils.Messaging;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EssenceGuiItem extends GuiItem {
    public EssenceGuiItem(GameItem givenItem, Essence givenEssence) {
        super(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(String.format("%s%s", givenEssence.getType().getPrefix(), givenEssence.getType().getName()))
                        .modelData(givenEssence.getType().getModelData())
                        .build(),
                event -> {
                    event.setCancelled(true);

                    givenItem.setItem(
                            ItemBuilder.of(givenItem.getItem())
                                    .lore(
                                            ChatColor.GRAY + "Success Chance:" + ChatColor.GOLD + " 100%",
                                            ChatColor.GRAY + "▸ Vanity Effect:" + ChatColor.GOLD + " ZZZ"
                                    )
                                    .build()
                    );

                    EssenceItem essenceItem = new EssenceItem(givenItem.getItem());
                    essenceItem.update();

                    givenItem.getStats().setEssence(null);
                    givenItem.getStats().setHasEssenceSocket(false);
                    givenItem.update();

                    Player player = (Player) event.getWhoClicked();

                    for (ItemStack it : player.getInventory().getContents()) {
                        if (!it.hasItemMeta() || !Objects.requireNonNull(it.getItemMeta()).hasDisplayName()) continue;
                        if (it.getItemMeta().getDisplayName().contains("Extractor")) {
                            it.setAmount(it.getAmount() - 1);
                            break;
                        }
                    }

                    Essence essence = essenceItem.getEssence();

                    Messaging.sendMessage(player, String.format("<green>You have successfully extracted %s%s!", essence.getType().getPrefix(), essence.getType().getName()));
                    player.playSound(player.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1F, 0.8F);
                    player.getInventory().addItem(essenceItem.getItem());
                    player.closeInventory();
                }
        );
    }
}
