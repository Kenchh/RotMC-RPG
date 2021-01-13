package me.kench.rotmc.items.stats;

public class Gem {
    private final GemType type;
    private final int level;

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
