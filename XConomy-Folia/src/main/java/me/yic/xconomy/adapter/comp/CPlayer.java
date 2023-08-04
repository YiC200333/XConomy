package me.yic.xconomy.adapter.comp;


import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer implements iPlayer {
    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public CPlayer(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void kickPlayer(String reason){
        Bukkit.getScheduler().runTask(XConomy.getInstance(), ()->player.kickPlayer(reason));
    }

    @Override
    public void sendMessage(String message){
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] message){
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String per) {
        return player.hasPermission(per);
    }

    @Override
    public UUID getUniqueId(){
        return player.getUniqueId();
    }

    @Override
    public String getName(){
        return player.getName();
    }

    @Override
    public boolean isOnline(){
        if (player == null){
            return false;
        }
        return player.isOnline();
    }
}
