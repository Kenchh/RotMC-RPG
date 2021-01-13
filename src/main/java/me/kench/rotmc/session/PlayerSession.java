package me.kench.rotmc.session;

import me.kench.rotmc.RotMcPlugin;
import me.kench.rotmc.items.GameItem;
import me.kench.rotmc.items.stats.EssenceType;
import me.kench.rotmc.items.stats.essenceanimations.EssenceAnimation;
import me.kench.rotmc.player.EssenceTicker;
import me.kench.rotmc.player.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
    private final List<Block> obsidianBlocks;
    private String lastKiller;
    private String lastDamage;
    private BukkitTask ticker;
    private GameItem gameItem;
    private PlayerClass clickedClass;

    public PlayerSession(UUID uniqueId) {
        this.uniqueId = uniqueId;

        activeEssences = new HashMap<>();
        goldBlocks = new ArrayList<>();
        iceBlocks = new ArrayList<>();
        obsidianBlocks = new ArrayList<>();
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

    public List<Block> getObsidianBlocks() {
        return obsidianBlocks;
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
        this.ticker = new EssenceTicker(getPlayer()).runTaskTimer(RotMcPlugin.getInstance(), 1L, 60L);
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

    public PlayerClass getClickedClass() {
        return clickedClass;
    }

    public void setClickedClass(PlayerClass clickedClass) {
        this.clickedClass = clickedClass;
    }
}
