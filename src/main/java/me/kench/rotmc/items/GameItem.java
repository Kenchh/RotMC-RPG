package me.kench.rotmc.items;

import me.kench.rotmc.items.stats.Gem;
import me.kench.rotmc.player.RpgClass;
import me.kench.rotmc.utils.ItemUtils;
import me.kench.rotmc.utils.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameItem {
    private ItemStack item;
    private final GameItemStats stats;
    private final List<RpgClass> rpgClasses;
    private int level;

    public GameItem(ItemStack item) {
        this.item = item;
        stats = new GameItemStats(this);
        rpgClasses = new ArrayList<>();

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (meta.hasDisplayName()) {
            String displayName = meta.getDisplayName();

            if (ItemUtils.isGem(displayName) || ItemUtils.isRune(displayName) || ItemUtils.isEssence(displayName)) {
                return;
            }
        }

        List<String> lore = null;
        if (meta.hasLore()) lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        for (String line : lore) {
            if (line.contains("Class:")) {
                String classes = line.replace("§eClass:§f ", "");
                for (String rpgClass : classes.split(", ")) {
                    rpgClasses.add(RpgClass.getByName(rpgClass.trim()));
                }
            }

            if (line.contains("Level:")) {
                String lvl = TextUtils.getLastNumber(line, 0);
                level = Integer.parseInt(lvl.replace("+", ""));
            }

            if (ItemUtils.isEssence(line)) {
                stats.setEssence(ItemUtils.getEssenceFromString(line));
            }

            if (ItemUtils.isRune(line)) {
                stats.setRune(ItemUtils.getRuneFromString(line));
            }

            if (ItemUtils.isGem(line)) {
                stats.addGem(ItemUtils.getGemFromString(line, 4));
            }
        }

        for (int i = 1; i <= lore.size(); i++) {
            String line = lore.get(lore.size() - i);

            if (line.contains("Essence Socket")) {
                stats.setHasEssenceSocket(true);
                continue;
            }

            if (line.contains("Rune Socket")) {
                stats.setHasRuneSocket(true);
                continue;
            }

            if (line.contains("Gem Socket")) {
                stats.setGemSockets(stats.getGemSockets() + 1);
            }
        }

        stats.setPlayerStatBoost(ItemUtils.getItemStatsFromLore(lore));
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public GameItemStats getStats() {
        return stats;
    }

    public List<RpgClass> getRpgClasses() {
        return rpgClasses;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void update() {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = meta.getLore();

        for (int i = lore.size() - 1; i >= 0; i--) {
            if (lore.get(i).equalsIgnoreCase(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------------") || lore.get(i).contains(ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------------")) {
                break;
            } else {
                lore.remove(i);
            }
        }

        for (int i = 0; i < stats.getGems().size(); i++) {
            Gem gem = stats.getGems().get(i);
            lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "▸ 〘 " + gem.getType().getPrefix() + gem.getType().getName() + " " + TextUtils.getRomanFromNumber(gem.getLevel()) + ChatColor.GREEN.toString() + " 〙");
        }

        for (int i = 0; i < stats.getGemSockets(); i++) {
            lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "▸ 〘 Gem Socket 〙");
        }

        if (stats.getRune() != null) {
            lore.add(ChatColor.RESET + ChatColor.AQUA.toString() + "▸ 〘 " + stats.getRune().getType().getPrefix() + stats.getRune().getType().getName() + ChatColor.AQUA.toString() + " 〙");
        }

        if (stats.hasRuneSocket()) {
            lore.add(ChatColor.RESET + ChatColor.AQUA.toString() + "▸ 〘 Rune Socket 〙");
        }

        if (stats.getEssence() != null) {
            lore.add(ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + "▸ 〘 " + stats.getEssence().getType().getPrefix() + stats.getEssence().getType().getName() + ChatColor.LIGHT_PURPLE.toString() + " 〙");
        }

        if (stats.hasEssenceSocket()) {
            lore.add(ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + "▸ 〘 Essence Socket 〙");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
