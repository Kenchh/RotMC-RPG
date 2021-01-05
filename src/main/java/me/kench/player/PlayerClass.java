package me.kench.player;

import me.kench.RotMC;
import me.kench.game.GameClass;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.GemType;
import me.kench.items.stats.RuneType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.utils.ItemUtils;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerClass {

    private UUID uuid;
    private Player player;
    private GameClass gameClass;
    public boolean selected;
    public Inventory inventory;

    int xp = 0;
    int level = 1;

    private Stats stats;

    public float attackAllStat = 0;
    public float defenseAllStat = 0;
    public float dodgeAllStat = 0;

    public PlayerClass(UUID uuid, Player player, GameClass gameClass, int xp, int level, Stats stats) {
        this.uuid = uuid;
        this.player = player;
        this.gameClass = gameClass;

        this.xp = xp;
        this.level = level;

        this.stats = stats;

    }

    public GameClass getGameClass() {
        return gameClass;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addStat(String s) {
        String fc = s.substring(0, 1).toUpperCase();
        s = fc + s.substring(1);
        switch (s) {
            case "Health":
                if (stats.health < stats.getCap(getData().getName(), s)) {
                    player.sendMessage(ChatColor.GREEN + "Your health stat has increased by " + GemType.HEALTH.getPrefix() + "1!");
                    stats.health += 1F;
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max health!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " health");
                }
                break;
            case "Attack":
                if (stats.attack < stats.getCap(getData().getName(), s)) {
                    player.sendMessage(ChatColor.GREEN + "Your damage stat has increased by " + GemType.ATTACK.getPrefix() + "1!");
                    stats.attack += 1F;
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max attack!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " attack");
                }
                break;
            case "Defense":
                if (stats.defense < stats.getCap(getData().getName(), s)) {
                    stats.defense += 1F;
                    player.sendMessage(ChatColor.GREEN + "Your defense stat has increased by " + GemType.DEFENSE.getPrefix() + "1!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max defense!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " defense");
                }
                break;
            case "Speed":
                if (stats.speed < stats.getCap(getData().getName(), s)) {
                    stats.speed += 1F;
                    player.sendMessage(ChatColor.GREEN + "Your speed stat has increased by " + GemType.SPEED.getPrefix() + "1!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max speed!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " speed");
                }
                break;
            case "Dodge":
                if (stats.dodge < stats.getCap(getData().getName(), s)) {
                    stats.dodge += 1F;
                    player.sendMessage(ChatColor.GREEN + "Your evasion stat has increased by " + GemType.DODGE.getPrefix() + "1!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max dodge!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " evasion");
                }
                break;
            case "Vitality":
                if (stats.vitality < stats.getCap(getData().getName(), s)) {
                    stats.vitality += 1F;
                    player.sendMessage(ChatColor.GREEN + "Your vitality stat has increased by " + GemType.VITALITY.getPrefix() + "1!");
                } else {
                    player.sendMessage(ChatColor.RED + "You already have max vitality!");
                    //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s " + player.getName() + " evasion");
                }
                break;
        }
        applyStats();
        RotMC.getInstance().getSqlManager().update(getPlayer(), null);
    }

    public void giveXP(int xp, boolean withoutmultiplier) {
        if (!withoutmultiplier) {
            this.xp += xp * getMultiplier();
        } else {
            this.xp += xp;
        }

        int newlevel = RotMC.getInstance().getLevelProgression().getLevelByXP(this.xp);

        levelUp(newlevel);
        levelDown(newlevel);

        this.level = newlevel;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player, this);
        RotMC.getInstance().getSqlManager().update(player, null);
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
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player, this);
        RotMC.getInstance().getSqlManager().update(player, null);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.xp = RotMC.getInstance().getLevelProgression().getXPByLevel(level);

        levelUp(level);
        levelDown(level);

        this.level = level;
        RotMC.getInstance().getLevelProgression().displayLevelProgression(player, this);
        RotMC.getInstance().getSqlManager().update(player, null);
    }

    public void resetCaps() {
        stats.health = 0;
        stats.attack = 0;
        stats.defense = 0;
        stats.speed = 0;
        stats.dodge = 0;
    }

    private void levelDown(int newlevel) {
        if (newlevel < this.level) {
            applyStats();

            User user = RotMC.getInstance().getApi().getUserManager().loadUser(player.getUniqueId()).join();

            for (int i = level; i >= newlevel + 1; i--) {
                user.data().remove(Node.builder("rotmc.level.{1-" + i + "}").build());
            }

            user.data().add(Node.builder("rotmc.level.{1-" + newlevel + "}").build());

            RotMC.getInstance().getApi().getUserManager().saveUser(user);

            player.sendMessage(ChatColor.RED + "You have leveled down to " + ChatColor.GOLD + newlevel + ChatColor.RED + "!");
        }
    }

    private void levelUp(int newlevel) {
        if (RotMC.getInstance().getLevelProgression().hasLeveledUp(player, newlevel)) {

            User user = RotMC.getInstance().getApi().getUserManager().loadUser(player.getUniqueId()).join();

            for (int i = level + 1; i <= newlevel; i++) {

                double hmult = stats.getCap(getData().getName(), "Health", newlevel) / 35 / 3.5;
                double amult = stats.getCap(getData().getName(), "Attack", newlevel) / 35 / 3.5;
                double dmult = stats.getCap(getData().getName(), "Defense", newlevel) / 35 / 3.5;
                double smult = stats.getCap(getData().getName(), "Speed", newlevel) / 35 / 3.5;
                double emult = stats.getCap(getData().getName(), "Dodge", newlevel) / 35 / 3.5;

                if ((((double) new Random().nextInt(100) + 1)) / 100D <= hmult) {
                    addStat("Health");
                }

                if ((((double) new Random().nextInt(100) + 1)) / 100D <= amult) {
                    addStat("Attack");
                }

                if ((((double) new Random().nextInt(100) + 1)) / 100D <= dmult) {
                    addStat("Defense");
                }

                if ((((double) new Random().nextInt(100) + 1)) / 100D <= smult) {
                    addStat("Speed");
                }

                if ((((double) new Random().nextInt(100) + 1)) / 100D <= emult) {
                    addStat("Dodge");
                }

                user.data().add(Node.builder("rotmc.level.{1-" + i + "}").build());
            }

            RotMC.getInstance().getApi().getUserManager().saveUser(user);

            player.sendMessage(ChatColor.GREEN + "You have reached level " + ChatColor.GOLD + newlevel + ChatColor.GREEN + "!");
            player.sendMessage(ChatColor.GREEN + "Type /stats to check your updated stats.");

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

            applyStats();
        }
    }

    private double getMultiplier() {

        for (double m = 1.5; m > 1.0; m -= 0.1) {
            if (player.hasPermission("rotmc.fame.multiplier." + m)) {
                return m;
            }
        }
        return 1;
    }

    public void tickEssences(PlayerData pd) {

        ArrayList<EssenceType> activeEssences = ItemUtils.getActiveEssences(this.player);
        ArrayList<EssenceType> etToRemove = new ArrayList<>();
        for (EssenceType et : pd.activeEssences.keySet()) {
            if (!activeEssences.contains(et)) {
                etToRemove.add(et);
                pd.activeEssences.get(et).cancel();
            }
        }

        for (EssenceType et : etToRemove)
            pd.activeEssences.remove(et);

        for (EssenceType et : activeEssences) {
            if (!pd.activeEssences.containsKey(et)) {
                EssenceAnimation ea = ItemUtils.getAnimationFromType(et);
                pd.activeEssences.put(et, ea);
                ea.start(this.player);
            }
        }

    }

    public void applyStats() {

        // ItemUtils.checkAllowedArmor(player);

        ArrayList<PotionEffectType> effects = ItemUtils.getOverallRuneEffects(player);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            boolean runeeffect = false;
            for (RuneType rt : RuneType.values()) {
                if (rt.getPotionEffectType().getName().equalsIgnoreCase(pe.getType().getName())) {
                    runeeffect = true;
                    break;
                }
            }
            if (runeeffect) {
                if (!effects.contains(pe.getType())) player.removePotionEffect(pe.getType());
            }
        }

        for (PotionEffectType effect : effects) {
            player.addPotionEffect(new PotionEffect(effect, 10000000, 0));
        }

        float speedPlayerStat = stats.getSpeed(stats.speed, false, false);
        float speedGemStat = stats.getSpeed(ItemUtils.getOverallGemStatsFromEquipment(player).speed, false, true);
        float speedItemStat = ItemUtils.getOverallItemStatsFromEquipment(player).speed / 100F;

        float speedAll = speedPlayerStat + speedItemStat + speedGemStat;

        /*
        if(speedAll > Stats.speedMaxCap) {
            speedAll = Stats.speedMaxCap;
        }
         */

        player.setWalkSpeed(0.2F + 0.2F * speedAll);

        float healthPlayerStat = stats.getHealth(stats.health, false, false);
        float healthGemStat = stats.getHealth(ItemUtils.getOverallGemStatsFromEquipment(player).health, false, true);
        float healthItemStat = ItemUtils.getOverallItemStatsFromEquipment(player).health;

        float healthAll = healthPlayerStat + healthItemStat + healthGemStat;

        /*
        if(healthAll > Stats.healthMaxCap) {
            healthAll = Stats.healthMaxCap;
        }
         */

        List<String> leather = Arrays.asList("HUNTRESS", "ASSASSIN", "ROGUE");
        List<String> robe = Arrays.asList("NECROMANCER");
        List<String> heavy = Arrays.asList("WARRIOR", "KNIGHT");


        if (leather.contains(gameClass.getName().toUpperCase()))
            player.setMaxHealth(20 + healthAll);

        if (robe.contains(gameClass.getName().toUpperCase()))
            player.setMaxHealth(16 + healthAll);

        if (heavy.contains(gameClass.getName().toUpperCase()))
            player.setMaxHealth(24 + healthAll);

        float attackPlayerStat = stats.getAttack(stats.attack, false, false);
        float attackGemStat = stats.getAttack(ItemUtils.getOverallGemStatsFromEquipment(player).attack, false, true);
        float attackItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).attack) / 100F;

        attackAllStat = attackPlayerStat + attackGemStat + attackItemStat;


        float dodgePlayerStat = stats.getDodge(stats.dodge, true, false);
        float dodgeGemStat = stats.getDodge(ItemUtils.getOverallGemStatsFromEquipment(player).dodge, true, true);
        float dodgeItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).dodge);

        dodgeAllStat = dodgePlayerStat + dodgeGemStat + dodgeItemStat;


        float defensePlayerStat = stats.getDefense(stats.defense, false, false);
        float defenseGemStat = stats.getDefense(ItemUtils.getOverallGemStatsFromEquipment(player).defense, false, true);
        float defenseItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).defense) / 200F;

        defenseAllStat = defensePlayerStat + defenseGemStat + defenseItemStat;

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
