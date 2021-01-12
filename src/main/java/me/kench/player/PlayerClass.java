package me.kench.player;

import me.kench.RotMC;
import me.kench.database.playerdata.PlayerData;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.GemType;
import me.kench.items.stats.RuneType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.player.stat.Stat;
import me.kench.player.stat.Stats;
import me.kench.player.stat.view.StatView;
import me.kench.session.PlayerSession;
import me.kench.utils.ItemUtils;
import me.kench.utils.Messaging;
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
    private StatView attackAllStat, evadeAllStat, defenseAllStat;

    /**
     * Creates a NEW PlayerClass with default data, to later be saved to the database.
     * @param playerUniqueId the owner's unique id
     * @param rpgClass the {@link RpgClass} that this PlayerClass represents
     */
    public PlayerClass(UUID playerUniqueId, RpgClass rpgClass) {
        this(
                playerUniqueId,
                UUID.randomUUID(),
                rpgClass,
                new Stats(),
                0L,
                0,
                false,
                null
        );
    }

    /**
     * Creates a PlayerClass that was previously saved in the database.
     * @param playerUniqueId the owner's unique id
     * @param uniqueId the unique id of this PlayerClass
     * @param rpgClass the {@link RpgClass} that this PlayerClass represents
     * @param stats the {@link Stats} of the PlayerClass
     * @param fame the fame (xp) of the PlayerClass
     * @param level the level; see {@link LevelProgression}.
     * @param selected whether or not this PlayerClass is currently selected by the player
     * @param inventory the inventory of this PlayerClass
     */
    public PlayerClass(UUID playerUniqueId, UUID uniqueId, RpgClass rpgClass, Stats stats, long fame, int level, boolean selected, Inventory inventory) {
        this.playerUniqueId = playerUniqueId;
        this.uniqueId = uniqueId;
        this.rpgClass = rpgClass;
        this.stats = stats;
        this.fame = fame;
        this.level = level;
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
        if (stats.getStat(stat).getStatPoints() < stats.getCap(getRpgClass(), stat)) {
            Messaging.sendMessage(getPlayer(), String.format("<green>Your %s stat has increased by %s1!", stat.getName(), GemType.fromStat(stat).getPrefix()));
            stats.incrementStat(stat, 1F);
        }

        applyStats();
    }

    public long getFame() {
        return fame;
    }

    public void giveFame(long fame, boolean withoutMultiplier) {
        if (!withoutMultiplier) {
            this.fame += fame * getMultiplier();
        } else {
            this.fame += fame;
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

    public StatView getAttackAllStat() {
        return attackAllStat;
    }

    public void setAttackAllStat(StatView attackAllStat) {
        this.attackAllStat = attackAllStat;
    }

    public StatView getEvadeAllStat() {
        return evadeAllStat;
    }

    public void setEvadeAllStat(StatView evadeAllStat) {
        this.evadeAllStat = evadeAllStat;
    }

    public StatView getDefenseAllStat() {
        return defenseAllStat;
    }

    public void setDefenseAllStat(StatView defenseAllStat) {
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
                    double dodgeMultiplier = stats.getCapForLevel(rpgClass, Stat.EVASION, newLevel) / 35 / 3.5;

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
                        addStat(Stat.EVASION);
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

        List<PotionEffectType> effects = ItemUtils.getRuneEffects(player);
        effects.forEach(effect -> player.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, 0)));

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            PotionEffectType type = potionEffect.getType();

            RuneType runeType = RuneType.getByPotionEffectType(type);
            if (runeType == null) continue;

            if (!effects.contains(type)) {
                player.removePotionEffect(type);
            }
        }

        Stats overallStats = stats.clone();
        overallStats.incrementStats(ItemUtils.getGemStatsFromEquipment(player));
        overallStats.incrementStats(ItemUtils.getItemStatsFromEquipment(player));

        // Default walk speed is 0.2F
        player.setWalkSpeed(0.2F * overallStats.getStat(Stat.SPEED).getValue());

        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(rpgClass.getBaseHealth() + overallStats.getStat(Stat.HEALTH).getValue());
        }

        attackAllStat = overallStats.getStat(Stat.ATTACK);
        evadeAllStat = overallStats.getStat(Stat.EVASION);
        defenseAllStat = overallStats.getStat(Stat.DEFENSE);
    }

    @Override
    public int compareTo(@NotNull PlayerClass other) {
        return Long.compare(getFame(), other.getFame());
    }
}
