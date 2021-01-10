package me.kench.gui.createclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.items.ItemBuilder;
import me.kench.player.PlayerClass;
import me.kench.player.RpgClass;
import me.kench.utils.Messaging;
import me.kench.utils.RankUtils;
import me.kench.utils.TextUtils;
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

                    RotMC.newChain()
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
