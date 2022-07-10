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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.comp.CPlugin;
import me.yic.xconomy.adapter.comp.CallAPI;
import me.yic.xconomy.adapter.comp.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.data.syncdata.SyncBalanceAll;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

public class DataCon {
    private static final DataLink DataLink = AdapterManager.DATALINK;
    private static final CPlugin plu = AdapterManager.PLUGIN;

    public static PlayerData getPlayerData(UUID uuid) {
        return getPlayerDatai(uuid);
    }

    public static PlayerData getPlayerData(String username) {
        return getPlayerDatai(username);
    }

    public static BigDecimal getAccountBalance(String account) {
        if (XConomy.Config.DISABLE_CACHE){
            DataLink.getBalNonPlayer(account);
        }
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
        if (plu.getOnlinePlayersisEmpty()) {
            Cache.clearCache();
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

    public static void changeplayerdata(final String type, final UUID uid, final BigDecimal amount, final Boolean isAdd, final String command, final Object comment) {
        PlayerData pd = getPlayerData(uid);
        UUID u = pd.getUniqueId();
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getBalance();

        RecordInfo ri = new RecordInfo(type, command, comment);

        CallAPI.CallPlayerAccountEvent(u, pd.getName(), bal, amount, isAdd, ri);

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        Cache.updateIntoCache(u, pd, newvalue);
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            prepareudpmessage(pd, isAdd, amount, ri);
        } else {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                plu.runTaskAsynchronously(() -> DataLink.save(pd, isAdd, amount, ri));
            } else {
                DataLink.save(pd, isAdd, amount, ri);
            }
        }
    }


    @SuppressWarnings("ConstantConditions")
    public static void changeaccountdata(final String type, final String u, final BigDecimal amount, final Boolean isAdd, final String command) {
        BigDecimal newvalue = amount;
        BigDecimal balance = CacheNonPlayer.getBalanceFromCacheOrDB(u);

        RecordInfo ri = new RecordInfo(type, command, null);

        CallAPI.CallNonPlayerAccountEvent(u, balance, amount, isAdd, type);
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
            plu.runTaskAsynchronously(() -> DataLink.saveNonPlayer(u, amount, fnewvalue, isAdd, ri));
        } else {
            DataLink.saveNonPlayer(u, amount, newvalue, isAdd, ri);
        }
    }

    public static void changeallplayerdata(String targettype, String type, BigDecimal amount, Boolean isAdd, String command, StringBuilder comment) {
        Cache.clearCache();

        RecordInfo ri = new RecordInfo(type, command, comment);

        if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            plu.runTaskAsynchronously(() -> DataLink.saveall(targettype, amount, isAdd, ri));
        } else {
            DataLink.saveall(targettype, amount, isAdd, ri);
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


    public static void prepareudpmessage(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                plu.runTaskAsynchronously(() -> sendudpmessage(pd, isAdd, amount, ri));
            } else {
                sendudpmessage(pd, isAdd, amount, ri);
            }
        }
    }

    public static void sendudpmessage(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            oos.writeObject(pd);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessTask(output, pd, isAdd, amount, ri);
    }

    public static void sendallpdmessage(String targettype, BigDecimal amount, Boolean isAdd) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            if (targettype.equals("all")) {
                oos.writeObject(new SyncBalanceAll(XConomy.Config.BUNGEECORD_SIGN, true, isAdd, amount));
            } else if (targettype.equals("online")) {
                oos.writeObject(new SyncBalanceAll(XConomy.Config.BUNGEECORD_SIGN, false, isAdd, amount));
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessTask(output, null, isAdd, null, null);

    }

    private static void SendMessTask(ByteArrayOutputStream stream, PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {

        SendPluginMessage.SendMessTask("xconomy:acb", stream);
        if (pd != null) {
            DataLink.save(pd, isAdd, amount, ri);
        }
    }

}
