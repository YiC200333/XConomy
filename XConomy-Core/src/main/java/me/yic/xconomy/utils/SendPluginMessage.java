package me.yic.xconomy.utils;

import me.yic.xconomy.AdapterManager;

import java.io.ByteArrayOutputStream;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayOutputStream stream) {
        if (!AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            AdapterManager.PLUGIN.sendPluginMessage(channel, stream);
        }
    }
}
