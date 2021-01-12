package me.kench.player.stat.view;

/**
 * Speed is considered a percentage. Its actual applicable value is generated via
 * {@code 1 + (points / 100)}. It's display value is a float with a single decimal point.
 */
public class SpeedView extends StatView {
    public SpeedView(float statPoints) {
        super(statPoints);
    }

    @Override
    public float getValue() {
        return 1 + (getStatPoints() / 100);
    }

    @Override
    public String getDisplayValue() {
        return String.format("%.1f%%", getStatPoints());
    }
}
