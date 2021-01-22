/*
 *  This file (Cache.java) is a part of project XConomy
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.xconomy.data.caches;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.task.SendMessTaskS;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    public static void insertIntoUUIDCache(final String u, UUID v) {
        uid.put(u, v);
    }

    @SuppressWarnings("all")
    public static void removeFromUUIDCache(final String u) {
        if (uid.containsKey(u)) {
            uid.remove(u);
        }
    }

    public static void refreshFromCache(final UUID uuid) {
        if (uuid != null) {
            DataCon.getBal(uuid);
        }
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
        if (Sponge.getServer().getOnlinePlayers().size() == 0) {
            clearCache();
        }
        return amount;
    }

    public static BigDecimal cachecorrection(UUID u, BigDecimal amount, Boolean isAdd) {
        BigDecimal newvalue;
        BigDecimal bal = getBalanceFromCacheOrDB(u);
        if (isAdd) {
            newvalue = bal.add(amount);
        } else {
            newvalue = bal.subtract(amount);
        }
        insertIntoCache(u, newvalue);

        if (ServerINFO.IsBungeeCordMode) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("balance");
            output.writeUTF(XConomy.getSign());
            output.writeUTF(u.toString());
            output.writeUTF(amount.toString());
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> output.toByteArray());
        }
        return newvalue;
    }

    public static void change(String type, UUID u, String playername, BigDecimal amount, Boolean isAdd, String reason) {
        BigDecimal newvalue = amount;
        BigDecimal bal = getBalanceFromCacheOrDB(u);
        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }
        insertIntoCache(u, newvalue);
        presavedata(type, u, playername, isAdd, bal, amount, newvalue, reason);

    }

    public static void changeall(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        bal.clear();
        DataCon.saveall(targettype, type, amount, isAdd, reason);
        if (ServerINFO.IsBungeeCordMode) {
            sendmessaveall(targettype, amount, isAdd);
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


    public static User getplayer(String name) {
        UUID u = translateUUID(name, null);
        User mainp = null;
        if (u != null) {
            mainp = Sponge.getServiceManager().provide(UserStorageService.class).get().get(u).get();
        }
        return mainp;
    }


    public static UUID translateUUID(String name, Player pp) {
        if (uid.containsKey(name)) {
            return uid.get(name);
        } else {
            if (pp != null) {
                insertIntoUUIDCache(name, pp.getUniqueId());
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

    private static void presavedata(String type, UUID u, String player, Boolean isAdd,
                                    BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String reason) {

        if (ServerINFO.IsBungeeCordMode) {
            sendmessave(type, u, player, isAdd, balance, amount, newbalance, reason);
        } else {
            if (ServerINFO.RequireAsyncRun) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() ->
                        DataCon.save(type, u, player, isAdd, balance, amount, newbalance, reason));
            } else {
                DataCon.save(type, u, player, isAdd, balance, amount, newbalance, reason);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void sendmessave(String type, UUID u, String player, Boolean isAdd,
                                    BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("balance");
        output.writeUTF(XConomy.getSign());
        output.writeUTF(u.toString());
        output.writeUTF(newbalance.toString());
        if (ServerINFO.RequireAsyncRun) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(new SendMessTaskS(
                    output, type, u, player, isAdd, balance, amount, newbalance, command));
        } else {
            SendMessTask(output, type, u, player, isAdd, balance, amount, newbalance, command);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void sendmessaveall(String targettype, BigDecimal amount, Boolean isAdd) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("balanceall");
        output.writeUTF(XConomy.getSign());
        if (targettype.equals("all")) {
            output.writeUTF("all");
        } else if (targettype.equals("online")) {
            output.writeUTF("online");
        }
        output.writeUTF(amount.toString());
        if (isAdd) {
            output.writeUTF("add");
        } else {
            output.writeUTF("subtract");
        }
        if (ServerINFO.RequireAsyncRun) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(new SendMessTaskS(
                    output, null, null, null, isAdd, null, null, null, null));
        } else {
            SendMessTask(output, null, null, null, isAdd, null, null, null, null);
        }
    }


    public static void SendMessTask(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                                    BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        if (u != null) {
            DataCon.save(type, u, player, isAdd, balance, amount, newbalance, command);
        }
    }

}
