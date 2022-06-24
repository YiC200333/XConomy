package me.yic.xconomy.comp;


import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer {
    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public void kickPlayer(String reason){
        Bukkit.getScheduler().runTask(XConomy.getInstance(), ()->player.kickPlayer(reason));
    }

    public void sendMessage(String message){
        player.sendMessage(message);
    }

    public void sendMessage(String[] message){
        player.sendMessage(message);
    }

    public UUID getUniqueId(){
        return player.getUniqueId();
    }

    public String getName(){
        return player.getName();
    }

    public boolean isOnline(){
        if (player == null){
            return false;
        }
        return player.isOnline();
    }
}
