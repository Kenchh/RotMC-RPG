package me.kench.utils;

import me.kench.items.GameItem;
import me.kench.items.stats.*;
import me.kench.items.stats.essenceanimations.*;
import me.kench.player.stat.Stat;
import me.kench.player.stat.Stats;
import me.kench.player.stat.view.StatView;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static Stats getItemStatsFromEquipment(Player player) {
        Stats stats = new Stats();

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if (gameItem.getStats().getPlayerStatBoost() != null) {
                    stats.incrementStats(gameItem.getStats().getPlayerStatBoost());
                }
            }
        }

        return stats;
    }

    public static Stats getGemStatsFromEquipment(Player player) {
        Stats stats = new Stats();

        PlayerInventory playerInventory = player.getInventory();

        if (playerInventory.getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(player.getInventory().getItemInMainHand());
            for (Gem gem : gameItem.getStats().getGems()) {
                stats.addGem(gem.getType(), gem.getLevel());
            }
        }

        if (playerInventory.getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(player.getInventory().getItemInOffHand());
            for (Gem gem : gameItem.getStats().getGems()) {
                stats.addGem(gem.getType(), gem.getLevel());
            }
        }

        for (ItemStack armor : playerInventory.getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                for (Gem gem : gameItem.getStats().getGems()) {
                    stats.addGem(gem.getType(), gem.getLevel());
                }
            }
        }

        return stats;
    }

    public static List<PotionEffectType> getRuneEffects(Player player) {
        List<PotionEffectType> effects = new ArrayList<>();

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if (gameItem.getStats().getRune() != null) {
                    effects.add(gameItem.getStats().getRune().getType().getPotionEffectType());
                }
            }
        }

        return effects;
    }

    public static Stats getItemStatsFromLore(List<String> lore) {
        Stats stats = new Stats();

        for (String loreLine : lore) {
            for (GemType gemType : GemType.values()) {
                if (loreLine.contains(gemType.getPrefix() + TextUtils.constantToName(gemType.toString()))) {
                    int level = Integer.parseInt(TextUtils.getLastWord(loreLine, 0).replace("%", "").replace("+", ""));
                    stats.addGem(gemType, level);
                }
            }
        }

        return stats;
    }

    public static EssenceAnimation getAnimationFromType(EssenceType essenceType) {
        switch (essenceType) {
            case VENOM:
                return new Venom();
            case LOVE:
                return new Love();
            case AQUA:
                return new Aqua();
            case FIRE:
                return new Fire();
            case BLOOD:
                return new Blood();
            case WINTER:
                return new Winter();
            case GANDA:
                return new Ganda();
            case HADORI:
                return new Hadori();
            case ILLURI:
                return new Illuri();
            case KABIRI:
                return new Kabiri();
            default:
                return null;
        }
    }

    public static List<EssenceType> getActiveEssences(Player player) {
        List<EssenceType> essenceTypes = new ArrayList<>();

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(player.getInventory().getItemInMainHand());
            if (gameItem.getStats().getEssence() != null) {
                essenceTypes.add(gameItem.getStats().getEssence().getType());
            }
        }

        if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(player.getInventory().getItemInOffHand());
            if (gameItem.getStats().getEssence() != null) {
                EssenceType essenceType = gameItem.getStats().getEssence().getType();
                if (!essenceTypes.contains(essenceType)) {
                    essenceTypes.add(essenceType);
                }
            }
        }

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if (gameItem.getStats().getEssence() != null) {
                    EssenceType essenceType = gameItem.getStats().getEssence().getType();
                    if (!essenceTypes.contains(essenceType)) {
                        essenceTypes.add(essenceType);
                    }
                }
            }
        }

        return essenceTypes;
    }
    public static StatView getValueFromGemType(GemType gemType, int level) {
        Stats stats = new Stats();
        stats.addGem(gemType, level);

        switch (gemType) {
            case HEALTH:
                return stats.getStat(Stat.HEALTH);
            case ATTACK:
                return stats.getStat(Stat.ATTACK);
            case DEFENSE:
                return stats.getStat(Stat.DEFENSE);
            case SPEED:
                return stats.getStat(Stat.SPEED);
            case EVASION:
                return stats.getStat(Stat.EVASION);
            case VITALITY:
                return stats.getStat(Stat.VITALITY);
        }

        return null;
    }

    public static Gem getGemFromString(String string, int indent) {
        int level = TextUtils.getNumberFromRoman(TextUtils.getLastWord(string, indent));

        for (GemType gemType : GemType.values()) {
            if (string.contains(gemType.getName())) {
                return new Gem(gemType, level);
            }
        }

        return null;
    }

    public static boolean isGem(String line) {
        for (GemType gemType : GemType.values()) {
            if (line.contains(gemType.getName())) {
                return true;
            }
        }

        return false;
    }

    public static Rune getRuneFromString(String string) {
        for (RuneType runeType : RuneType.values()) {
            if (string.contains(runeType.getName())) {
                return new Rune(runeType);
            }
        }

        return null;
    }

    public static boolean isRune(String line) {
        for (RuneType runeType : RuneType.values()) {
            if (line.contains(runeType.getName())) {
                return true;
            }
        }

        return false;
    }

    public static Essence getEssenceFromString(String string) {
        for (EssenceType essenceType : EssenceType.values()) {
            if (string.contains(essenceType.getName())) {
                return new Essence(essenceType);
            }
        }

        return null;
    }

    public static boolean isEssence(String line) {
        for (EssenceType essenceType : EssenceType.values()) {
            if (line.contains(essenceType.getName())) {
                return true;
            }
        }

        return false;
    }
}
