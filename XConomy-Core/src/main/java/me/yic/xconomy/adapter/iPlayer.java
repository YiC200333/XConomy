package me.yic.xconomy.adapter;

import java.util.UUID;

@SuppressWarnings("unused")
public interface iPlayer {

    boolean isOp();

    void kickPlayer(String reason);

    void sendMessage(String message);

    void sendMessage(String[] message);

    boolean hasPermission(String per);

    UUID getUniqueId();

    String getName();

    boolean isOnline();
}
