package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iPlugin;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Sponge.server().onlinePlayers().isEmpty();
    }

    @Override
    public void broadcastMessage(String message) {
        Sponge.server().broadcastAudience().sendMessage(Component.text(message));
    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        //Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                //Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
    }
}
