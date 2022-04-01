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
import me.yic.xconomy.api.event.NonPlayerAccountEvent;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

public class DataCon{
    public static PlayerData getPlayerData(UUID uuid) {
        return getPlayerDatai(Cache.getSubUUID(uuid));
    }

    public static PlayerData getPlayerData(String username) {
        return getPlayerDatai(username);
    }

    public static BigDecimal getAccountBalance(String account) {
        return CacheNonPlayer.getBalanceFromCacheOrDB(account);
    }

    private static <T> PlayerData getPlayerDatai(T u) {
        PlayerData pd = null;

        if (XConomy.Config.DISABLE_CACHE) {
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
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Cache.clearCache();
        }
        if (pd == null) {
            return new PlayerData(null, "*", BigDecimal.ZERO);
        }
        return pd;
    }


    public static boolean hasaccountdatacache(String name) {
        return CacheNonPlayer.CacheContainsKey(name);

    }

    public static boolean containinfieldslist(String name) {
        if (XConomy.Config.NON_PLAYER_ACCOUNT_SUBSTRING != null) {
            for (String field : XConomy.Config.NON_PLAYER_ACCOUNT_SUBSTRING) {
                if (name.contains(field)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void changeplayerdata(String type, UUID uid, BigDecimal amount, Boolean isAdd, String reason) {
        UUID u = Cache.getSubUUID(uid);
        PlayerData pd = getPlayerData(u);
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getBalance();

        Bukkit.getScheduler().runTask(XConomy.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerAccountEvent(u, pd.getName(), bal, amount, isAdd, reason, type)));

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        Cache.updateIntoCache(u, pd, newvalue);
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            prepareudpmessage(type, u, pd, isAdd, amount, reason);
        } else {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> DataLink.save(type, pd, isAdd, amount, reason));
            } else {
                DataLink.save(type, pd, isAdd, amount, reason);
            }
        }
    }


    public static void changeaccountdata(final String u, final BigDecimal amount, final Boolean isAdd, final String type) {
        BigDecimal newvalue = amount;
        BigDecimal balance = CacheNonPlayer.getBalanceFromCacheOrDB(u);
        Bukkit.getScheduler().runTask(XConomy.getInstance(), () -> Bukkit.getPluginManager().callEvent(new NonPlayerAccountEvent(u, balance, amount, isAdd, type)));
        if (isAdd != null) {
            if (isAdd) {
                newvalue = balance.add(amount);
            } else {
                newvalue = balance.subtract(amount);
            }
        }
        CacheNonPlayer.insertIntoCache(u, newvalue);

        if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            final BigDecimal fnewvalue = newvalue;
            Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> DataLink.saveNonPlayer(type, u, amount, fnewvalue, isAdd));
        }else {
            DataLink.saveNonPlayer(type, u, amount, newvalue, isAdd);
        }
    }

    public static void changeallplayerdata(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        Cache.clearCache();

        if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> DataLink.saveall(targettype, type, amount, isAdd, reason));
        } else {
            DataLink.saveall(targettype, type, amount, isAdd, reason);
        }

        if (XConomy.Config.BUNGEECORD_ENABLE) {
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


    public static Player getplayer(String name) {
        PlayerData pd = getPlayerData(name);
        UUID u = pd.getUniqueId();
        Player mainp = null;
        if (u != null) {
            mainp = Bukkit.getPlayer(u);
            if (mainp == null && XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                UUID subu = CacheSemiOnline.CacheSubUUID_getsubuuid(u.toString());
                if (subu != null) {
                    Player subp = Bukkit.getPlayer(subu);
                    if (subp != null) {
                        return subp;
                    }
                }
            }
        }
        return mainp;
    }

    public static void prepareudpmessage(String type, UUID u, PlayerData pd, Boolean isAdd, BigDecimal amount, String reason) {
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> sendudpmessage(type, u, pd, isAdd, amount, reason));
            } else {
                sendudpmessage(type, u, pd, isAdd, amount, reason);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendudpmessage(String type, UUID u, PlayerData pd, Boolean isAdd, BigDecimal amount, String command) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(XConomy.Config.BUNGEECORD_SIGN);
        output.writeUTF(XConomy.syncversion);
        output.writeUTF("updateplayer");
        output.writeUTF(u.toString());
        SendMessTask(output, type, pd, isAdd, amount, command);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void sendallpdmessage(String targettype, BigDecimal amount, Boolean isAdd) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(XConomy.Config.BUNGEECORD_SIGN);
        output.writeUTF(XConomy.syncversion);
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

        SendPluginMessage.SendMessTask("xconomy:acb", stream);
        if (pd != null) {
            DataLink.save(type, pd, isAdd, amount, command);
        }
    }



}
