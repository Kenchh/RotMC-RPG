package me.kench.rotmc.player.stat.view;

/**
 * Vitality is considered a whole number value. Its actual applicable value
 * and its display value are the same value an equate to the number of {@code points}.
 */
public class VitalityView extends StatView {
    public VitalityView(float statPoints) {
        super(statPoints);
    }

    @Override
    public float getValue() {
        return getStatPoints();
    }

    @Override
    public String getDisplayValue() {
        return String.format("%.1f", getStatPoints());
    }

    @Override
    public StatView merge(StatView... views) {
        return new VitalityView(merge0(views));
    }
}
