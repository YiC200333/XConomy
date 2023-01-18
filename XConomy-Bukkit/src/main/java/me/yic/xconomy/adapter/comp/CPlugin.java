package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {
    @Override
    public boolean getOnlinePlayersisEmpty(){
        return Bukkit.getOnlinePlayers().isEmpty();
    }

    @Override
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
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
