package me.yic.xconomy.utils;

import com.google.common.io.ByteArrayDataOutput;
import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayDataOutput stream) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), channel, stream.toByteArray());
        }
    }
}
