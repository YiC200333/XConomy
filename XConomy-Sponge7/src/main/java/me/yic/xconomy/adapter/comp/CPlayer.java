package me.yic.xconomy.adapter.comp;



import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.UUID;

@SuppressWarnings("unused")
public class CPlayer implements iPlayer {
    private final Player player;

    public CPlayer(Player player) {
        this.player = player;
    }

    public CPlayer(UUID uuid) {
        Player player = null;
        if (Sponge.getServer().getPlayer(uuid).isPresent()) {
            player = Sponge.getServer().getPlayer(uuid).get();
        }
        this.player = player;
    }

    public CPlayer(User player) {
        Player pl;
        if (player != null){
            if (player.getPlayer().isPresent()) {
                pl = player.getPlayer().get();
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
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> player.kick(Text.of(reason)));
    }

    @Override
    public void sendMessage(String message){
        player.sendMessage(Text.of(message));
    }

    @Override
    public void sendMessage(String[] message){
        for (String mess : message) {
            player.sendMessage(Text.of(mess));
        }
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
