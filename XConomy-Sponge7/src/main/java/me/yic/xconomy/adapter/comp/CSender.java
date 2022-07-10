package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iSender;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@SuppressWarnings("unused")
public class CSender implements iSender {
    private final CommandSource sender;

    public CSender(CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public boolean isOp() {
        return sender.hasPermission("xconomy.op");
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
    public boolean hasPermission(String per) {
        return sender.hasPermission(per);
    }

    @Override
    public void sendMessage(String message){
        sender.sendMessage(Text.of(message));
    }

    @Override
    public void sendMessage(String[] message){
        for (String mess : message) {
            sender.sendMessage(Text.of(mess));
        }
    }
}
