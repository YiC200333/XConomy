package me.yic.xconomy.listeners;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

public class ConnectionListeners {

    @SuppressWarnings("unused")
    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        if (Sponge.getServer().getOnlinePlayers().size() == 1) {
            //Cache.clearCache();
        }
    }

    @SuppressWarnings("unused")
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player a = event.getTargetEntity();
        //DataCon.newPlayer(a);

        if (!XConomy.config.getNode("Settings","semi-online-mode").getBoolean()) {
            //   Cache.translateUUID(a.getName(), a);
        }
        //if (a.isOp()) {
         //   notifyUpdate(a);
        //}
    }


    private void notifyUpdate(Player player) {
        //if (!(XConomy.checkup() & Updater.old)) {
        //    return;
        //}
        //CommonPlayer.sendMessage(player,"§f[XConomy]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion);
        player.sendMessage(Text.of("§f[XConomy]§ahttps://www.spigotmc.org/resources/xconomy.75669/"));

        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage(Text.of("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html"));
        }

    }
}
