package me.kench.player.stat.view;

/**
 * Defense is considered a percentage, but is special. Due to quirks in Minecraft,
 * we halve the stat points in the percentage calculation, effectively doubling
 * the scale of the stat. This is to level out the end result in Minecraft itself.
 * Its actual applicable value is generated via {@code (0.5 * points) / 100}. Its
 * display value is a float with a single decimal point.
 */
public class DefenseView extends StatView {
    public DefenseView(float statPoints) {
        super(statPoints);
    }

    @Override
    public float getValue() {
        return (0.5F * getStatPoints()) / 100F;
    }

    @Override
    public String getDisplayValue() {
        return String.format("%.1f%%", getStatPoints());
    }
}
