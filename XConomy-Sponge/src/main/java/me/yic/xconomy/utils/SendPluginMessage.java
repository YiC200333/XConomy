package me.yic.xconomy.utils;

import com.google.common.io.ByteArrayDataOutput;
import me.yic.xconomy.XConomy;
import org.spongepowered.api.Sponge;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayDataOutput stream) {
        if (!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        }
    }
}
