package me.kench.player;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.GemType;
import me.kench.items.stats.RuneType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.session.PlayerSession;
import me.kench.utils.ItemUtils;
import me.kench.utils.Messaging;
import me.kench.utils.StatUtils;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerClass implements Comparable<PlayerClass> {
    private final UUID playerUniqueId;
    private final UUID uniqueId;
    private final RpgClass rpgClass;
    private final Stats stats;
    private long fame = 0;
    private int level = 1;
    private boolean selected;
    private Inventory inventory;
    private float attackAllStat, dodgeAllStat, defenseAllStat;

    public PlayerClass(UUID playerUniqueId, UUID uniqueId, RpgClass rpgClass, Stats stats, long fame, int level, boolean selected, Inventory inventory) {
        this.playerUniqueId = playerUniqueId;
        this.uniqueId = uniqueId;
        this.rpgClass = rpgClass;
        this.fame = fame;
        this.level = level;
        this.stats = stats;
        this.selected = selected;
        this.inventory = inventory;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getPlayerUniqueId());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getPlayerUniqueId());
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public RpgClass getRpgClass() {
        return rpgClass;
    }

    public Stats getStats() {
        return stats;
    }

    public void addStat(Stat stat) {
        if (stats.getStat(stat) < stats.getCap(getRpgClass(), stat)) {
            Messaging.sendMessage(getPlayer(), String.format("<green>Your %s stat has increased by %s1!", stat.getName(), GemType.fromStat(stat).getPrefix()));
            stats.incrementStat(stat);
        }

        applyStats();
    }

    public long getFame() {
        return fame;
    }

    public void giveFame(int xp, boolean withoutMultiplier) {
        if (!withoutMultiplier) {
            this.fame += xp * getMultiplier();
        } else {
            this.fame += xp;
        }

        processLevelChange(RotMC.getInstance().getLevelProgression().getLevelByFame(this.fame));
    }

    public void setFame(long fame) {
        this.fame = fame;
        processLevelChange(RotMC.getInstance().getLevelProgression().getLevelByFame(this.fame));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.fame = RotMC.getInstance().getLevelProgression().getFameByLevel(level);
        processLevelChange(level);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public float getAttackAllStat() {
        return attackAllStat;
    }

    public void setAttackAllStat(float attackAllStat) {
        this.attackAllStat = attackAllStat;
    }

    public float getDodgeAllStat() {
        return dodgeAllStat;
    }

    public void setDodgeAllStat(float dodgeAllStat) {
        this.dodgeAllStat = dodgeAllStat;
    }

    public float getDefenseAllStat() {
        return defenseAllStat;
    }

    public void setDefenseAllStat(float defenseAllStat) {
        this.defenseAllStat = defenseAllStat;
    }

    private void processLevelChange(int newLevel) {
        levelUp(newLevel);
        levelDown(newLevel);

        this.level = newLevel;

        RotMC.getInstance().getLevelProgression().displayLevelProgression(getPlayer());
    }

    private void levelDown(int newLevel) {
        if (newLevel < level) {
            UserManager userManager = RotMC.getInstance().getLuckPerms().getUserManager();
            User user = userManager.getUser(getPlayerUniqueId());
            if (user == null) return;

            for (int i = level; i >= newLevel + 1; i--) {
                    user.data().remove(Node.builder(String.format("rotmc.level.{1-%d}", i)).build());
                }

            user.data().add(Node.builder(String.format("rotmc.level.{1-%d}", newLevel)).build());
            userManager.saveUser(user);

            Messaging.sendMessage(getPlayer(), String.format("<red>You have leveled down to <gold>%d<red>!", newLevel));

            applyStats();
        }
    }

    private void levelUp(int newLevel) {
        if (newLevel > level) {
            UserManager userManager = RotMC.getInstance().getLuckPerms().getUserManager();
            User user = userManager.getUser(getPlayerUniqueId());
            if (user == null) return;

            for (int i = level + 1; i <= newLevel; i++) {
                    double healthMultiplier = stats.getCapForLevel(rpgClass, Stat.HEALTH, newLevel) / 35 / 3.5;
                    double attackMultiplier = stats.getCapForLevel(rpgClass, Stat.ATTACK, newLevel) / 35 / 3.5;
                    double defenseMultiplier = stats.getCapForLevel(rpgClass, Stat.DEFENSE, newLevel) / 35 / 3.5;
                    double speedMultiplier = stats.getCapForLevel(rpgClass, Stat.SPEED, newLevel) / 35 / 3.5;
                    double dodgeMultiplier = stats.getCapForLevel(rpgClass, Stat.DODGE, newLevel) / 35 / 3.5;

                    if ((((double) new Random().nextInt(100) + 1)) / 100D <= healthMultiplier) {
                        addStat(Stat.HEALTH);
                    }

                    if ((((double) new Random().nextInt(100) + 1)) / 100D <= attackMultiplier) {
                        addStat(Stat.ATTACK);
                    }

                    if ((((double) new Random().nextInt(100) + 1)) / 100D <= defenseMultiplier) {
                        addStat(Stat.DEFENSE);
                    }

                    if ((((double) new Random().nextInt(100) + 1)) / 100D <= speedMultiplier) {
                        addStat(Stat.SPEED);
                    }

                    if ((((double) new Random().nextInt(100) + 1)) / 100D <= dodgeMultiplier) {
                        addStat(Stat.DODGE);
                    }

                    // TODO: Vitality

                    user.data().add(Node.builder("rotmc.level.{1-" + i + "}").build());
                }

            userManager.saveUser(user);

            Player player = getPlayer();
            Messaging.sendMessage(player, String.format("<green>You have reached level <gold>%d<green>!", newLevel));
            Messaging.sendMessage(player, "<green>Type /stats to check your updated stats.");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

            applyStats();
        }
    }

    private double getMultiplier() {
        for (double multiplier = 1.5; multiplier > 1.0; multiplier -= 0.1) {
            if (getPlayer().hasPermission(String.format("rotmc.fame.multiplier.%s", multiplier))) {
                return multiplier;
            }
        }

        return 1;
    }

    public void tickEssences(PlayerData data) {
        PlayerSession session = data.getSession();

        List<EssenceType> activeEssences = ItemUtils.getActiveEssences(getPlayer());
        List<EssenceType> etToRemove = new ArrayList<>();

        for (EssenceType essenceType : session.getActiveEssences().keySet()) {
            if (!activeEssences.contains(essenceType)) {
                etToRemove.add(essenceType);
                session.getActiveEssences().get(essenceType).cancel();
            }
        }

        for (EssenceType essenceType : etToRemove) {
            session.getActiveEssences().remove(essenceType);
        }

        for (EssenceType essenceType : activeEssences) {
            if (!session.getActiveEssences().containsKey(essenceType)) {
                EssenceAnimation essenceAnimation = ItemUtils.getAnimationFromType(essenceType);
                if (essenceAnimation != null) {
                    session.getActiveEssences().put(essenceType, essenceAnimation);
                    essenceAnimation.start(getPlayer());
                }
            }
        }

    }

    public void applyStats() {
        Player player = getPlayer();

        List<PotionEffectType> effects = ItemUtils.getOverallRuneEffects(player);
        for (PotionEffect pe : player.getActivePotionEffects()) {
            boolean runeEffect = false;

            for (RuneType rt : RuneType.values()) {
                if (rt.getPotionEffectType().getName().equalsIgnoreCase(pe.getType().getName())) {
                    runeEffect = true;
                    break;
                }
            }

            if (runeEffect) {
                if (!effects.contains(pe.getType())) {
                    player.removePotionEffect(pe.getType());
                }
            }
        }

        for (PotionEffectType effect : effects) {
            player.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, 0));
        }

        float speedPlayerStat = StatUtils.getSpeed(stats.getStat(Stat.SPEED), false, false);
        float speedGemStat = StatUtils.getSpeed(ItemUtils.getOverallGemStatsFromEquipment(player).getStat(Stat.SPEED), false, true);
        float speedItemStat = ItemUtils.getOverallItemStatsFromEquipment(player).getStat(Stat.SPEED) / 100F;
        float speedAll = speedPlayerStat + speedItemStat + speedGemStat;
        player.setWalkSpeed(0.2F + 0.2F * speedAll);

        float healthPlayerStat = StatUtils.getHealth(stats.getStat(Stat.HEALTH), false, false);
        float healthGemStat = StatUtils.getHealth(ItemUtils.getOverallGemStatsFromEquipment(player).getStat(Stat.HEALTH), false, true);
        float healthItemStat = ItemUtils.getOverallItemStatsFromEquipment(player).getStat(Stat.HEALTH);
        float healthAll = healthPlayerStat + healthItemStat + healthGemStat;

        AttributeInstance maxHealthAttr = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        switch (rpgClass) {
            case NECROMANCER:
                // robe
                maxHealthAttr.setBaseValue(16 + healthAll);
                break;
            case HUNTRESS:
            case ASSASSIN:
            case ROGUE:
                // leather
                maxHealthAttr.setBaseValue(20 + healthAll);
                break;
            case WARRIOR:
            case KNIGHT:
                // heavy
                maxHealthAttr.setBaseValue(24 + healthAll);
                break;

        }

        // Nothing happening down here?
        float attackPlayerStat = StatUtils.getAttack(stats.getStat(Stat.ATTACK), false, false);
        float attackGemStat = StatUtils.getAttack(ItemUtils.getOverallGemStatsFromEquipment(player).getStat(Stat.ATTACK), false, true);
        float attackItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).getStat(Stat.ATTACK)) / 100F;
        attackAllStat = attackPlayerStat + attackGemStat + attackItemStat;

        float dodgePlayerStat = StatUtils.getDodge(stats.getStat(Stat.DODGE), true, false);
        float dodgeGemStat = StatUtils.getDodge(ItemUtils.getOverallGemStatsFromEquipment(player).getStat(Stat.DODGE), true, true);
        float dodgeItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).getStat(Stat.DODGE));
        dodgeAllStat = dodgePlayerStat + dodgeGemStat + dodgeItemStat;

        float defensePlayerStat = StatUtils.getDefense(stats.getStat(Stat.DEFENSE), false, false);
        float defenseGemStat = StatUtils.getDefense(ItemUtils.getOverallGemStatsFromEquipment(player).getStat(Stat.DEFENSE), false, true);
        float defenseItemStat = ((float) ItemUtils.getOverallItemStatsFromEquipment(player).getStat(Stat.DEFENSE)) / 200F;
        defenseAllStat = defensePlayerStat + defenseGemStat + defenseItemStat;
    }

    @Override
    public int compareTo(@NotNull PlayerClass other) {
        return Long.compare(getFame(), other.getFame());
    }
}
