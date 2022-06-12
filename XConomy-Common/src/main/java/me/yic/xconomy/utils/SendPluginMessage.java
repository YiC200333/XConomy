package me.yic.xconomy.utils;

import me.yic.xconomy.comp.Comp;
import java.io.ByteArrayOutputStream;

public class SendPluginMessage {
    public static void SendMessTask(String channel, ByteArrayOutputStream stream) {
        if (!Comp.getOnlinePlayersisEmpty()) {
            Comp.sendPluginMessage(channel, stream);
        }
    }
}
