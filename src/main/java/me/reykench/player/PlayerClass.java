package me.reykench.player;

import me.reykench.RotMC;
import me.reykench.game.GameClass;
import me.reykench.items.GameItem;
import me.reykench.items.ItemStats;
import me.reykench.items.stats.*;
import me.reykench.items.stats.essenceanimations.EssenceAnimation;
import me.reykench.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerClass {

    private UUID uuid;
    private Player player;
    private GameClass gameClass;
    public boolean selected;

    int xp = 0;
    int level = 1;

    private Stats stats;

    public PlayerClass(UUID uuid, Player player, GameClass gameClass, int xp, int level, Stats stats) {
        this.uuid = uuid;
        this.player = player;
        this.gameClass = gameClass;

        this.xp = xp;
        this.level = level;

        this.stats = stats;

    }

    public UUID getUuid() {
        return uuid;
    }

    public void addStat(String s) {
        String fc = s.substring(0,1).toUpperCase();
        s = fc+s.substring(1);
        switch (s) {
            case "Health":
                if (stats.health < stats.getCap(getData().getName(), s, level)) {
                    player.sendMessage(ChatColor.GREEN + "Your health stat has increased by " + GemType.HEALTH.getPrefix() + ChatColor.BOLD + stats.getHealth(1) + "!");
                    stats.health++;
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max health!");
                }
                break;
            case "Attack":
                if (stats.attack < stats.getCap(getData().getName(), s, level)) {
                    player.sendMessage(ChatColor.GREEN + "Your damage stat has increased by " + GemType.ATTACK.getPrefix() + ChatColor.BOLD + stats.getAttack(1, true) + "!");
                    stats.attack++;
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max attack damage!");
                }
                break;
            case "Defense":
                if (stats.defense < stats.getCap(getData().getName(), s, level)) {
                    stats.defense++;
                    player.sendMessage(ChatColor.GREEN + "Your defense stat has increased by " + GemType.DEFENSE.getPrefix() + ChatColor.BOLD + stats.getDefense(1, true) + "!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max defense!");
                }
                break;
            case "Speed":
                if (stats.speed < stats.getCap(getData().getName(), s, level)) {
                    stats.speed++;
                    player.sendMessage(ChatColor.GREEN + "Your speed stat has increased by " + GemType.SPEED.getPrefix() + ChatColor.BOLD + stats.getSpeed(1, true) + "%!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max speed!");
                }
                break;
            case "Dodge":
                if (stats.dodge < stats.getCap(getData().getName(), s, level)) {
                    stats.dodge++;
                    player.sendMessage(ChatColor.GREEN + "Your evasion stat has increased by " + GemType.DODGE.getPrefix() + ChatColor.BOLD + stats.getDodge(1, true) + "%!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max dodge!");
                }
                break;
        }
        applyStats();
        RotMC.getInstance().getDatabase().update(getPlayer(), null);
    }

    public void giveXP(int xp) {
        this.xp += xp*getMultiplier();
        int newlevel = RotMC.getInstance().getLevelProgression().getLevelByXP(this.xp);

        levelUp(newlevel);
        levelDown(newlevel);

        this.level = newlevel;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player);
        RotMC.getInstance().getDatabase().update(player, null);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        int newlevel = RotMC.getInstance().getLevelProgression().getLevelByXP(this.xp);

        levelUp(newlevel);
        levelDown(newlevel);

        this.level = newlevel;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player);
        RotMC.getInstance().getDatabase().update(player, null);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.xp = RotMC.getInstance().getLevelProgression().getXPByLevel(level);

        levelUp(level);
        levelDown(level);

        this.level = level;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player);
        RotMC.getInstance().getDatabase().update(player, null);
    }

    private void levelDown(int newlevel) {
        if(newlevel < this.level) {
            applyStats();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manudelp " + player.getName() + " rotmc.level." + newlevel);
            player.sendMessage(ChatColor.RED + "You have leveled down to " + ChatColor.GOLD + newlevel + ChatColor.RED + "!");
        }
    }

    private void levelUp(int newlevel) {
        if(RotMC.getInstance().getLevelProgression().hasLeveledUp(player, newlevel)) {
            applyStats();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "manuaddp " + player.getName() + " rotmc.level." + newlevel);
            player.sendMessage(ChatColor.GREEN + "You have reached level " + ChatColor.GOLD + newlevel + ChatColor.GREEN + "!");
            player.sendMessage(ChatColor.GREEN + "Type /stats to check your updated stats.");
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1F, 1F);
        }
    }

    private double getMultiplier() {

        for(double m=1.5;m>1.0;m-=0.1) {
            if(player.hasPermission("rotmc.fame.multiplier." + m)) {
                return m;
            }
        }
        return 1;
    }

    public void applyStats() {

        PlayerData pd = RotMC.getPlayerData(player);

        ArrayList<EssenceType> activeEssences = ItemUtils.getActiveEssences(player);

        /* Removing old essences */
        ArrayList<EssenceType> etToRemove = new ArrayList<>();
        for(EssenceType et : pd.activeEssences.keySet()) {
            if(!activeEssences.contains(et)) {
                etToRemove.add(et);
                pd.activeEssences.get(et).cancel();
            }
        }
        /*  */

        for(EssenceType et : etToRemove) {
            pd.activeEssences.remove(et);
        }

        /* Adding new essences */
        for(EssenceType et : activeEssences) {
            if(!pd.activeEssences.containsKey(et)) {

                EssenceAnimation ea = ItemUtils.getAnimationFromType(et);

                pd.activeEssences.put(et, ea);

                ea.start(player);
            }
        }
        /*  */

        ArrayList<PotionEffectType> effects = ItemUtils.getOverallRuneEffects(player);
        for(PotionEffect pe : player.getActivePotionEffects()) {
            boolean runeeffect = false;
            for(RuneType rt : RuneType.values()) {
                if(rt.getPotionEffectType().getName().equalsIgnoreCase(pe.getType().getName())) {
                    runeeffect = true;
                    break;
                }
            }
            if(runeeffect) {
                if (!effects.contains(pe.getType())) player.removePotionEffect(pe.getType());
            }
        }

        for(PotionEffectType effect : effects) {
            player.addPotionEffect(new PotionEffect(effect, 10000000, 0));
        }

        player.setWalkSpeed(0.2F + 0.2F*(stats.getSpeed(stats.speed + ItemUtils.getOverallGemStatsFromEquipment(player).speed, false) + (((float) ItemUtils.getOverallItemStatsFromEquipment(player).speed)/100F)));
        player.setMaxHealth(20 + stats.getHealth(stats.health + ItemUtils.getOverallGemStatsFromEquipment(player).health) + ItemUtils.getOverallItemStatsFromEquipment(player).health);
    }

    public Stats getStats() {
        return stats;
    }

    public Player getPlayer() {
        return player;
    }

    public GameClass getData() {
        return gameClass;
    }

}
