package me.kench.rotmc.utils;

public class EnumUtil {
    public static <T extends Enum<T>> String prettifyConstant(T constant) {
        StringBuilder builder = new StringBuilder();

        for (String piece : constant.name().split("_")) {
            builder.append(piece.charAt(0))
                    .append(piece.substring(1).toLowerCase())
                    .append(" ");
        }

        builder.trimToSize();
        return builder.toString();
    }
}
