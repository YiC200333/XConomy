/*
 *  This file (SQL.java) is a part of project XConomy
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
package me.yic.xconomy.data.sql;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.DatabaseConnection;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SQL {

    public static String tableName = "xconomy";
    public static String tableNonPlayerName = "xconomynon";
    public static String tableRecordName = "xconomyrecord";
    public static String tableUUIDName = "xconomyuuid";
    public static String tableLoginName = "xconomylogin";
    public final static DatabaseConnection database = new DatabaseConnection();
    static final String encoding = XConomyLoad.DConfig.ENCODING;

    public static boolean hasnonplayerplugin = false;


    public static boolean con() {
        return database.setGlobalConnection();
    }

    public static void close() {
        database.close();
    }

    public static void getwaittimeout() {
        if (XConomyLoad.DConfig.isMySQL()) {
            try {
                Connection connection = database.getConnectionAndCheck();

                String query = "show variables like 'wait_timeout'";

                PreparedStatement statement = connection.prepareStatement(query);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int waittime = rs.getInt(2);
                    if (waittime > 50) {
                        database.waittimeout = waittime - 30;
                    }

                }

                rs.close();
                statement.close();
                database.closeHikariConnection(connection);

            } catch (SQLException ignored) {
                XConomy.getInstance().logger("Get 'wait_timeout' error", 1, null);
            }
        }

        database.setHikariValidationTimeout();

    }

    public static void createTable() {
        try {
            Connection connection = database.getConnectionAndCheck();
            Statement statement = connection.createStatement();

            if (statement == null) {
                return;
            }

            String query1;
            String query2;
            String query3 = "create table if not exists " + tableRecordName
                    + "(id int(20) not null auto_increment, type varchar(50) not null, uid varchar(50) not null, player varchar(50) not null,"
                    + "balance double(20,2), amount double(20,2) not null, operation varchar(50) not null,"
                    + " command varchar(255) not null, comment varchar(255) not null, datetime datetime not null,"
                    + " primary key (id)) default charset = " + encoding + ";";
            String query4 = "create table if not exists " + tableLoginName
                    + "(UUID varchar(50) not null, last_time datetime not null, " + "primary key (UUID)) default charset = " + encoding + ";";

            String query5;
            if (XConomyLoad.DConfig.isMySQL()) {
                query1 = "create table if not exists " + tableName
                        + "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, hidden int(5) not null, "
                        + "primary key (UID)) default charset = " + encoding + ";";
                query2 = "create table if not exists " + tableNonPlayerName
                        + "(account varchar(50) not null, balance double(20,2) not null, "
                        + "primary key (account)) default charset = " + encoding + ";";
                query5 = "create table if not exists " + tableUUIDName
                        + "(UUID varchar(50) not null, DUUID varchar(50) not null, " +
                        "primary key (UUID)) default charset = " + encoding + ";";
            } else {
                query1 = "create table if not exists " + tableName
                        + "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, hidden int(5) not null, "
                        + "primary key (UID));";
                query2 = "create table if not exists " + tableNonPlayerName
                        + "(account varchar(50) not null, balance double(20,2) not null, "
                        + "primary key (account));";
                query5 = "create table if not exists " + tableUUIDName
                        + "(UUID varchar(50) not null, DUUID varchar(50) not null, " +
                        "primary key (UUID));";
            }

            statement.executeUpdate(query1);
            if (hasnonplayerplugin || XConomyLoad.Config.NON_PLAYER_ACCOUNT) {
                statement.executeUpdate(query2);
            }
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.executeUpdate(query5);
            }
            if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.TRANSACTION_RECORD) {
                statement.executeUpdate(query3);
                if (XConomyLoad.Config.PAY_TIPS) {
                    statement.executeUpdate(query4);
                }
            }
            statement.close();
            database.closeHikariConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayerData(UUID uuid) {
        PlayerData bd = null;
        try {
            Connection connection = database.getConnectionAndCheck();
            String sql = "select * from " + tableName + " where UID = ?";
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                sql = "select * from " + tableName + " where UID = ifnull((select DUUID from " + tableUUIDName + " where UUID = ?), ?)";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.setString(2, uuid.toString());
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UUID fuuid = uuid;
                if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    fuuid = UUID.fromString(rs.getString(1));
                    Cache.insertIntoMultiUUIDCache(fuuid, uuid);
                }
                BigDecimal cacheThisAmt = DataFormat.formatString(rs.getString(3));
                bd = new PlayerData(fuuid, rs.getString(2), cacheThisAmt);
                Cache.insertIntoCache(fuuid, bd);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }

    @SuppressWarnings("ConstantConditions")
    public static PlayerData getPlayerData(String name) {
        PlayerData bd = null;
        try {
            Connection connection = database.getConnectionAndCheck();
            String query;

            if (XConomyLoad.Config.USERNAME_IGNORE_CASE) {
                if (XConomyLoad.DConfig.isMySQL()) {
                    query = "select * from " + tableName + " where player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ? COLLATE NOCASE";
                }
            } else {
                if (XConomyLoad.DConfig.isMySQL()) {
                    query = "select * from " + tableName + " where binary player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ?";
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(1));
                UUID puuid = null;
                if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.OFFLINE) || XConomyLoad.Config.UUIDMODE.equals(UUIDMode.ONLINE)) {
                    puuid = GetUUID.getUUID(null, name);
                }
                if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.DEFAULT) || XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)
                        || (puuid != null && uuid.toString().equalsIgnoreCase(puuid.toString()))) {
                    String username = rs.getString(2);
                    BigDecimal cacheThisAmt = DataFormat.formatString(rs.getString(3));
                    if (cacheThisAmt != null) {
                        bd = new PlayerData(uuid, username, cacheThisAmt);
                        Cache.insertIntoCache(uuid, bd);
                    }
                    break;
                }
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bd;
    }


    public static BigDecimal getNonPlayerData(String playerName) {
        BigDecimal bal = null;
        try {
            Connection connection = database.getConnectionAndCheck();
            String query;

            if (XConomyLoad.DConfig.isMySQL()) {
                query = "select * from " + tableNonPlayerName + " where binary account = ?";
            } else {
                query = "select * from " + tableNonPlayerName + " where account = ?";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bal = DataFormat.formatString(rs.getString(2));
                CacheNonPlayer.insertIntoCache(playerName, bal);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
            return bal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set balance = ? where UID = ?";
//            if (XConomyLoad.Config.DISABLE_CACHE) {
            if (isAdd != null) {
                if (isAdd) {
                    query = " set balance = balance + ? where UID = ?";
                } else {
                    query = " set balance = balance - ? where UID = ?";
                }
            }
//            }
            PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
//            if (!XConomyLoad.Config.DISABLE_CACHE) {
//                statement.setDouble(1, pd.getBalance().doubleValue());
//            } else {
            statement.setDouble(1, amount.doubleValue());
//            }
            statement.setString(2, pd.getUniqueId().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        record(connection, pd, isAdd, amount, pd.getBalance(), ri);
        database.closeHikariConnection(connection);
    }

    //public static void save(String type, PlayerData pd, Boolean isAdd,
    //                        BigDecimal oldbalance, BigDecimal amount, String command) {
    //    Connection connection = database.getConnectionAndCheck();
    //    UUID u = pd.getUniqueId();
    //    try {
    //        String query;
    //        query = " set balance = " + pd.getbalance().doubleValue() + " where UID = ?";
    //        boolean requirefresh = false;
    //        if (isAdd != null) {
    //            requirefresh = true;
    //            query = query + "AND balance = " + oldbalance.toString();
    //        }
    //        PreparedStatement statement1 = connection.prepareStatement("update " + tableName + query);
    //        statement1.setString(1, u.toString());
    //        int rs = statement1.executeUpdate();
    //        statement1.close();
    //        if (requirefresh && rs == 0) {
    //            command += "(Cache Correction)";
    //        }
    //        record(connection, type, pd, isAdd, amount, pd.getbalance(), command);
    //        if (requirefresh && rs == 0) {
    //            DataCon.refreshFromCache(u);
    //            PlayerData npd = DataCon.cachecorrection(u, amount, isAdd);
    //            if (isAdd) {
    //               query = " set balance = balance + " + amount.doubleValue() + " where UID = ?";
    //            } else {
    //                query = " set balance = balance - " + amount.doubleValue() + " where UID = ?";
    //            }
    //            PreparedStatement statement2 = connection.prepareStatement("update " + tableName + query);
    //            statement2.setString(1, u.toString());
    //            statement2.executeUpdate();
    //            statement2.close();
    //            record(connection, type, npd, isAdd, amount, npd.getbalance(), "Cache Correction Detail");
    //        }
    //    } catch (SQLException e) {
    //        e.printStackTrace();
    //    }
    //    database.closeHikariConnection(connection);
    //}

    public static void saveall(String targettype, List<UUID> players, BigDecimal amount, Boolean isAdd,
                               RecordInfo ri) {
        Connection connection = database.getConnectionAndCheck();
        try {
            if (targettype.equalsIgnoreCase("all")) {
                String query;
                if (isAdd) {
                    query = " set balance = balance + " + amount.doubleValue();
                } else {
                    query = " set balance = balance - " + amount.doubleValue();
                }
                PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
                statement.executeUpdate();
                statement.close();
            } else if (targettype.equalsIgnoreCase("online")) {
                StringBuilder query;
                if (isAdd) {
                    query = new StringBuilder(" set balance = balance + " + amount + " where");
                } else {
                    query = new StringBuilder(" set balance = balance - " + amount + " where");
                }
                int jsm = players.size();
                int js = 1;

                for (UUID u : players) {
                    if (js == jsm) {
                        query.append(" UID = '").append(u.toString()).append("'");
                    } else {
                        query.append(" UID = '").append(u.toString()).append("' OR");
                        js = js + 1;
                    }
                }
                PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ri != null) {
            record(connection, null, isAdd, amount, BigDecimal.ZERO, ri);
        }
        database.closeHikariConnection(connection);
    }

    public static void saveNonPlayer(String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd, RecordInfo ri) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set balance = ? where account = ?";
            if (XConomyLoad.Config.DISABLE_CACHE) {
                if (isAdd != null) {
                    if (isAdd) {
                        query = " set balance = balance + ? where account = ?";
                    } else {
                        query = " set balance = balance - ? where account = ?";
                    }
                }
            }
            PreparedStatement statement = connection.prepareStatement("update " + tableNonPlayerName + query);
            if (!XConomyLoad.Config.DISABLE_CACHE) {
                statement.setDouble(1, newbalance.doubleValue());
            } else {
                statement.setDouble(1, amount.doubleValue());
            }
            statement.setString(2, account);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        record(connection, new PlayerData(null, account, null), isAdd, amount, newbalance, ri);
        database.closeHikariConnection(connection);
    }


    public static void deletePlayerData(String UUID) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = "delete from " + tableName + " where UID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUID);
            statement.executeUpdate();
            statement.close();
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                query = "delete from " + tableName + " where DUUID = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, UUID);
                statement.executeUpdate();
                statement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeHikariConnection(connection);
    }

    public static void getBaltop() {
        try {
            Connection connection = database.getConnectionAndCheck();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from " + tableName + " where hidden != '1' order by balance desc limit " + XConomyLoad.Config.RANKING_SIZE);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Cache.baltop.put(rs.getString(2), DataFormat.formatString(rs.getString(3)));
                Cache.baltop_papi.add(rs.getString(2));
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String sumBal() {
        String bal = "0.0";

        try {
            Connection connection = database.getConnectionAndCheck();
            PreparedStatement statement = connection.prepareStatement("select SUM(balance) from " + tableName + " where hidden != '1'");

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bal = rs.getString(1);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bal;
    }

    public static void hidetop(UUID u, int type) {
        Connection connection = database.getConnectionAndCheck();
        try {
            String query = " set hidden = ? where UID = ?";
            PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
            statement.setInt(1, type);
            statement.setString(2, u.toString());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeHikariConnection(connection);
    }

    public static void record(Connection co, PlayerData pd, Boolean isAdd,
                              BigDecimal amount, BigDecimal newbalance, RecordInfo ri) {
        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.TRANSACTION_RECORD) {
            String uid = "N/A";
            String name = "N/A";
            String operation;
            if (pd != null) {
                if (pd.getUniqueId() != null) {
                    uid = pd.getUniqueId().toString();
                }
                name = pd.getName();
            }
            if (isAdd != null) {
                if (isAdd) {
                    operation = "DEPOSIT";
                } else {
                    operation = "WITHDRAW";
                }
            } else {
                operation = "SET";
            }
            try {
                String query;
                Date dd = new Date();
                String sd = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dd);
                query = "INSERT INTO " + tableRecordName + "(type,uid,player,balance,amount,operation,command,comment,datetime) values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = co.prepareStatement(query);
                statement.setString(1, ri.getType());
                statement.setString(2, uid);
                statement.setString(3, name);
                statement.setDouble(4, newbalance.doubleValue());
                statement.setDouble(5, amount.doubleValue());
                statement.setString(6, operation);
                statement.setString(7, ri.getCommand());
                statement.setString(8, ri.getComment());
                statement.setString(9, sd);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}