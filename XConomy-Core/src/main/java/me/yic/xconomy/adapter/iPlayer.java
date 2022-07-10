package me.yic.xconomy.adapter;

import java.util.UUID;

@SuppressWarnings("unused")
public interface iPlayer {
    void kickPlayer(String reason);

    void sendMessage(String message);

    void sendMessage(String[] message);

    UUID getUniqueId();

    String getName();

    boolean isOnline();
}
