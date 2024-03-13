package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlayer;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer implements iPlayer {
    private final ServerPlayer player;

    public CPlayer(ServerPlayer player) {
        this.player = player;
    }

    public CPlayer(UUID uuid) {
        ServerPlayer player = null;
        if (Sponge.server().player(uuid).isPresent()) {
            player = Sponge.server().player(uuid).get();
        }
        this.player = player;
    }

    public CPlayer(User player) {
        ServerPlayer pl;
        if (player != null){
            if (player.player().isPresent()) {
                pl = player.player().get();
            }else{
                pl = null;
            }
        }else {
            pl = null;
        }
        this.player = pl;
    }

    @Override
    public boolean isOp() {
        return player.hasPermission("xconomy.op");
    }

    @Override
    public void kickPlayer(String reason){
        Sponge.asyncScheduler().executor(XConomy.getInstance().plugincontainer).execute(() -> player.kick(Component.text(reason)));
    }

    @Override
    public void sendMessage(String message){
        player.sendMessage(Component.text(message));
    }

    @Override
    public void sendMessage(String[] message){
        for (String mess : message) {
            player.sendMessage(Component.text(mess));
        }
    }

    @Override
    public boolean hasPermission(String per) {
        return player.hasPermission(per);
    }

    @Override
    public UUID getUniqueId(){
        return player.profile().uuid();
    }

    @Override
    public String getName(){
        if (player.profile().name().isPresent()){
            return player.profile().name().get();
        }
        return "";
    }

    @Override
    public boolean isOnline(){
        if (player == null){
            return false;
        }
        return player.isOnline();
    }
}
