package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iPlayer;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer implements iPlayer {
    @Override
    public void kickPlayer(String reason) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] message) {

    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
