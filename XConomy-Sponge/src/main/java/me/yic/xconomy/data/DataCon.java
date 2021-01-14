package me.yic.xconomy.data;/*
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

import me.yic.xconomy.XConomy;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCon {

    public static boolean create() {
        if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
            XConomy.getInstance().logger("数据保存方式", " - MySQL");
            setupMySqlTable();

            if (SQL.con()) {
                SQL.getwaittimeout();
                SQL.createTable();
                SQL.updataTable();
                XConomy.getInstance().logger("MySQL连接正常", null);
            } else {
                XConomy.getInstance().logger("MySQL连接异常", null);
                return false;
            }

        } else {
            XConomy.getInstance().logger("数据保存方式", " - SQLite");
            setupSqLiteAddress();

            File dataFolder = new File(XConomy.getInstance().configDir.toFile(), "playerdata");
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                XConomy.getInstance().logger("文件夹创建异常", null);
                return false;
            }
            if (SQL.con()) {
                SQL.createTable();
                SQL.updataTable();
                XConomy.getInstance().logger("SQLite连接正常", null);
            } else {
                XConomy.getInstance().logger("SQLite连接异常", null);
                return false;
            }

        }

        //if (!CacheSemiOnline.createfile()) {
        //    return false;
        //}

        XConomy.getInstance().logger("XConomy加载成功", null);
        return true;
    }

    public static void newPlayer(Player a) {
        SQL.newPlayer(a);
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

    public static void setTopBalHide(UUID u, Integer type) {
        SQL.hidetop(u, type);
    }

    public static String getBalSum() {
        if (SQL.sumBal() == null) {
            return "0.0";
        }
        return SQL.sumBal();
    }

    public static void save(String type, UUID UID, String player, Boolean isAdd,
                            BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        SQL.save(type, UID, player, isAdd, balance, amount, newbalance, command);
    }


    public static void saveall(String targettype, String type, BigDecimal amount, Boolean isAdd, String reason) {
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> {
                        if (targettype.equalsIgnoreCase("all")) {
                            SQL.saveall(targettype, type, null, amount, isAdd, reason);
                        } else if (targettype.equalsIgnoreCase("online")) {
                            List<UUID> ol = new ArrayList<>();
                            for (Player pp : Sponge.getServer().getOnlinePlayers()) {
                                ol.add(pp.getUniqueId());
                            }
                            SQL.saveall(targettype, type, ol, amount, isAdd, reason);
                        }
                }
        );
    }

    public static void saveNonPlayer(String type, String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd) {
        SQL.saveNonPlayer(type, account, amount, newbalance, isAdd);
    }

    @SuppressWarnings("ConstantConditions")
    private static void setupMySqlTable() {
        if (XConomy.config.getNode("MySQL","table-suffix").getString() != null & !XConomy.config.getNode("MySQL","table-suffix").getString().equals("")) {
            SQL.tableName = "xconomy_" + XConomy.config.getNode("MySQL","table-suffix").getString().replace("%sign%", XConomy.getSign());
            SQL.tableNonPlayerName = "xconomynon_" + XConomy.config.getNode("MySQL","table-suffix").getString().replace("%sign%", XConomy.getSign());
            SQL.tableRecordName = "xconomyrecord_" + XConomy.config.getNode("MySQL","table-suffix").getString().replace("%sign%", XConomy.getSign());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void setupSqLiteAddress() {
        if (XConomy.config.getNode("SQLite","path").getString().equalsIgnoreCase("Default")) {
            return;
        }

        File folder = new File(XConomy.config.getNode("SQLite","path").getString());
        if (folder.exists()) {
            SQL.database.userdata = new File(folder, "data.db");
        } else {
            XConomy.getInstance().logger("自定义文件夹路径不存在", null);
        }

    }
}
