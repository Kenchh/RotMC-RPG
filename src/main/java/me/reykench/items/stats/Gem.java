package me.reykench.items.stats;

public class Gem {

    GemType type;
    int level;

    public Gem(GemType type, int level) {
        this.type = type;
        this.level = level;
    }

    public GemType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
}
