package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.iPlugin;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.UUIDMode;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.network.EngineConnectionSide;
import org.spongepowered.api.network.ServerSideConnection;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.api.network.channel.raw.play.RawPlayDataHandler;
import org.spongepowered.api.util.Identifiable;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    private final HashMap<String, RawDataChannel> channels = new HashMap<>();
    private final HashMap<String, RawPlayDataHandler<ServerSideConnection>> channellisteners = new HashMap<>();

    @Override
    public CPlayer getplayer(PlayerData pd) {
        Optional<User> p = Optional.empty();
        if (pd != null) {
            try {
                if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    p = Sponge.server().userManager().load(pd.getName()).get();
                } else {
                    p = Sponge.server().userManager().load(pd.getUniqueId()).get();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new CPlayer(p.orElse(null));
    }

    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Sponge.server().onlinePlayers().isEmpty();
    }

    @Override
    public List<UUID> getOnlinePlayersUUIDs() {
        List<UUID> ol = new ArrayList<>();
        for (Player pp : Sponge.server().onlinePlayers()) {
            ol.add(pp.profile().uuid());
        }
        return ol;
    }

    @Override
    public void broadcastMessage(String message) {
        Sponge.server().broadcastAudience().sendMessage(Component.text(message));
    }

    @Override
    public UUID NameToUUID(String name) {
        try {
            Optional<User> pu = Sponge.server().userManager().load(name).get();
            if (!pu.isPresent()) {
                return null;
            }
            Optional<ServerPlayer> player = pu.get().player();
            return player.map(Identifiable::uniqueId).orElse(null);
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

        @Override
    public boolean isSync() {
        return Thread.currentThread().getName().equalsIgnoreCase("Server thread");
    }
    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Sponge.asyncScheduler().executor(XConomy.getInstance().plugincontainer).execute(runnable);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long time) {
        Sponge.asyncScheduler().executor(XConomy.getInstance().plugincontainer).schedule(runnable, time, TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        //Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), channel).sendTo(
                //Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
    }

    @Override
    public void registerIncomingPluginChannel(String channel, String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            Object ob = clazz.getDeclaredConstructor().newInstance();
            RawDataChannel rawchannel = Sponge.channelManager().ofType(ResourceKey.resolve(channel), RawDataChannel.class);
            channels.put(channel, rawchannel);
            RawPlayDataHandler<ServerSideConnection> rawchannellistener = (RawPlayDataHandler<ServerSideConnection>) ob;
            channellisteners.put(classname, rawchannellistener);
            rawchannel.play().addHandler(EngineConnectionSide.SERVER, rawchannellistener);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registerOutgoingPluginChannel(String channel) {
        Sponge.channelManager().ofType(ResourceKey.resolve(channel), RawDataChannel.class);
    }

    @Override
    public void unregisterIncomingPluginChannel(String channel, String classname) {
        if (channels.containsKey(channel)) {
            if (channellisteners.containsKey(classname)) {
                channels.get(channel).play().removeHandler(channellisteners.get(classname));
                channellisteners.remove(classname);
            }
            channels.remove(channel);
        }
    }

    @Override
    public void unregisterOutgoingPluginChannel(String channel) {

    }
}
