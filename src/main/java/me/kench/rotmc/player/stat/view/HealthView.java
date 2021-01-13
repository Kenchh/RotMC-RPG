package me.kench.rotmc.player.stat.view;

/**
 * Health is considered in terms of half-hearts in Minecraft. It's actual applicable
 * value is generated via {@code 0.5 * points}. Its display value is a float with a
 * single decimal point followed by the letters {@code "HP"}.
 */
public class HealthView extends StatView {
    public HealthView(float statPoints) {
        super(statPoints);
    }

    @Override
    public float getValue() {
        return 0.5F * getStatPoints();
    }

    @Override
    public String getDisplayValue() {
        return String.format("%.1f HP", getStatPoints());
    }

    @Override
    public StatView merge(StatView... views) {
        return new HealthView(merge0(views));
    }
}
