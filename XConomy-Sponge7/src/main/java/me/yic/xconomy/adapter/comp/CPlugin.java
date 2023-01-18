package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlugin;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    private final HashMap<String, ChannelBinding.RawDataChannel> channels = new HashMap<>();
    private final HashMap<String, RawDataListener> channellisteners = new HashMap<>();

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

    @Override
    public void registerIncomingPluginChannel(String channel, String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            Object ob = clazz.getDeclaredConstructor().newInstance();
            ChannelBinding.RawDataChannel rawchannel = Sponge.getChannelRegistrar().createRawChannel(XConomy.getInstance(), channel);
            channels.put(channel, rawchannel);
            RawDataListener rawchannellistener = (RawDataListener) ob;
            channellisteners.put(classname, rawchannellistener);
            rawchannel.addListener(Platform.Type.SERVER, rawchannellistener);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registerOutgoingPluginChannel(String channel) {
        Sponge.getChannelRegistrar().createRawChannel(XConomy.getInstance(), channel);
    }

    @Override
    public void unregisterIncomingPluginChannel(String channel, String classname) {
        if (channels.containsKey(channel)) {
            if (channellisteners.containsKey(classname)) {
                channels.get(channel).removeListener(channellisteners.get(classname));
                channellisteners.remove(classname);
            }
            channels.remove(channel);
        }
    }

    @Override
    public void unregisterOutgoingPluginChannel(String channel) {

    }
}
