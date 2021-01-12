package me.kench.player.stat.view;

/**
 * Evasion is considered a percentage. Its actual applicable value is generated via
 * {@code points / 100}. It's display value is a float with a single decimal point.
 */
public class EvasionView extends StatView {
    public EvasionView(float statPoints) {
        super(statPoints);
    }

    @Override
    public float getValue() {
        return getStatPoints() / 100;
    }

    @Override
    public String getDisplayValue() {
        return String.format("%.1f%%", getStatPoints());
    }

    @Override
    public StatView merge(StatView... views) {
        return new EvasionView(merge0(views));
    }
}
