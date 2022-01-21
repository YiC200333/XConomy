/*
 *  This file (DataCon.java) is a part of project XConomy
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
package me.yic.xconomy.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.info.ServerINFO;
import me.yic.xconomy.utils.PlayerData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.math.BigDecimal;
import java.util.UUID;

public class DataCon{

    public static <T> PlayerData getPlayerData(T u) {
        PlayerData pd = null;

        if (ServerINFO.disablecache) {
            DataLink.getPlayerData(u);
        }

        if (Cache.CacheContainsKey(u)) {
            pd = Cache.getDataFromCache(u);
        } else {
            DataLink.getPlayerData(u);
            if (Cache.CacheContainsKey(u)) {
                pd = Cache.getDataFromCache(u);
            }
        }
        if (Sponge.getServer().getOnlinePlayers().size() == 0) {
            Cache.clearCache();
        }
        if (pd == null) {
            return new PlayerData(null, "*", BigDecimal.ZERO);
        }
        return pd;
    }


    //public static void refreshFromCache(UUID uuid) {
    //    if (uuid != null) {
    //        DataLink.getPlayerData(uuid);
    //    }
    //}

    //@SuppressWarnings("UnstableApiUsage")
    //public static PlayerData cachecorrection(UUID u, BigDecimal amount, Boolean isAdd) {
    //    BigDecimal newvalue;
    //    PlayerData npd = getPlayerData(u);
    //    if (isAdd) {
    //        newvalue = npd.getbalance().add(amount);
    //    } else {
    //        newvalue = npd.getbalance().subtract(amount);
    //    }
    //    Cache.updateIntoCache(u, npd, newvalue);

    //    if (ServerINFO.IsBungeeCordMode) {
    //        ByteArrayDataOutput output = ByteStreams.newDataOutput();
    //        output.writeUTF("balance");
    //        output.writeUTF(XConomy.getSign());
    //        output.writeUTF(u.toString());
    //        output.writeUTF(amount.toString());
    //        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), "xconomy:acb", output.toByteArray());
    //    }
    //    return npd;
    //}


    public static void change(String type, UUID u, BigDecimal amount, Boolean isAdd, String reason) {
        PlayerData pd = getPlayerData(u);
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getbalance();

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        Cache.updateIntoCache(u, pd, newvalue);
        if (ServerINFO.IsBungeeCordMode) {
            prepareudpmessage(type, u, pd, isAdd, amount, reason);
        } else {
            if (DataBaseINFO.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.save(type, pd, isAdd, amount, reason));
            } else {
                DataLink.save(type, pd, isAdd, amount, reason);
            }
        }
    }

    public static void changeall(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        Cache.clearCache();

        if (DataBaseINFO.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.saveall(targettype, type, amount, isAdd, reason));
        } else {
            DataLink.saveall(targettype, type, amount, isAdd, reason);
        }

        if (ServerINFO.IsBungeeCordMode) {
            sendallpdmessage(targettype, amount, isAdd);
        }
    }

    public static void baltop() {
        Cache.baltop.clear();
        Cache.baltop_papi.clear();
        sumbal();
        DataLink.getTopBal();
    }

    public static void sumbal() {
        Cache.sumbalance = DataFormat.formatString(DataLink.getBalSum());
    }


    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    public static User getplayer(String name) {
        PlayerData pd = getPlayerData(name);
        UUID u = pd.getUniqueId();
        User mainp = null;
        if (u != null) {
            mainp = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(u)).get();
        }
        return mainp;
    }


    public static void prepareudpmessage(String type, UUID u, PlayerData pd, Boolean isAdd, BigDecimal amount, String reason) {
        if (ServerINFO.IsBungeeCordMode) {
            if (DataBaseINFO.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> sendudpmessage(type, u, pd, isAdd, amount, reason));
            } else {
                sendudpmessage(type, u, pd, isAdd, amount, reason);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendudpmessage(String type, UUID u, PlayerData pd, Boolean isAdd, BigDecimal amount, String command) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(XConomy.getSign());
        output.writeUTF("updateplayer");
        output.writeUTF(u.toString());
        SendMessTask(output, type, pd, isAdd, amount, command);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendallpdmessage(String targettype, BigDecimal amount, Boolean isAdd) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(XConomy.getSign());
        output.writeUTF("balanceall");
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
        SendMessTask(output, null, null, isAdd, null, null);

    }

    private static void SendMessTask(ByteArrayDataOutput stream, String type, PlayerData pd, Boolean isAdd, BigDecimal amount, String command) {

        if (!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        }
        if (pd != null) {
            DataLink.save(type, pd, isAdd, amount, command);
        }
    }
}
