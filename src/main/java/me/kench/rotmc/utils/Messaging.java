package me.kench.rotmc.utils;

import me.kench.rotmc.RotMcPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class Messaging {
    public static void sendMessage(CommandSender target, String mini) {
        sendMessage(target, MiniMessage.markdown().parse(mini));
    }

    public static void sendMessage(CommandSender target, Component component) {
        RotMcPlugin.getInstance().adventure().sender(target).sendMessage(component);
    }
}
