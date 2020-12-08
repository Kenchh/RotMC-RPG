package me.kench.gui.items;

import me.kench.RotMC;
import me.kench.player.PlayerClass;
import me.kench.utils.RankUtils;
import me.kench.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PlayerClassItem extends ItemStack {

    public PlayerClassItem(PlayerClass playerClass) {
        this.setType(Material.CARROT_ON_A_STICK);
        this.setAmount(1);

        ItemMeta meta = this.getItemMeta();
        meta.setCustomModelData(0);

        String selected = "";

        if(RotMC.getPlayerData(playerClass.getPlayer()).getMainClass().getUuid().equals(playerClass.getUuid())) {
            selected = ChatColor.GREEN + "" + ChatColor.BOLD + " SELECTED";
        }

        meta.setDisplayName(ChatColor.YELLOW + playerClass.getData().getName() + " " + ChatColor.GOLD + "" + playerClass.getLevel() + selected);
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "" + TextUtils.getDecimalFormat().format(playerClass.getXp()) + " Fame "
                        + ChatColor.WHITE + RankUtils.getCharacterRank(playerClass.getPlayer(), playerClass.getData().getName()) + "/5",
                ChatColor.YELLOW + "Left-Click" + ChatColor.GRAY + " to select profile.",
                ChatColor.YELLOW + "Right-Click" + ChatColor.GRAY + " to delete profile."
        ));

        switch (playerClass.getData().getName()) {
            case "Knight":
                meta.setCustomModelData(151);
                break;
            case "Necromancer":
                meta.setCustomModelData(101);
                break;
            case "Warrior":
                meta.setCustomModelData(141);
                break;
            case "Huntress":
                meta.setCustomModelData(111);
                break;
            case "Assassin":
                meta.setCustomModelData(121);
                break;
            case "Rogue":
                meta.setCustomModelData(131);
                break;
        }

        this.setItemMeta(meta);
    }

}
