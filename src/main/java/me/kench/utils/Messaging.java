package me.kench.utils;

import me.kench.RotMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Messaging {
    public static void sendMessage(Player player, String mini) {
        sendMessage(player, MiniMessage.markdown().parse(mini));
    }

    public static void sendMessage(Player player, Component component) {
        RotMC.getInstance().adventure().player(player).sendMessage(component);
    }
}
