package me.kench.gui.deleteclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.RotMC;
import me.kench.gui.chooseclass.ChooseClassGui;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.utils.Messaging;
import me.kench.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DeleteClassConfirmGuiConfirmButton extends GuiItem {
    public DeleteClassConfirmGuiConfirmButton(PlayerClass playerClass) {
        super(
                ItemBuilder.create(Material.EMERALD_BLOCK).name(TextUtils.parseMini("<green>**CONFIRM**")).build(),
                event -> {
                    event.setCancelled(true);

                    RotMC.getInstance().getDataManager().getPlayerData()
                            .chainLoadSafe(playerClass.getUniqueId())
                            .delay(1)
                            .syncLast(data -> {
                                Player player = data.getPlayer();
                                if (player == null) return;

                                if (data.getSelectedClass().equals(playerClass)) {
                                    Messaging.sendMessage(player, "<red>You need to switch to another profile to delete your current one!");
                                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.2F);
                                    new ChooseClassGui().display(player);
                                    return;
                                }

                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " " + playerClass.getFame());
                                data.getClasses().removeIf(clazz -> clazz.getUniqueId().equals(playerClass.getUniqueId()));
                                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 0.75F);
                                new ChooseClassGui().display(player);
                            })
                            .execute();
                }
        );
    }
}
