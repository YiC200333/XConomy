package me.yic.xconomy.listeners;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.ServerINFO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListeners implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() == 1) {
            Cache.clearCache();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player a = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> DataCon.newPlayer(a));

        if (!XConomy.config.getBoolean("Settings.semi-online-mode")) {
            Cache.translateUUID(a.getName(), a);
        }

        if (a.isOp()) {
            notifyUpdate(a);
        }
    }


    private void notifyUpdate(Player player) {
        if (!(XConomy.checkup() & Updater.old)) {
            return;
        }
        player.sendMessage("§f[XConomy]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion);
        player.sendMessage("§f[XConomy]§ahttps://www.spigotmc.org/resources/xconomy.75669/");

        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
        }

    }

}
