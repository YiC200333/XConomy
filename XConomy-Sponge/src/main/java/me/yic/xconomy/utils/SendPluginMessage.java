package me.yic.xconomy.utils;

import me.yic.xconomy.XConomy;
import org.spongepowered.api.Sponge;

import java.io.ByteArrayOutputStream;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayOutputStream stream) {
        if (!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        }
    }
}
