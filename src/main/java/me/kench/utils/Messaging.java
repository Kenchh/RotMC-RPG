package me.kench.utils;

import me.kench.RotMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class Messaging {
    public static void sendMessage(CommandSender target, String mini) {
        sendMessage(target, MiniMessage.markdown().parse(mini));
    }

    public static void sendMessage(CommandSender target, Component component) {
        RotMC.getInstance().adventure().sender(target).sendMessage(component);
    }
}
