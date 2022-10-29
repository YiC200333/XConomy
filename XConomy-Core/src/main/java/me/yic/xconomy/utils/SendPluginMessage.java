package me.yic.xconomy.utils;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.syncdata.SyncData;

public class SendPluginMessage {
    public static void SendMessTask(String channel, SyncData sd) {
        if (!AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            AdapterManager.PLUGIN.sendPluginMessage(channel, sd.toByteArray(XConomy.syncversion));
        }
    }
}
