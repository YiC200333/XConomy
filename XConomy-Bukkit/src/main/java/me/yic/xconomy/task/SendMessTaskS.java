package me.yic.xconomy.task;

import com.google.common.io.ByteArrayDataOutput;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.utils.ServerINFO;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS {

    public static void Scheduler(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                                 BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () ->
                SendMess(stream, type, u, player, isAdd, balance, amount, newbalance, command));

    }

    public static void SendMess(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                                BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        if (ServerINFO.IsBungeeCordMode) {
            Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), "xconomy:acb", stream.toByteArray());
        }
        if (u != null) {
            DataCon.save(type, u, player, isAdd, balance, amount, newbalance, command);
        }
    }
}