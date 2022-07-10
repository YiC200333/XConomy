package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iPlugin;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class CPlugin implements iPlugin {
    @Override
    public boolean getOnlinePlayersisEmpty() {
        return false;
    }

    @Override
    public void broadcastMessage(String message) {

    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {

    }

    @Override
    public void sendPluginMessage(String channel, ByteArrayOutputStream stream) {

    }
}
