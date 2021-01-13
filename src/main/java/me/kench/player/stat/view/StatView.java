package me.kench.player.stat.view;

import java.util.Arrays;

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

    public abstract StatView merge(StatView... views);

    protected final float merge0(StatView... views) {
        final float[] statPoints = {getStatPoints()};
        Arrays.stream(views).forEach(view -> statPoints[0] += view.getStatPoints());
        return statPoints[0];
    }
}
