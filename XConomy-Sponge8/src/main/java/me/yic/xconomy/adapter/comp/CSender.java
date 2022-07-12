package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.adapter.iSender;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

@SuppressWarnings("unused")
public class CSender implements iSender {
    private final CommandCause sender;

    public CSender(CommandCause sender) {
        this.sender = sender;
    }

    @Override
    public boolean isOp() {
        return sender.hasPermission("xconomy.op");
    }

    @Override
    public boolean isPlayer() {
        return sender.root() instanceof ServerPlayer;
    }

    @Override
    public CPlayer toPlayer() {
        return new CPlayer((ServerPlayer) sender.root());
    }

    @Override
    public String getName() {
        if (((ServerPlayer) sender.root()).profile().name().isPresent()){
            return ((ServerPlayer) sender.root()).profile().name().get();
        }
        return "";
    }

    @Override
    public boolean hasPermission(String per) {
        return sender.hasPermission(per);
    }

    @Override
    public void sendMessage(String message){
        if (isPlayer()){
            ((ServerPlayer) sender.root()).sendMessage(Component.text(message));
        }else{
            sender.sendMessage(Identity.nil(), Component.text(message));
        }
    }

    @Override
    public void sendMessage(String[] message){
        for (String mess : message) {
            if (isPlayer()) {
                ((ServerPlayer) sender.root()).sendMessage(Component.text(mess));
            }else{
                sender.sendMessage(Identity.nil(), Component.text(mess));
            }
        }
    }
}
