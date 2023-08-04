package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class CSender implements iSender {
    private final CommandSender sender;

    public CSender(CommandSender sender) {
        this.sender = sender;
    }


    @Override
    public boolean isOp() {
        return sender.isOp();
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public CPlayer toPlayer() {
        return new CPlayer((Player) sender);
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public void sendMessage(String message){
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] message){
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String per) {
        return sender.hasPermission(per);
    }
}
