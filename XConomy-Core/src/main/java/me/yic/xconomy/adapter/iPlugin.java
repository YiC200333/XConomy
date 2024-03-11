package me.yic.xconomy.adapter;


import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.data.syncdata.PlayerData;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface iPlugin {
    CPlayer getplayer(PlayerData pd);
    boolean getOnlinePlayersisEmpty();

    List<UUID> getOnlinePlayersUUIDs();

    void broadcastMessage(String message);

    UUID NameToUUID(String name);

    boolean isSync();
    void runTaskAsynchronously(Runnable ra);

    void runTaskLaterAsynchronously(Runnable ra, long time);

    void sendPluginMessage(String channel, ByteArrayOutputStream stream);

    void registerIncomingPluginChannel(String channel, String classname);

    void registerOutgoingPluginChannel(String channel);

    void unregisterIncomingPluginChannel(String channel, String classname);

    void unregisterOutgoingPluginChannel(String channel);
}
