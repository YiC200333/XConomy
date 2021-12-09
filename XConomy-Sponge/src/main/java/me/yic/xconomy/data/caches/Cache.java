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
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.PlayerINFO;
import me.yic.xconomy.info.ServerINFO;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static Map<UUID, PlayerData> pds = new ConcurrentHashMap<>();
    public static Map<String, PlayerINFO> uid = new ConcurrentHashMap<>();
    public static Map<String, BigDecimal> baltop = new HashMap<>();
    public static List<String> baltop_papi = new ArrayList<>();
    public static BigDecimal sumbalance = BigDecimal.ZERO;

    public static void insertIntoCache(final UUID uuid, PlayerData pd) {
        pds.put(uuid, pd);
    }

    public static void updateIntoCache(final UUID uuid, PlayerData pd, BigDecimal newbalance) {
        pd.setbalance(newbalance);
        pds.put(uuid, pd);
    }

    @SuppressWarnings("all")
    public static void removefromCache(final UUID uuid) {
        if (pds.containsKey(uuid)) {
            pds.remove(uuid);
        }
    }

    public static void insertIntoUUIDCache(final String u, PlayerINFO v) {
        uid.put(u, v);
    }

    @SuppressWarnings("all")
    public static void removeFromUUIDCache(final String u) {
        String up = u;
        if (ServerINFO.IgnoreCase) {
            up = u.toLowerCase();
        }
        if (uid.containsKey(up)) {
            uid.remove(up);
        }
        if (ServerINFO.IsBungeeCordMode) {
            sendmessremoveCache(u);
        }
    }

    public static void refreshFromCache(final UUID uuid) {
        if (uuid != null) {
            DataCon.getBal(uuid);
        }
    }


    public static void clearCache() {
        pds.clear();
        uid.clear();
    }

    public static PlayerData getBalanceFromCacheOrDB(UUID u) {
        PlayerData pd = null;

        if (ServerINFO.disablecache){
            DataCon.getBal(u);
        }

        if (pds.containsKey(u)) {
            pd = pds.get(u);
        } else {
            DataCon.getBal(u);
            if (pds.containsKey(u)) {
                pd = pds.get(u);
            }
        }
        if (Sponge.getServer().getOnlinePlayers().size() == 0) {
            clearCache();
        }
        return pd;
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
    public static PlayerData cachecorrection(UUID u, BigDecimal amount, Boolean isAdd) {
        BigDecimal newvalue;
        PlayerData npd = getBalanceFromCacheOrDB(u);
        if (isAdd) {
            newvalue = npd.getbalance().add(amount);
        } else {
            newvalue = npd.getbalance().subtract(amount);
        }
        updateIntoCache(u, npd, newvalue);

        if (ServerINFO.IsBungeeCordMode) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("balance");
            output.writeUTF(XConomy.getSign());
            output.writeUTF(u.toString());
            output.writeUTF(amount.toString());
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> output.toByteArray());
        }
        return npd;
    }

    public static void change(String type, UUID u, BigDecimal amount, Boolean isAdd, String reason) {
        PlayerData pd = getBalanceFromCacheOrDB(u);
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getbalance();

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        updateIntoCache(u, pd, newvalue);
        if (DataBaseINFO.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            if (ServerINFO.IsBungeeCordMode) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> sendmessave(type, pd, isAdd, bal, amount, reason));
            } else {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataCon.save(type, pd, isAdd, bal, amount, reason));
            }
        } else {
            if (ServerINFO.IsBungeeCordMode) {
                sendmessave(type, pd, isAdd, bal, amount, reason);
            } else {
                DataCon.save(type, pd, isAdd, bal, amount, reason);
            }
        }
    }

    public static void changeall(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        pds.clear();

        if (DataBaseINFO.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataCon.saveall(targettype, type, amount, isAdd, reason));
        } else {
            DataCon.saveall(targettype, type, amount, isAdd, reason);
        }

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


    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public static User getplayer(String name) {
        UUID u = translateUUID(name, null);
        User mainp = null;
        if (u != null) {
            mainp = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(u)).get();
        }
        return mainp;
    }


    public static UUID translateUUID(String name, Player pp) {
        if (name == null) {
            return null;
        }

        String namep = name;
        if (ServerINFO.IgnoreCase) {
            namep = name.toLowerCase();
        }
        if (uid.containsKey(namep)) {
            return uid.get(namep).getUniqueId();
        } else {
            if (pp != null) {
                PlayerINFO pi = new PlayerINFO(pp.getUniqueId(), pp.getName());
                insertIntoUUIDCache(namep, pi);
                return uid.get(namep).getUniqueId();
            } else {
                DataCon.getUid(name);
                if (uid.containsKey(namep)) {
                    return uid.get(namep).getUniqueId();
                }
            }
        }
        return null;
    }


    public static String getrealname(String name) {
        String namep = name;
        if (ServerINFO.IgnoreCase) {
            namep = name.toLowerCase();
        }
        if (uid.containsKey(namep)) {
            return uid.get(namep).getName();
        }
        return "NULL";
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendmessave(String type, PlayerData pd, Boolean isAdd,
                                   BigDecimal oldbalance, BigDecimal amount, String command) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("balance");
        output.writeUTF(XConomy.getSign());
        output.writeUTF(pd.getUniqueId().toString());
        //output.writeUTF(newbalance.toString());
        SendMessTask(output, type, pd, isAdd, oldbalance, amount, command);

    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendmessaveall(String targettype, BigDecimal amount, Boolean isAdd) {
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
        SendMessTask(output, null, null, isAdd, null, null, null);

    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendmessremoveCache(String player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("updateplayer");
        output.writeUTF(XConomy.getSign());
        output.writeUTF(player);
        SendMessTask(output, null, null, null, null, null, null);
    }


    private static void SendMessTask(ByteArrayDataOutput stream, String type, PlayerData pd, Boolean isAdd,
                                     BigDecimal oldbalance, BigDecimal amount, String command) {
        if (!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        }
        if (pd != null) {
            DataCon.save(type, pd, isAdd, oldbalance, amount, command);
        }
    }

}
