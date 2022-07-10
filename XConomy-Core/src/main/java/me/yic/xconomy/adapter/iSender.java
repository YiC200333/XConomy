package me.yic.xconomy.adapter;

import me.yic.xconomy.adapter.comp.CPlayer;

@SuppressWarnings("unused")
public interface iSender {

    boolean isOp();

    boolean isPlayer();

    CPlayer toPlayer();

    String getName();

    boolean hasPermission(String per);

    void sendMessage(String message);

    void sendMessage(String[] message);
}
