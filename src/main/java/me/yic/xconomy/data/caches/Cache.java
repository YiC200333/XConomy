package me.yic.xconomy.data.caches;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.task.SendMessTaskS;

public class Cache {
    public static Map<UUID, BigDecimal> bal = new ConcurrentHashMap<>();
    public static Map<String, BigDecimal> baltop = new HashMap<>();
    public static List<String> baltop_papi = new ArrayList<>();
    public static Map<String, UUID> uid = new ConcurrentHashMap<>();
    public static BigDecimal sumbalance = BigDecimal.ZERO;

    public static void insertIntoCache(final UUID uuid, BigDecimal value) {
        if (value != null) {
            bal.put(uuid, value);
        }
    }

    public static void cacheUUID(final String u, UUID v) {
        uid.put(u, v);
    }

    public static void clearCache() {
        bal.clear();
        uid.clear();
    }

    public static BigDecimal getBalanceFromCacheOrDB(UUID u) {
        BigDecimal amount = BigDecimal.ZERO;
        if (bal.containsKey(u)) {
            amount = bal.get(u);
        } else {
            DataCon.getBal(u);
            if (bal.containsKey(u)) {
                amount = bal.get(u);
            }
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            clearCache();
        }
        return amount;
    }

    public static void change(UUID u, BigDecimal amount, Boolean isAdd, String type, String playername, String reason) {
        BigDecimal newvalue = amount;
        if (isAdd != null) {
            BigDecimal bal = getBalanceFromCacheOrDB(u);
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }
        insertIntoCache(u, newvalue);
        RecordData x = null;
        if (XConomy.config.getBoolean("Settings.mysql") && XConomy.config.getBoolean("Settings.transaction-record")) {
             x = new RecordData(type, u, playername, newvalue, amount, isAdd, reason);
        }
        if (XConomy.isBungeecord()) {
            sendmess(u, newvalue, amount, isAdd, x);
        } else {
            DataCon.save(u, amount, isAdd, x);
        }
    }

    public static void baltop() {
        baltop.clear();
        baltop_papi.clear();
        sumbal();
        DataCon.getTopBal();
    }

    public static void sumbal() {
        sumbalance = DataFormat.formatString(DataCon.getBalSum());
    }

    public static UUID translateUUID(String name) {
        if (uid.containsKey(name)) {
            return uid.get(name);
        } else {
            Player pp = Bukkit.getPlayerExact(name);
            if (pp != null) {
                uid.put(name, pp.getUniqueId());
                return uid.get(name);
            } else {
                DataCon.getUid(name);
                if (uid.containsKey(name)) {
                    return uid.get(name);
                }
            }
        }
        return null;
    }

    private static void sendmess(UUID u, BigDecimal amount, BigDecimal amountc, Boolean isAdd, RecordData x) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF("balance");
            output.writeUTF(XConomy.getSign());
            output.writeUTF(u.toString());
            output.writeUTF(amount.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new SendMessTaskS(stream, u, amountc, isAdd, x).runTaskAsynchronously(XConomy.getInstance());
    }

}
