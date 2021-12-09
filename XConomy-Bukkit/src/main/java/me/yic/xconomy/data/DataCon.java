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
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.data.sql.SQLCreateNewAccount;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCon extends DataBaseINFO {

    public static boolean create() {
        switch (getStorageType()) {
            case 1:
                XConomy.getInstance().logger("数据保存方式", " - SQLite");
                setupSqLiteAddress();

                File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    XConomy.getInstance().logger("文件夹创建异常", null);
                    return false;
                }
                break;

            case 2:
                XConomy.getInstance().logger("数据保存方式", " - MySQL");
                setupMySqlTable();
                break;

        }

        if (SQL.con()) {
            if (getStorageType() == 2) {
                SQL.getwaittimeout();
            }
            SQL.createTable();
            SQL.updataTable();
            loggersysmess("连接正常");
        } else {
            loggersysmess("连接异常");
            return false;
        }

        if (!CacheSemiOnline.createfile()) {
            return false;
        }

        XConomy.getInstance().logger("XConomy加载成功", null);
        return true;
    }

    public static void newPlayer(Player a) {
        SQLCreateNewAccount.newPlayer(a);
    }

    public static void getBal(UUID u) {
        SQL.select(u);
    }

    public static void getUid(String name) {
        SQL.selectUID(name);
    }

    public static void getBalNonPlayer(String u) {
        SQL.selectNonPlayer(u);
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

    public static void save(String type, PlayerData pd, Boolean isAdd,
                            BigDecimal oldbalance, BigDecimal amount, String command) {
        SQL.save(type, pd, isAdd, oldbalance, amount, command);
    }


    public static void saveall(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (targettype.equalsIgnoreCase("all")) {
                    SQL.saveall(targettype, type, null, amount, isAdd, reason);
                } else if (targettype.equalsIgnoreCase("online")) {
                    List<UUID> ol = new ArrayList<>();
                    for (Player pp : Bukkit.getOnlinePlayers()) {
                        ol.add(pp.getUniqueId());
                    }
                    SQL.saveall(targettype, type, ol, amount, isAdd, reason);
                }
            }
        }.runTaskAsynchronously(XConomy.getInstance());
    }

    public static void saveNonPlayer(String type, String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd) {
        SQL.saveNonPlayer(type, account, amount, newbalance, isAdd);
    }

    private static void setupMySqlTable() {
        if (gettablesuffix() != null & !gettablesuffix().equals("")) {
            SQL.tableName = "xconomy_" + gettablesuffix().replace("%sign%", XConomy.getSign());
            SQL.tableNonPlayerName = "xconomynon_" + gettablesuffix().replace("%sign%", XConomy.getSign());
            SQL.tableRecordName = "xconomyrecord_" + gettablesuffix().replace("%sign%", XConomy.getSign());
        }
    }

    private static void setupSqLiteAddress() {
        if (gethost().equalsIgnoreCase("Default")) {
            return;
        }

        File folder = new File(gethost());
        if (folder.exists()) {
            SQL.database.userdata = new File(folder, "data.db");
        } else {
            XConomy.getInstance().logger("自定义文件夹路径不存在", null);
        }

    }
}
