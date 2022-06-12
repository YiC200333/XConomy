package me.yic.xconomy.comp;


import me.yic.xconomy.XConomy;
import org.spongepowered.api.Sponge;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class Comp {
    public static boolean getOnlinePlayersisEmpty(){
        return Sponge.getServer().getOnlinePlayers().isEmpty();
    }

    public static void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
    }
}
