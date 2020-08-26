package me.reykench.items;

import me.reykench.RotMC;
import me.reykench.items.stats.Essence;
import me.reykench.items.stats.Gem;
import me.reykench.items.stats.Rune;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Socketing {

    public Socketing (Player p, GameItem gameItem, GemItem gem) {
        int percentage = new Random().nextInt(100) + 1;

        if(percentage <= gem.getSuccessChance()) {
            /** success */
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 1F);
            gameItem.getStats().gemsockets--;
            gameItem.getStats().gems.add(new Gem(gem.getGem().getType(), gem.getGem().getLevel()));
            p.sendMessage(ChatColor.GREEN + "Your item has successfully been upgraded!");
            gameItem.update(RotMC.getPlayerData(p).getMainClass());
        } else {
            /** fail */
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
            gameItem.getItem().setAmount(gameItem.getItem().getAmount() - 1);
            p.sendMessage(ChatColor.RED + "Item upgrade failed.");
        }

    }

    public Socketing(Player p, GameItem gameItem, RuneItem rune) {
        int percentage = new Random().nextInt(100) + 1;

        if(percentage <= rune.getSuccessChance()) {
            /** success */
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 1F);
            gameItem.getStats().hasRuneSocket = false;
            gameItem.getStats().setRune(new Rune(rune.getRune().getType()));
            p.sendMessage(ChatColor.GREEN + "Your item has successfully been upgraded!");
            gameItem.update(RotMC.getPlayerData(p).getMainClass());
        } else {
            /** fail */
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
            gameItem.getItem().setAmount(gameItem.getItem().getAmount() - 1);
            p.sendMessage(ChatColor.RED + "Item upgrade failed.");
        }
    }

    public Socketing(Player p, GameItem gameItem, EssenceItem essence) {

        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 1F);
        gameItem.getStats().hasEssenceSocket = false;
        gameItem.getStats().setEssence(new Essence(essence.getEssence().getType()));
        p.sendMessage(ChatColor.GREEN + "Your item has successfully been upgraded!");
        gameItem.update(RotMC.getPlayerData(p).getMainClass());

    }

}
