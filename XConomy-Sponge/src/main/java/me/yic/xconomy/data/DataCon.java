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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.data.syncdata.SyncBalanceAll;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Optional;
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
        if (Sponge.getServer().getOnlinePlayers().size() == 0) {
            Cache.clearCache();
        }
        return pd;
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


    public static void changeplayerdata(String type, UUID u, BigDecimal amount, Boolean isAdd, String reason) {
        PlayerData pd = getPlayerData(u);
        BigDecimal newvalue = amount;
        BigDecimal bal = pd.getBalance();

        if (isAdd != null) {
            if (isAdd) {
                newvalue = bal.add(amount);
            } else {
                newvalue = bal.subtract(amount);
            }
        }

        Cache.updateIntoCache(u, pd, newvalue);
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            prepareudpmessage(type, pd, isAdd, amount, reason);
        } else {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.save(type, pd, isAdd, amount, reason));
            } else {
                DataLink.save(type, pd, isAdd, amount, reason);
            }
        }
    }

    public static void changeaccountdata(final String u, final BigDecimal amount, final Boolean isAdd, final String type) {
        BigDecimal newvalue = amount;
        BigDecimal balance = CacheNonPlayer.getBalanceFromCacheOrDB(u);
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
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.saveNonPlayer(type, u, amount, fnewvalue, isAdd));
        }else {
            DataLink.saveNonPlayer(type, u, amount, newvalue, isAdd);
        }
    }


    public static void changeallplayerdata(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        Cache.clearCache();

        if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.saveall(targettype, type, amount, isAdd, reason));
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


    public static User getplayer(String name) {
        PlayerData pd = getPlayerData(name);
        UUID u = pd.getUniqueId();
        Optional<Object> mainp = Optional.empty();
        if (u != null) {
            mainp = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(u));
            if (!mainp.isPresent() && XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                UUID subu = CacheSemiOnline.CacheSubUUID_getsubuuid(u.toString());
                if (subu != null) {
                    Optional<Object> subp = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(subu));
                    return (User) subp.orElse(null);
                }
            }
        }
        return (User) mainp.orElse(null);
    }


    public static void prepareudpmessage(String type, PlayerData pd, Boolean isAdd, BigDecimal amount, String reason) {
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            if (XConomy.DConfig.canasync && Thread.currentThread().getName().equalsIgnoreCase("Server thread")) {
                Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> sendudpmessage(type, pd, isAdd, amount, reason));
            } else {
                sendudpmessage(type, pd, isAdd, amount, reason);
            }
        }
    }


    public static void sendudpmessage(String type, PlayerData pd, Boolean isAdd, BigDecimal amount, String command) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            oos.writeObject(pd);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessTask(output, type, pd, isAdd, amount, command);
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
        SendMessTask(output, null, null, isAdd, null, null);

    }

    private static void SendMessTask(ByteArrayOutputStream stream, String type, PlayerData pd, Boolean isAdd, BigDecimal amount, String command) {

        SendPluginMessage.SendMessTask("xconomy:acb", stream);
        if (pd != null) {
            DataLink.save(type, pd, isAdd, amount, command);
        }
    }
}
