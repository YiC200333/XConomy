package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.iPlugin;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.UUIDMode;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    private final HashMap<String, ChannelBinding.RawDataChannel> channels = new HashMap<>();
    private final HashMap<String, RawDataListener> channellisteners = new HashMap<>();

    @Override
    public CPlayer getplayer(PlayerData pd) {
        Optional<Object> p = Optional.empty();
        if (pd != null) {
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)){
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getName()));
            }else{
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getUniqueId()));
            }
        }
        return new CPlayer((User) p.orElse(null));
    }

    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Sponge.getServer().getOnlinePlayers().isEmpty();
    }

    @Override
    public List<UUID> getOnlinePlayersUUIDs() {
        List<UUID> ol = new ArrayList<>();
        for (Player pp : Sponge.getServer().getOnlinePlayers()) {
            ol.add(pp.getUniqueId());
        }
        return ol;
    }

    @Override
    public void broadcastMessage(String message) {
        Sponge.getServer().getBroadcastChannel().send(Text.of(message));
    }
    @Override
    public UUID NameToUUID(String name) {
        Optional<User> pu = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(name));
        if (!pu.isPresent()){
            return null;
        }
        Optional<Player> player = pu.get().getPlayer();
        if (!player.isPresent()){
            return null;
        }
        return player.get().getUniqueId();
    }
    @Override
    public boolean isSync() {
        return Thread.currentThread().getName().equalsIgnoreCase("Server thread");
    }
    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(runnable);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable ra, long time) {
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).schedule(ra, time, TimeUnit.MILLISECONDS);
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
