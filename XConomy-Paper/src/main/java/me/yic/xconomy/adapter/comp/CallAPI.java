package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.api.event.NonPlayerAccountEvent;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import me.yic.xconomy.info.RecordInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public class CallAPI {
    public static void CallPlayerAccountEvent(UUID u, String name, BigDecimal bal, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
        Runnable event = () -> Bukkit.getPluginManager().callEvent(
                new PlayerAccountEvent(u, name, bal, amount, isAdd, ri.getCommand(), ri.getType()));
        Player player = Bukkit.getPlayer(u);
        if (player == null) {
            runGlobal(event);
        } else if (Bukkit.isOwnedByCurrentRegion(player)) {
            event.run();
        } else {
            // Keep account notifications even if the player retires before execution.
            if (player.getScheduler().run(XConomy.getInstance(), task -> event.run(), () -> runGlobal(event)) == null) {
                runGlobal(event);
            }
        }
    }

    public static void CallNonPlayerAccountEvent(String u, BigDecimal bal, BigDecimal amount, boolean isAdd, String type) {
        runGlobal(() -> Bukkit.getPluginManager().callEvent(new NonPlayerAccountEvent(u, bal, amount, isAdd, type)));
    }

    private static void runGlobal(Runnable action) {
        if (Bukkit.isGlobalTickThread()) {
            action.run();
        } else {
            Bukkit.getGlobalRegionScheduler().execute(XConomy.getInstance(), action);
        }
    }
}
