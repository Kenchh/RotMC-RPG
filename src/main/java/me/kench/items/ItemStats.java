package me.kench.items;

import me.kench.items.stats.Essence;
import me.kench.items.stats.Gem;
import me.kench.items.stats.Rune;
import me.kench.player.Stats;

import java.util.ArrayList;

public class ItemStats {

    GameItem classItem;

    public int gemsockets = 0;
    public boolean hasRuneSocket = false;
    public boolean hasEssenceSocket;

    public ArrayList<Gem> gems = new ArrayList<>();
    private Rune rune;
    private Essence essence;
    public Stats stats;

    public ItemStats(GameItem classItem) {
        this.classItem = classItem;
    }

    public void addGem(Gem gem) {
        gems.add(gem);
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
