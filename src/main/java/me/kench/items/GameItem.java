package me.kench.items;

import me.kench.game.GameClass;
import me.kench.items.stats.Gem;
import me.kench.player.PlayerClass;
import me.kench.utils.ItemUtils;
import me.kench.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameItem {

    private ItemStack item;
    private ArrayList<GameClass> gameClasses = new ArrayList<>();
    private int level;

    private ItemStats stats;

    public GameItem(ItemStack item) {
        this.item = item;
        stats = new ItemStats(this);

        ItemMeta meta = item.getItemMeta();

        if(meta.hasDisplayName() && ItemUtils.isGem(meta.getDisplayName())) {
            return;
        }

        if(meta.hasDisplayName() && ItemUtils.isRune(meta.getDisplayName())) {
            return;
        }

        if(meta.hasDisplayName() && ItemUtils.isEssence(meta.getDisplayName())) {
            return;
        }

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();

        for(String s : lore) {

            if(s.contains("Class:")) {
                String classes = s.replace("§eClass:§f ", "");

                for(String gameClass : classes.split(", ")) {
                    gameClasses.add(new GameClass(gameClass));
                }
            }

            if(s.contains("Level:")) {
                String lvl = TextUtils.getLastNumber(s, 0);
                level = Integer.parseInt(lvl.replace("+", ""));
            }

            checkForStats(s);

        }

        for(int i=1;i<=lore.size(); i++) {
            String line = lore.get(lore.size() - i);

            if(line.contains("Essence Socket")) {
                stats.hasEssenceSocket = true;
                continue;
            }

            if(line.contains("Rune Socket")) {
                stats.hasRuneSocket = true;
                continue;
            }

            if(line.contains("Gem Socket")) {
                stats.gemsockets++;
                continue;
            }
        }

        stats.stats = ItemUtils.getItemStatsByLore(lore);
    }

    private void checkForStats(String s) {

        if(ItemUtils.isEssence(s))
            stats.setEssence(ItemUtils.getEssenceFromString(s));

        if(ItemUtils.isRune(s))
            stats.setRune(ItemUtils.getRuneFromString(s, 4));

        if(ItemUtils.isGem(s))
            stats.addGem(ItemUtils.getGemFromString(s, 4));
    }

    public ItemStats getStats() {
        return stats;
    }

    public ArrayList<GameClass> getGameClasses() {
        return gameClasses;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void update() {

        if(item == null) return;

        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();

        for(int i=lore.size()-1;i>=0;i--) {
            if(lore.get(i).equalsIgnoreCase(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------------") || lore.get(i).contains(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------------")) {
                break;
            } else {
                lore.remove(i);
            }
        }

        for(int i=0;i<stats.gems.size();i++) {
            Gem gem = stats.gems.get(i);
            lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "▸ 〘 " + gem.getType().getPrefix() + gem.getType().getName() + " " + TextUtils.getRomanFromNumber(gem.getLevel()) + ChatColor.GREEN.toString() + " 〙");
        }

        for(int i=0;i<stats.gemsockets;i++) {
            lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "▸ 〘 Gem Socket 〙");
        }

        if(stats.getRune() != null) {
            lore.add(ChatColor.RESET + ChatColor.AQUA.toString() + "▸ 〘 " + stats.getRune().getType().getPrefix() + stats.getRune().getType().getName() + ChatColor.AQUA.toString() + " 〙");
        }

        if(stats.hasRuneSocket) {
            lore.add(ChatColor.RESET + ChatColor.AQUA.toString() + "▸ 〘 Rune Socket 〙");
        }

        if(stats.getEssence() != null) {
            lore.add(ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + "▸ 〘 " + stats.getEssence().getType().getPrefix() + stats.getEssence().getType().getName() + ChatColor.LIGHT_PURPLE.toString() + " 〙");
        }

        if(stats.hasEssenceSocket) {
            lore.add(ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + "▸ 〘 Essence Socket 〙");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }
}
