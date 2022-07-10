package me.yic.xconomy.adapter;


import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public interface iPlugin {
    boolean getOnlinePlayersisEmpty();

    void broadcastMessage(String message);

    void runTaskAsynchronously(Runnable runnable);

    void sendPluginMessage(String channel, ByteArrayOutputStream stream);
}
