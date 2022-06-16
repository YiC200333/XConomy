package me.yic.xconomy.comp;



import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer {
    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public void kickPlayer(String reason){
        player.kick(Text.of(reason));
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
