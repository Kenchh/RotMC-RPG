package me.kench.utils;

import me.kench.RotMC;
import me.kench.items.GameItem;
import me.kench.items.stats.*;
import me.kench.items.stats.essenceanimations.*;
import me.kench.player.PlayerClass;
import me.kench.player.Stat;
import me.kench.player.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static ArrayList<Block> goldBlocks = new ArrayList<>();
    public static ArrayList<Block> iceBlocks = new ArrayList<>();
    public static ArrayList<Block> obsidianBlocks = new ArrayList<>();

    public static Stats getOverallItemStatsFromEquipment(Player p) {
        Stats overallStats = new Stats();

        /*
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
        */

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);

                if (gameItem.getStats().getPlayerStatBoost() != null) {
                    Stats itemStats = gameItem.getStats().getPlayerStatBoost();

                    overallStats.incrementStat(Stat.HEALTH, itemStats.getStat(Stat.HEALTH));
                    overallStats.incrementStat(Stat.ATTACK, itemStats.getStat(Stat.ATTACK));
                    overallStats.incrementStat(Stat.DEFENSE, itemStats.getStat(Stat.DEFENSE));
                    overallStats.incrementStat(Stat.SPEED, itemStats.getStat(Stat.SPEED));
                    overallStats.incrementStat(Stat.DODGE, itemStats.getStat(Stat.DODGE));
                    overallStats.incrementStat(Stat.VITALITY, itemStats.getStat(Stat.VITALITY));
                }
            }
        }

        return overallStats;
    }

    public static Stats getItemStatsByLore(List<String> lore) {
        Stats stats = new Stats();

        for (String s : lore) {
            for (GemType gt : GemType.values()) {
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
                        case VITALITY:
                            stats.vitality += level;
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

            case GANDA:
                return new Ganda();
            case HADORI:
                return new Hadori();
            case ILLURI:
                return new Illuri();
            case KABIRI:
                return new Kabiri();
        }
        return null;
    }

    public static ArrayList<EssenceType> getActiveEssences(Player p) {
        ArrayList<EssenceType> essenceTypes = new ArrayList<>();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());
            if (gameItem.getStats().getEssence() != null) {
                EssenceType essenceType = gameItem.getStats().getEssence().getType();
                if (!essenceTypes.contains(essenceType)) {
                    essenceTypes.add(essenceType);
                }
            }
        }

        if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());
            if (gameItem.getStats().getEssence() != null) {
                EssenceType essenceType = gameItem.getStats().getEssence().getType();
                if (!essenceTypes.contains(essenceType)) {
                    essenceTypes.add(essenceType);
                }
            }
        }

        for (ItemStack armor : p.getInventory().getArmorContents()) {

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

    public static void checkAllowedArmor(Player p) {

        int i = 0;
        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);

                PlayerData pd = RotMC.getPlayerData(p);
                if (pd == null) {
                    throwAwayArmor(armor, p, i);
                    i++;
                    continue;
                }

                PlayerClass pc = pd.getMainClass();
                if (pc == null) {
                    throwAwayArmor(armor, p, i);
                    i++;
                    continue;
                }

                if (gameItem.getLevel() != 0) {
                    int level = gameItem.getLevel();

                    if (pc.getLevel() < level) {
                        p.sendMessage(ChatColor.RED + "You need to be level " + level + " to use this item!");
                        throwAwayArmor(armor, p, i);
                        i++;
                        continue;
                    }
                }

                boolean foundClass = false;
                String currentclass = pc.getData().getName();
                if (gameItem.getGameClasses().isEmpty() == false) {
                    for (GameClass gameClass : gameItem.getGameClasses()) {
                        String className = gameClass.getName();

                        if (className.equalsIgnoreCase(currentclass)) {
                            foundClass = true;
                            break;
                        }
                    }
                }

                if (!foundClass) {
                    p.sendMessage(ChatColor.RED + "That item is not suitable for " + currentclass + "!");
                    throwAwayArmor(armor, p, i);
                    i++;
                    continue;
                }
            }
            i++;
        }
    }

    private static void throwAwayArmor(ItemStack armor, Player p, int i) {
        ItemStack armorClone = armor.clone();
        switch (i) {
            case 3:
                p.getInventory().setHelmet(null);
                break;
            case 2:
                p.getInventory().setChestplate(null);
                break;
            case 1:
                p.getInventory().setLeggings(null);
                break;
            case 0:
                p.getInventory().setBoots(null);
                break;
        }

        if (fullInv(p)) {
            Item itemDropped = p.getWorld().dropItem(p.getLocation(), armorClone);
            itemDropped.setPickupDelay(40);
        } else {
            p.getInventory().addItem(armorClone);
        }
    }

    private static boolean fullInv(Player p) {
        for (ItemStack it : p.getInventory().getStorageContents()) {
            if (it == null) return false;
        }
        return true;
    }

    public static ArrayList<PotionEffectType> getOverallRuneEffects(Player p) {
        ArrayList<PotionEffectType> effects = new ArrayList<>();

        /*

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

         */

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);
                if (gameItem.getStats().getRune() != null)
                    effects.add(gameItem.getStats().getRune().getType().getPotionEffectType());
            }
        }

        return effects;
    }

    public static Stats getOverallGemStatsFromEquipment(Player p) {

        Stats overallStats = new Stats();

        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInMainHand());

            for (Gem g : gameItem.getStats().gems) {
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
                    case VITALITY:
                        overallStats.vitality += g.getLevel();
                        break;
                }
            }
        }

        if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            GameItem gameItem = new GameItem(p.getInventory().getItemInOffHand());

            for (Gem g : gameItem.getStats().gems) {
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
                    case VITALITY:
                        overallStats.vitality += g.getLevel();
                        break;
                }
            }
        }

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                GameItem gameItem = new GameItem(armor);

                for (Gem g : gameItem.getStats().gems) {
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
                        case VITALITY:
                            overallStats.vitality += g.getLevel();
                            break;
                    }
                }
            }
        }

        return overallStats;
    }

    public static float getValueFromGemType(GemType gemType, int level, boolean visual) {
        switch (gemType) {
            case HEALTH:
                return StatUtils.getHealth(level, visual, true);
            case ATTACK:
                return StatUtils.getAttack(level, visual, true);
            case DEFENSE:
                return StatUtils.getDefense(level, visual, true);
            case SPEED:
                return StatUtils.getSpeed(level, visual, true);
            case DODGE:
                return StatUtils.getDodge(level, visual, true);
            case VITALITY:
                return StatUtils.getVitality(level, visual, true);
        }
        return -1;
    }

    public static Gem getGemFromString(String s, int indent) {
        int level = TextUtils.getNumberFromRoman(TextUtils.getLastWord(s, indent));

        for (GemType gemType : GemType.values()) {
            if (s.contains(gemType.getName())) {
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

    public static Rune getRuneFromString(String s, int indent) {

        for (RuneType runeType : RuneType.values()) {
            if (s.contains(runeType.getName())) {
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

    public static Essence getEssenceFromString(String s) {
        for (EssenceType essenceType : EssenceType.values()) {
            if (s.contains(essenceType.getName())) {
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
