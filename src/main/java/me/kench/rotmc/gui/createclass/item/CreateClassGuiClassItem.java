package me.kench.rotmc.gui.createclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.database.playerdata.PlayerData;
import me.kench.rotmc.items.ItemBuilder;
import me.kench.rotmc.player.PlayerClass;
import me.kench.rotmc.player.RpgClass;
import me.kench.rotmc.utils.Messaging;
import me.kench.rotmc.utils.RankUtils;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CreateClassGuiClassItem extends GuiItem {
    public CreateClassGuiClassItem(RpgClass rpgClass, PlayerData data) {
        super(
                ItemBuilder.create(Material.CARROT_ON_A_STICK)
                        .name(TextUtils.parseMini(String.format(
                                "<yellow>**%s** <white>%s/5",
                                rpgClass.getName(),
                                RankUtils.getCharacterRank(data, rpgClass))
                        ))
                        .modelData(rpgClass.getCustomModelData())
                        .build(),
                event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    RotMcPlugin.newChain()
                            .sync(() -> {
                                Messaging.sendMessage(player, String.format("<green>You have selected %s!", rpgClass.getName()));

                                PlayerClass newClass = new PlayerClass(data.getUniqueId(), rpgClass);
                                data.getClasses().add(newClass);
                                data.changeSelectedClass(newClass.getUniqueId(), true);

                                for (int i = 0; i < 3; i++) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1.2F);
                                }

                                player.closeInventory();
                            })
                            .execute();
                }
        );
    }
}
