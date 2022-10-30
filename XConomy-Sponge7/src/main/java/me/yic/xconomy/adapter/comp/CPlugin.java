package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Sponge.getServer().getOnlinePlayers().isEmpty();
    }

    @Override
    public void broadcastMessage(String message) {
        Sponge.getServer().getBroadcastChannel().send(Text.of(message));
    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
    }
}
