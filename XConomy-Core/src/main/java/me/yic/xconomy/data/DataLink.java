/*
 *  This file (DataLink.java) is a part of project XConomy
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
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.data.redis.RedisThread;
import me.yic.xconomy.data.sql.*;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.utils.RedisConnection;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class DataLink{
    public static boolean hasnonplayerplugin = false;

    public static boolean create() {
        switch (XConomyLoad.DConfig.getStorageType()) {
            case 1:
                XConomy.getInstance().logger("数据保存方式", 0, " - SQLite");
                SQLSetup.setupSqLiteAddress();

                File dataFolder = XConomy.getInstance().getPDataFolder();
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    XConomy.getInstance().logger("文件夹创建异常", 1, null);
                    return false;
                }
                break;

            case 2:
                XConomy.getInstance().logger("数据保存方式", 0, " - MySQL");
                SQLSetup.setupMySqlTable();
                break;

            case 3:
                XConomy.getInstance().logger("数据保存方式", 0, " - MariaDB");
                SQLSetup.setupMySqlTable();
                break;

        }

        if (SQL.con()) {
            if (XConomyLoad.DConfig.getStorageType() == 2 || XConomyLoad.DConfig.getStorageType() == 3) {
                SQL.getwaittimeout();
            }
            SQL.createTable();
            SQLUpdateTable.updataTable();
            SQLUpdateTable.updataTable_record();
            XConomyLoad.DConfig.loggersysmess("连接正常");
        } else {
            XConomyLoad.DConfig.loggersysmess("连接异常");
            return false;
        }


        if (XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            if (RedisConnection.connectredis()) {
                RedisThread rThread = new RedisThread();
                rThread.start();
            } else {
                return false;
            }
        }

        ImportData.isExitsFile();

        XConomy.getInstance().logger("XConomy加载成功", 0, null);
        return true;
    }

    public static void updatelogininfo(UUID uid) {
        if (XConomyLoad.DConfig.canasync) {
            AdapterManager.runTaskAsynchronously(() -> SQLLogin.updatelogininfo(uid));
        } else {
            SQLLogin.updatelogininfo(uid);
        }
    }

    public static void selectlogininfo(CPlayer pp) {
        if (XConomyLoad.DConfig.canasync) {
            AdapterManager.runTaskLaterAsynchronously(() -> SQLLogin.getPlayerlogin(pp), 1L);
        } else {
            SQLLogin.getPlayerlogin(pp);
        }
    }

    public static void deletePlayerData(UUID u) {
        SQL.deletePlayerData(u.toString());
    }

    public static BigDecimal getBalNonPlayer(String u) {
        if (AdapterManager.checkisMainThread()) {
            try {
                return CompletableFuture.supplyAsync(() -> SQL.getNonPlayerData(u)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }else{
            return SQL.getNonPlayerData(u);
        }
    }

    public static void getTopBal() {
        SQL.getBaltop();
    }

    public static void setTopBalHide(UUID u, int type) {
        SQL.hidetop(u, type);
    }

    public static String getBalSum() {
        if (SQL.sumBal() == null) {
            return "0.0";
        }
        return SQL.sumBal();
    }

    public static void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        SQL.save(pd, isAdd, amount, ri);
    }

    public static void newPlayer(CPlayer a) {
        SQLCreateNewAccount.newPlayer(a);
    }

    public static boolean newPlayer(UUID uid, String name) {
        return SQLCreateNewAccount.newPlayer(uid, name, null);
    }

    public static boolean newAccount(String name) {
        return SQLCreateNewAccount.createNonPlayerAccount(name);
    }

    public static <T> PlayerData getPlayerData(T key) {
        if (AdapterManager.checkisMainThread()) {
            try {
                return CompletableFuture.supplyAsync(() -> exgetPlayerData(key)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }else{
            return exgetPlayerData(key);
        }
    }

    private static <T> PlayerData exgetPlayerData(T key) {
        if (key instanceof UUID) {
            return SQL.getPlayerData((UUID) key);
        } else if (key instanceof String) {
            return SQL.getPlayerData((String) key);
        }
        return null;
    }

    public static void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
        AdapterManager.runTaskAsynchronously(() -> {
            if (targettype.equalsIgnoreCase("all")) {
                SQL.saveall(targettype, null, amount, isAdd, ri);
            } else if (targettype.equalsIgnoreCase("online")) {
                List<UUID> ol = AdapterManager.PLUGIN.getOnlinePlayersUUIDs();
                SQL.saveall(targettype, ol, amount, isAdd, ri);
            }
        });
    }

    public static void saveNonPlayer(String account, BigDecimal amount,
                               BigDecimal newbalance, Boolean isAdd, RecordInfo ri){
        SQL.saveNonPlayer(account, amount, newbalance, isAdd, ri);
    }
}
