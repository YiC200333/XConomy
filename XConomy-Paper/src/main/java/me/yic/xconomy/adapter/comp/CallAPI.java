package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.api.event.NonPlayerAccountEvent;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import me.yic.xconomy.info.RecordInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public class CallAPI {
    private final static Random random = new Random();

    public static void CallPlayerAccountEvent(UUID u, String name, BigDecimal bal, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
        if (AdapterManager.checkisMainThread()) {
            Bukkit.getPluginManager().callEvent(new PlayerAccountEvent(u, name, bal, amount, isAdd, ri.getCommand(), ri.getType()));
        } else {
            Bukkit.getRegionScheduler().run(XConomy.getInstance(), getThreadLocation(), scheduledTask -> Bukkit.getPluginManager().callEvent(new PlayerAccountEvent(u, name, bal, amount, isAdd, ri.getCommand(), ri.getType())));
        }
    }

    public static void CallNonPlayerAccountEvent(String u, BigDecimal bal, BigDecimal amount, boolean isAdd, String type) {
        if (AdapterManager.checkisMainThread()) {
            Bukkit.getPluginManager().callEvent(new NonPlayerAccountEvent(u, bal, amount, isAdd, type));
        } else {
            Bukkit.getRegionScheduler().run(XConomy.getInstance(), getThreadLocation(), scheduledTask -> Bukkit.getPluginManager().callEvent(new NonPlayerAccountEvent(u, bal, amount, isAdd, type)));
        }
    }

    private static Location getThreadLocation() {
        String worldname = "world";
        int rx = XConomyLoad.Config.RE_X;
        int ry = XConomyLoad.Config.RE_Y;
        if (XConomyLoad.Config.RE_WORLD != null) {
            worldname = XConomyLoad.Config.RE_WORLD;
        }
        if (rx <= 512) {
            rx = 512;
        }
        if (ry <= 512) {
            ry = 512;
        }
        return new Location(Bukkit.getWorld(worldname), random.nextInt(rx * 2) - rx, random.nextInt(ry * 2) - ry, 0);
    }
}
