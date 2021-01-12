package me.kench.gui.chooseclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.database.playerdata.PlayerData;
import me.kench.gui.deleteclass.DeleteClassConfirmGui;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.utils.Messaging;
import me.kench.utils.RankUtils;
import me.kench.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class PlayerClassItem extends GuiItem {
    public PlayerClassItem(PlayerData playerData, PlayerClass playerClass) {
        super(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(
                                TextUtils.parseMini(String.format(
                                        "<yellow>%s <gold>%d <green>**%s**",
                                        playerClass.getRpgClass().getName(),
                                        playerClass.getLevel(),
                                        playerData.getSelectedClass().getUniqueId().equals(playerClass.getUniqueId()) ? "SELECTED" : ""
                                ))
                        )
                        .lore(
                                TextUtils.parseMini(String.format(
                                        "<gray>%s Fame <white>%d/5",
                                        TextUtils.getDecimalFormat().format(playerClass.getFame()),
                                        RankUtils.getCharacterRank(playerData, playerClass.getRpgClass())
                                )),
                                TextUtils.parseMini("<yellow>Left-Click <gray>to select profile."),
                                TextUtils.parseMini("<yellow>Right-Click <gray>to delete profile.")
                        )
                        .modelData(playerClass.getRpgClass().getCustomModelData())
                        .build(),
                event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    if (event.getClick() == ClickType.RIGHT) {
                        new DeleteClassConfirmGui().display(player, playerClass);

                        for (int i = 0; i < 3; i++) {
                            player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
                        }

                        return;
                    }

                    if (!playerClass.getUniqueId().equals(playerData.getSelectedClass().getUniqueId())) {
                        playerData.changeSelectedClass(playerClass.getUniqueId(), false);

                        for (int i = 0; i < 3; i++) {
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
                        }

                        Messaging.sendMessage(player, String.format("<green>You have selected <gold>%d %s<green>!", playerClass.getLevel(), playerClass.getRpgClass().getName()));
                        player.closeInventory();
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                        Messaging.sendMessage(player, "<red>You already have this class selected!");
                    }
                }
        );
    }
}
