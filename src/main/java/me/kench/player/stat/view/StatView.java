package me.kench.player.stat.view;

public abstract class StatView {
    private final float statPoints;

    public StatView(float statPoints) {
        this.statPoints = statPoints;
    }

    public final float getStatPoints() {
        return statPoints;
    }

    public abstract float getValue();

    public abstract String getDisplayValue();
}
