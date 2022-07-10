package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iSender;

@SuppressWarnings("unused")
public class CSender implements iSender {
    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public CPlayer toPlayer() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasPermission(String per) {
        return false;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String[] message) {

    }
}
