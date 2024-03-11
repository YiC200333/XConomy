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
    public List<UUID> getOnlinePlayersUUIDs() {
        List<UUID> ol = new ArrayList<>();
        for (Player pp : Bukkit.getOnlinePlayers()) {
            ol.add(pp.getUniqueId());
        }
        return ol;
    }

    @Override
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
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
    public void runTaskAsynchronously(Runnable ra) {
        Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), ra);
    }

    @Override
    public void runTaskLaterAsynchronously(Runnable ra, long time) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(XConomy.getInstance(), ra, time);
    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), channel, stream.toByteArray());
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
