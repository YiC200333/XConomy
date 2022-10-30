package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlugin;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;

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
}
