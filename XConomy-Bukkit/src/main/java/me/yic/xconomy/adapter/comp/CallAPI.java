package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.api.event.NonPlayerAccountEvent;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import me.yic.xconomy.info.RecordInfo;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public class CallAPI {

    public static void CallPlayerAccountEvent(UUID u, String name, BigDecimal bal, BigDecimal amount, Boolean isAdd, RecordInfo ri){
        Bukkit.getScheduler().runTask(XConomy.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerAccountEvent(u, name, bal, amount, isAdd, ri.getCommand(), ri.getType())));
    }

    public static void CallNonPlayerAccountEvent(String u, BigDecimal bal, BigDecimal amount, boolean isAdd, String type){
        Bukkit.getScheduler().runTask(XConomy.getInstance(), () -> Bukkit.getPluginManager().callEvent(new NonPlayerAccountEvent(u, bal, amount, isAdd, type)));
    }

}
