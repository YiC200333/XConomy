package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.iPlugin;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.UUIDMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {

    @Override
    public CPlayer getplayer(PlayerData pd) {
        Player p = null;
        if (pd != null) {
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)){
                p = Bukkit.getPlayer(pd.getName());
            }else{
                p = Bukkit.getPlayer(pd.getUniqueId());
            }
        }
        return new CPlayer(p);
    }

    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Bukkit.getOnlinePlayers().isEmpty();
    }

    @Override
    public int getOnlinePlayerSize(){
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public List<UUID> getOnlinePlayersUUIDs() {
        List<UUID> ol = new ArrayList<>();
        for (Player pp : Bukkit.getOnlinePlayers()) {
            ol.add(pp.getUniqueId());
        }
        return ol;
    }

    @Override
    public void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            new CPlayer(player).sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public UUID NameToUUID(String name) {
        Player p = Bukkit.getPlayerExact(name);
        if (p == null){
            return null;
        }
        return p.getUniqueId();
    }

    @Override
    public boolean isSync() {
        return Bukkit.isPrimaryThread();
    }
    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(XConomy.getInstance(), scheduledTask -> runnable.run());
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable runnable, long time) {
        // The shared adapter supplies ticks; the async scheduler uses wall-clock time.
        Bukkit.getAsyncScheduler().runDelayed(XConomy.getInstance(), scheduledTask -> runnable.run(), time * 50L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        byte[] data = stream.toByteArray();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().run(XConomy.getInstance(), task ->
                    player.sendPluginMessage(XConomy.getInstance(), channel, data), null);
            return;
        }
    }

    @Override
    public void registerIncomingPluginChannel(String channel, String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            Object ob = clazz.getDeclaredConstructor().newInstance();
            Bukkit.getServer().getMessenger().registerIncomingPluginChannel(XConomy.getInstance(), channel, (PluginMessageListener) ob);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerOutgoingPluginChannel(String channel) {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(XConomy.getInstance(), channel);
    }

    @Override
    public void unregisterIncomingPluginChannel(String channel, String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            Object ob = clazz.getDeclaredConstructor().newInstance();
            Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(XConomy.getInstance(), channel, (PluginMessageListener) ob);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unregisterOutgoingPluginChannel(String channel) {
        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(XConomy.getInstance(), channel);
    }
}
