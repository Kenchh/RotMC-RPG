package me.kench.items;

import me.kench.items.stats.Essence;
import me.kench.items.stats.Gem;
import me.kench.items.stats.Rune;
import me.kench.player.stat.Stats;

import java.util.ArrayList;
import java.util.List;

public class GameItemStats {
    private final GameItem classItem;
    private final List<Gem> gems;
    private Stats playerStatBoost;
    private int gemSockets;
    private boolean hasRuneSocket;
    private boolean hasEssenceSocket;
    private Rune rune;
    private Essence essence;

    public GameItemStats(GameItem classItem) {
        this.classItem = classItem;
        gems = new ArrayList<>();
        playerStatBoost = new Stats();
    }

    public GameItem getClassItem() {
        return classItem;
    }

    public void addGem(Gem gem) {
        gems.add(gem);
    }

    public List<Gem> getGems() {
        return gems;
    }

    public Stats getPlayerStatBoost() {
        return playerStatBoost;
    }

    public void setPlayerStatBoost(Stats playerStatBoost) {
        this.playerStatBoost = playerStatBoost;
    }

    public int getGemSockets() {
        return gemSockets;
    }

    public void setGemSockets(int gemSockets) {
        this.gemSockets = gemSockets;
    }

    public void incrementGemSockets() {
        gemSockets++;
    }

    public void decrementGemSockets() {
        gemSockets--;
    }

    public boolean hasRuneSocket() {
        return hasRuneSocket;
    }

    public void setHasRuneSocket(boolean hasRuneSocket) {
        this.hasRuneSocket = hasRuneSocket;
    }

    public boolean hasEssenceSocket() {
        return hasEssenceSocket;
    }

    public void setHasEssenceSocket(boolean hasEssenceSocket) {
        this.hasEssenceSocket = hasEssenceSocket;
    }

    public Rune getRune() {
        return rune;
    }

    public void setRune(Rune rune) {
        this.rune = rune;
    }

    public Essence getEssence() {
        return essence;
    }

    public void setEssence(Essence essence) {
        this.essence = essence;
    }
}
