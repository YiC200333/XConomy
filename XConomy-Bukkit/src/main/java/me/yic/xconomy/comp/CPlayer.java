package me.yic.xconomy.comp;


import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer {
    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public void kickPlayer(String reason){
        player.kickPlayer(reason);
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
