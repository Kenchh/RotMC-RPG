package me.kench.session;

import me.kench.RotMC;
import me.kench.items.GameItem;
import me.kench.items.stats.EssenceType;
import me.kench.items.stats.essenceanimations.EssenceAnimation;
import me.kench.player.EssenceTicker;
import me.kench.player.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Class for tracking session ephemeral fields for each Player.
 * This data used to be in PlayerData, but was prone to be lost when the cache did periodic syncs.
 * Here the data lives for as long as the Player is online.
 */
public class PlayerSession {
    private final UUID uniqueId;
    private final Map<EssenceType, EssenceAnimation> activeEssences;
    private final List<Block> goldBlocks;
    private final List<Block> iceBlocks;
    private final List<Block> obbyBlocks;
    private String lastKiller;
    private String lastDamage;
    private BukkitTask ticker;
    private GameItem gameItem;
    private GameItem extractGameItem;
    private ItemStack extractor;
    private PlayerClass clickedClass;

    public PlayerSession(UUID uniqueId) {
        this.uniqueId = uniqueId;

        activeEssences = new HashMap<>();
        goldBlocks = new ArrayList<>();
        iceBlocks = new ArrayList<>();
        obbyBlocks = new ArrayList<>();
        lastKiller = "";
        lastDamage = "";
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public Map<EssenceType, EssenceAnimation> getActiveEssences() {
        return activeEssences;
    }

    public List<Block> getGoldBlocks() {
        return goldBlocks;
    }

    public List<Block> getIceBlocks() {
        return iceBlocks;
    }

    public List<Block> getObbyBlocks() {
        return obbyBlocks;
    }

    public String getLastKiller() {
        return lastKiller;
    }

    public void setLastKiller(String lastKiller) {
        this.lastKiller = lastKiller;
    }

    public String getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(String lastDamage) {
        this.lastDamage = lastDamage;
    }

    public void startTicker() {
        cancelTicker();
        this.ticker = new EssenceTicker(getPlayer()).runTaskTimer(RotMC.getInstance(), 1L, 60L);
    }

    public void cancelTicker() {
        if (this.ticker != null && !this.ticker.isCancelled()) {
            this.ticker.cancel();
        }
    }

    public GameItem getGameItem() {
        return gameItem;
    }

    public void setGameItem(GameItem gameItem) {
        this.gameItem = gameItem;
    }

    public GameItem getExtractGameItem() {
        return extractGameItem;
    }

    public void setExtractGameItem(GameItem extractGameItem) {
        this.extractGameItem = extractGameItem;
    }

    public ItemStack getExtractor() {
        return extractor;
    }

    public void setExtractor(ItemStack extractor) {
        this.extractor = extractor;
    }

    public PlayerClass getClickedClass() {
        return clickedClass;
    }

    public void setClickedClass(PlayerClass clickedClass) {
        this.clickedClass = clickedClass;
    }
}
