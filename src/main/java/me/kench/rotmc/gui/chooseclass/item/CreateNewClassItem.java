package me.kench.rotmc.gui.chooseclass.item;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.gui.createclass.CreateClassGui;
import me.kench.rotmc.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CreateNewClassItem extends GuiItem {
    public CreateNewClassItem() {
        super(
                ItemBuilder.create(Material.WHITE_STAINED_GLASS).amount(1).name(ChatColor.GREEN + "" + ChatColor.BOLD + "Create Class").build(),
                event -> {
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();

                    for (int i = 0; i < 3; i++) {
                        player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
                    }

                    RotMcPlugin.getInstance().getDataManager().getPlayerData()
                            .chainLoadSafe(player.getUniqueId())
                            .syncLast(data -> new CreateClassGui().display(player))
                            .execute();
                }
        );
    }
}
