package me.reykench.utils;

import me.reykench.RotMC;
import me.reykench.items.GameItem;
import me.reykench.items.stats.*;
import me.reykench.items.stats.essenceanimations.*;
import me.reykench.player.PlayerData;
import me.reykench.player.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static Stats getOverallItemStatsFromEquipment(Player p) {
        Stats overallStats = new Stats();

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());

            if(gameItem.getStats().stats != null) {
                Stats itemstats = gameItem.getStats().stats;

                overallStats.health += itemstats.health;
                overallStats.attack += itemstats.attack;
                overallStats.defense += itemstats.defense;
                overallStats.speed += itemstats.speed;
                overallStats.dodge += itemstats.dodge;
            }
        }

        if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());

            if(gameItem.getStats().stats != null) {
                Stats itemstats = gameItem.getStats().stats;

                overallStats.health += itemstats.health;
                overallStats.attack += itemstats.attack;
                overallStats.defense += itemstats.defense;
                overallStats.speed += itemstats.speed;
                overallStats.dodge += itemstats.dodge;
            }
        }

        for(ItemStack armor : p.getInventory().getArmorContents()) {
            if(armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);

                if(gameItem.getStats().stats != null) {
                    Stats itemstats = gameItem.getStats().stats;

                    overallStats.health += itemstats.health;
                    overallStats.attack += itemstats.attack;
                    overallStats.defense += itemstats.defense;
                    overallStats.speed += itemstats.speed;
                    overallStats.dodge += itemstats.dodge;
                }
            }
        }

        return overallStats;
    }
    
    public static Stats getItemStatsByLore(List<String> lore) {
        Stats stats = new Stats();

        for(String s : lore) {
            for(GemType gt : GemType.values()) {
                if (s.contains(gt.getPrefix() + TextUtils.constantToName(gt.toString()))) {
                    int level = Integer.parseInt(TextUtils.getLastWord(s, 0).replace("%", "").replace("+", ""));
                    switch (gt) {
                        case HEALTH:
                            stats.health += level;
                            break;
                        case ATTACK:
                            stats.attack += level;
                            break;
                        case DEFENSE:
                            stats.defense += level;
                            break;
                        case SPEED:
                            stats.speed += level;
                            break;
                        case DODGE:
                            stats.dodge += level;
                            break;
                    }
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
        }
        return null;
    }

    public static ArrayList<EssenceType> getActiveEssences(Player p) {
        ArrayList<EssenceType> essenceTypes = new ArrayList<>();

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());
            if(gameItem.getStats().getEssence() != null) {
                EssenceType essenceType = gameItem.getStats().getEssence().getType();
                if(!essenceTypes.contains(essenceType)) {
                    essenceTypes.add(essenceType);
                }
            }
        }

        if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());
            if(gameItem.getStats().getEssence() != null) {
                EssenceType essenceType = gameItem.getStats().getEssence().getType();
                if(!essenceTypes.contains(essenceType)) {
                    essenceTypes.add(essenceType);
                }
            }
        }

        for(ItemStack armor : p.getInventory().getArmorContents()) {
            if(armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if(gameItem.getStats().getEssence() != null) {
                    EssenceType essenceType = gameItem.getStats().getEssence().getType();
                    if (!essenceTypes.contains(essenceType)) {
                        essenceTypes.add(essenceType);
                    }
                }
            }
        }

        return essenceTypes;
    }

    public static ArrayList<PotionEffectType> getOverallRuneEffects(Player p) {
        ArrayList<PotionEffectType> effects = new ArrayList<>();

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());
            if(gameItem.getStats().getRune() != null)
                effects.add(gameItem.getStats().getRune().getType().getPotionEffectType());
        }

        if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());
            if(gameItem.getStats().getRune() != null)
                effects.add(gameItem.getStats().getRune().getType().getPotionEffectType());
        }

        for(ItemStack armor : p.getInventory().getArmorContents()) {
            if(armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if(gameItem.getStats().getRune() != null)
                    effects.add(gameItem.getStats().getRune().getType().getPotionEffectType());
            }
        }

        return effects;
    }

    public static Stats getOverallGemStatsFromEquipment(Player p) {

        Stats overallStats = new Stats();

        if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());

            for(Gem g : gameItem.getStats().gems) {
                GemType type = g.getType();
                switch (type) {
                    case HEALTH:
                        overallStats.health += g.getLevel();
                        break;
                    case ATTACK:
                        overallStats.attack += g.getLevel();
                        break;
                    case DEFENSE:
                        overallStats.defense += g.getLevel();
                        break;
                    case SPEED:
                        overallStats.speed += g.getLevel();
                        break;
                    case DODGE:
                        overallStats.dodge += g.getLevel();
                        break;
                }
            }
        }

        if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());

            for(Gem g : gameItem.getStats().gems) {
                GemType type = g.getType();
                switch (type) {
                    case HEALTH:
                        overallStats.health += g.getLevel();
                        break;
                    case ATTACK:
                        overallStats.attack += g.getLevel();
                        break;
                    case DEFENSE:
                        overallStats.defense += g.getLevel();
                        break;
                    case SPEED:
                        overallStats.speed += g.getLevel();
                        break;
                    case DODGE:
                        overallStats.dodge += g.getLevel();
                        break;
                }
            }
        }

        for(ItemStack armor : p.getInventory().getArmorContents()) {
            if(armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);

                for(Gem g : gameItem.getStats().gems) {
                    GemType type = g.getType();
                    switch (type) {
                        case HEALTH:
                            overallStats.health += g.getLevel();
                            break;
                        case ATTACK:
                            overallStats.attack += g.getLevel();
                            break;
                        case DEFENSE:
                            overallStats.defense += g.getLevel();
                            break;
                        case SPEED:
                            overallStats.speed += g.getLevel();
                            break;
                        case DODGE:
                            overallStats.dodge += g.getLevel();
                            break;
                    }
                }
            }
        }

        return overallStats;
    }

    public static float getValueFromGemType(GemType gemType, int level, boolean percentage) {
        switch(gemType) {
            case HEALTH:
                return new Stats().getHealth(level);
            case ATTACK:
                return new Stats().getAttack(level, percentage);
            case DEFENSE:
                return new Stats().getDefense(level, percentage);
            case SPEED:
                return new Stats().getSpeed(level, percentage);
            case DODGE:
                return new Stats().getDodge(level, percentage);
        }
        return -1;
    }

    public static Gem getGemFromString(String s, int indent) {
        int level = TextUtils.getNumberFromRoman(TextUtils.getLastWord(s, indent));

        for(GemType gemType : GemType.values()) {
            if(s.contains(gemType.getName())) {
                return new Gem(gemType, level);
            }
        }

        return null;
    }

    public static boolean isGem(String line) {
        for(GemType gemType : GemType.values()) {
            if(line.contains(gemType.getName())) {
                return true;
            }
        }

        return false;
    }

    public static Rune getRuneFromString(String s, int indent) {

        for(RuneType runeType : RuneType.values()) {
            if(s.contains(runeType.getName())) {
                return new Rune(runeType);
            }
        }

        return null;
    }

    public static boolean isRune(String line) {
        for(RuneType runeType : RuneType.values()) {
            if(line.contains(runeType.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Essence getEssenceFromString(String s) {
        for(EssenceType essenceType : EssenceType.values()) {
            if(s.contains(essenceType.getName())) {
                return new Essence(essenceType);
            }
        }

        return null;
    }

    public static boolean isEssence(String line) {
        for(EssenceType essenceType : EssenceType.values()) {
            if(line.contains(essenceType.getName())) {
                return true;
            }
        }
        return false;
    }

}
