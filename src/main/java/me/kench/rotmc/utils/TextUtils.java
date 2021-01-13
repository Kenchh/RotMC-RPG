package me.kench.rotmc.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.text.DecimalFormat;

public class TextUtils {
    public static String parseMini(String format) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.markdown().parse(format));
    }

    public static String constantToName(String constant) {
        String name = constant.toString().toLowerCase();

        String words[] = name.split("_");
        name = "";
        for (String word : words) {
            String firstletter = word.substring(0, 1).toUpperCase();
            String other = word.substring(1);

            String uppercaseWord = firstletter + other;
            name += uppercaseWord + " ";
        }
        name = name.substring(0, name.length() - 1);

        return name;
    }

    public static int getNumberFromRoman(String roman) {
        switch (roman) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            case "VI":
                return 6;
        }
        return -1;
    }

    public static String getRomanFromNumber(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
        }
        return "";
    }

    public static String getLastWord(String s, int indent) {
        String word = "";
        for (int i = s.length() - 1 - indent; i >= 0; i--) {
            char c = s.toCharArray()[i];
            if (c != ' ') {
                word = c + word;
            } else {
                break;
            }
        }
        return word;
    }

    public static String getLastNumber(String s, int indent) {
        String word = "";

        boolean stopAtNextInvalidChar = false;
        for (int i = s.length() - 1 - indent; i >= 0; i--) {
            char c = s.toCharArray()[i];

            if (stopAtNextInvalidChar == false && c == ' ') {
                continue;
            }

            if (Character.isDigit(c) || c == '+' || c == '-' || c == '%') {
                stopAtNextInvalidChar = true;
                word = c + word;
            } else {
                break;
            }
        }

        return word.replace("%", "");
    }

    public static DecimalFormat getDecimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        return decimalFormat;
    }

}
