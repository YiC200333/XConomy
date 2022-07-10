package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class Comp {
    public static boolean getOnlinePlayersisEmpty(){
        return Bukkit.getOnlinePlayers().isEmpty();
    }

    public static void sendPluginMessage(String channel, ByteArrayOutputStream stream){
        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), channel, stream.toByteArray());
    }
}
