package me.reykench.items;

import me.reykench.items.stats.Essence;
import me.reykench.items.stats.Gem;
import me.reykench.items.stats.Rune;
import me.reykench.player.Stats;

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
