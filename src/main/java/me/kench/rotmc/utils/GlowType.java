package me.kench.rotmc.utils;

import com.andrebreves.tuple.Tuple;
import com.andrebreves.tuple.Tuple2;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

import static me.kench.rotmc.utils.TextUtils.parseMini;

public enum GlowType {
    BRONZE_GLOW(Material.ORANGE_DYE, ChatColor.GOLD, Tuple.of(0, 0), parseMini("<red><obfuscated>!<reset><yellow>[<gold>Bronze Glow<yellow>]<red><obfuscated>!")),
    SILVER_GLOW(Material.GRAY_DYE, ChatColor.GRAY, Tuple.of(1, 0), parseMini("<white><obfuscated>!<reset><dark_gray>[<gray>Silver Glow<dark_gray>]<white><obfuscated>!")),
    GOLD_GLOW(Material.YELLOW_DYE, ChatColor.YELLOW, Tuple.of(2, 0), parseMini("<dark_red><obfuscated>!<reset><gold>[<yellow>Gold Glow<gold>]<dark_red><obfuscated>!")),
    PLATINUM_GLOW(Material.WHITE_DYE, ChatColor.WHITE, Tuple.of(3, 0), parseMini("<gray><obfuscated>!<reset><dark_gray>[<white>Platinum Glow<dark_gray>]<gray><obfuscated>!")),
    DIAMOND_GLOW(Material.LIGHT_BLUE_DYE, ChatColor.AQUA, Tuple.of(4, 0), parseMini("<dark_aqua><obfuscated>!<reset><white>[<aqua>Diamond Glow<white>]<dark_aqua><obfuscated>!")),
    TOP_FIVE_CHARACTER_GLOW(Material.LIME_DYE, ChatColor.GREEN, Tuple.of(0, 2), parseMini("<dark_green><obfuscated>!<reset><white>[<green>#5 Character Glow<white>]<dark_green><obfuscated>!")),
    TOP_CHARACTER_GLOW(Material.GREEN_DYE, ChatColor.DARK_GREEN, Tuple.of(1, 2), parseMini("<green><obfuscated>!<reset><white>[<dark_green>#1 Character Glow<white>]<green><obfuscated>!")),
    TOP_THREE_GUILD_GLOW(Material.PINK_DYE, ChatColor.LIGHT_PURPLE, Tuple.of(3, 2), parseMini("<dark_purple><obfuscated>!<reset><white>[<light_purple>#3 Guild Glow<white>]<dark_purple><obfuscated>!")),
    TOP_GUILD_GLOW(Material.PURPLE_DYE, ChatColor.DARK_PURPLE, Tuple.of(4, 2), parseMini("<light_purple><obfuscated>!<reset><white>[<dark_purple>#1 Guild Glow<white>]<light_purple><obfuscated>!")),
    INTERSTELLAR_GLOW(Material.LAPIS_LAZULI, ChatColor.BLUE, Tuple.of(1, 4), parseMini("<black><obfuscated>!<reset><dark_gray>[<blue>Interstellar Glow<dark_gray>]<black><obfuscated>!")),
    SHADOW_GLOW(Material.BLACK_DYE, ChatColor.BLACK, Tuple.of(3, 4), parseMini("<black><obfuscated>!<dark_gray>[<gray>S<white>h<gray>a<white>d<gray>o<white>w <gray>G<white>l<gray>o<white>w<dark_gray>]<black><obfuscated>!"));

    private final Material material;
    private final ChatColor glowColor;
    private final Tuple2<Integer, Integer> relativeGuiPosition;
    private final String displayName;

    GlowType(Material material, ChatColor glowColor, Tuple2<Integer, Integer> relativeGuiPosition, String displayName) {
        this.material = material;
        this.glowColor = glowColor;
        this.relativeGuiPosition = relativeGuiPosition;
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public ChatColor getGlowColor() {
        return glowColor;
    }

    public Tuple2<Integer, Integer> getRelativeGuiPosition() {
        return relativeGuiPosition;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return String.format("rotmc.glow.%s", getGlowColor().name().toLowerCase().replace("_", ""));
    }

    public static GlowType getByGlowColor(ChatColor glowColor) {
        return Arrays.stream(GlowType.values())
                .filter(glowType -> glowType.getGlowColor().equals(glowColor))
                .findFirst()
                .orElse(null);
    }
}
