package me.yic.xconomy.utils;

import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayOutputStream stream) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), channel, stream.toByteArray());
        }
    }
}
