package me.kench.gui.items;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.kench.RotMC;
import me.kench.gui.CreateClassGUI;
import me.kench.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CreateClassItem extends GuiItem {
    public CreateClassItem() {
        super(
                ItemBuilder.create(Material.WHITE_STAINED_GLASS).amount(1).name(ChatColor.GREEN + "" + ChatColor.BOLD + "Create Class").build(),
                event -> {
                    Player player = (Player) event.getWhoClicked();
                    for (int i = 0; i < 3; i++) {
                        player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 1.5F);
                    }

                    RotMC.getInstance().getDataManager().getAccessor().getPlayerData()
                            .loadSafe(player.getUniqueId())
                            .syncLast(data -> player.openInventory(new CreateClassGUI(data).getInv()))
                            .execute();
                }
        );
    }
}
