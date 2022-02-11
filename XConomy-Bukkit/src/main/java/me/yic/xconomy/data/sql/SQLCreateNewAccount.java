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
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.info.ServerINFO;
import me.yic.xconomy.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class SQLCreateNewAccount extends SQL{

    public static void newPlayer(Player player) {
        Connection connection = database.getConnectionAndCheck();

        if (ServerINFO.IsOnlineMode) {
            String uid = GetUUID.getUUID(player, player.getName()).toString();
            if (uid.equalsIgnoreCase(player.getUniqueId().toString())) {
                checkUserOnline(uid, player.getName(), connection);
            }else{
                kickplayer(player, 1);
                database.closeHikariConnection(connection);
                return;
            }

        }else{
            boolean doubledata = checkUser(player, connection);
            if (!doubledata) {
                selectUser(player.getUniqueId(), player.getName(), connection);
            }
        }
        database.closeHikariConnection(connection);
    }

    private static void kickplayer(Player player, int x) {
        String reason = "[XConomy] The same data exists in the server without different UUID";
        if (x == 1){
            reason = "[XConomy] UUID mismatch";
        }
        final String freason = reason;
        if (player.isOnline()) {
            Bukkit.getScheduler().runTask(XConomy.getInstance(), () ->
                    player.kickPlayer(freason));
        }
    }

    private static void createAccount(String UID, String user, double amount, Connection co_a) {
        try {
            String query;
            if (DataBaseINFO.isMySQL()) {
                query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) "
                        + "ON DUPLICATE KEY UPDATE UID = ?";
            } else {
                query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) ";
            }

            PreparedStatement statement = co_a.prepareStatement(query);
            statement.setString(1, UID);
            statement.setString(2, user);
            statement.setDouble(3, amount);
            statement.setInt(4, 0);

            if (DataBaseINFO.isMySQL()) {
                statement.setString(5, UID);
            }

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static boolean checkUser(Player player, Connection connection) {
        boolean doubledata = false;
        try {
            String query;
            if (ServerINFO.IgnoreCase) {
                if (DataBaseINFO.isMySQL()) {
                    query = "select * from " + tableName + " where player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ? COLLATE NOCASE";
                }
            }else {
                if (DataBaseINFO.isMySQL()) {
                    query = "select * from " + tableName + " where binary player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ?";
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getName());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (!player.getUniqueId().toString().equals(rs.getString(1))) {
                    doubledata = true;
                    if (!ServerINFO.IsSemiOnlineMode) {
                        kickplayer(player, 0);
                    } else {
                        CacheSemiOnline.CacheSubUUID_checkUser(rs.getString(1), player);
                    }
                }
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doubledata;
    }

    private static void checkUserOnline(String uid, String name, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
            statement.setString(1, uid);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (!name.equals(rs.getString(2))) {
                    Cache.removefromCache(UUID.fromString(uid));
                    DataCon.prepareudpmessage(null, UUID.fromString(uid), null, null, null, null);
                    updateUser(uid, name, connection);
                    XConomy.getInstance().logger(" 名称已更改!", "<#>" + name);
                }
            } else {
                createAccount(uid, name, ServerINFO.InitialAmount, connection);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createNonPlayerAccount(String account, double bal, Connection co) {
        try {
            String query;
            if (DataBaseINFO.isMySQL()) {
                query = "INSERT INTO " + tableNonPlayerName + "(account,balance) values(?,?) "
                        + "ON DUPLICATE KEY UPDATE account = ?";
            } else {
                query = "INSERT INTO " + tableNonPlayerName + "(account,balance) values(?,?)";
            }

            PreparedStatement statement = co.prepareStatement(query);
            statement.setString(1, account);
            statement.setDouble(2, bal);

            if (DataBaseINFO.isMySQL()) {
                statement.setString(3, account);
            }

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateUser(String UID, String user, Connection co_a) {
        try {
            PreparedStatement statement = co_a.prepareStatement("update " + tableName + " set player = ? where UID = ?");
            statement.setString(1, user);
            statement.setString(2, UID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectUser(UUID UID, String name, Connection connection) {
        String user = "#";

        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
            statement.setString(1, UID.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String u = rs.getString(1);
                user = rs.getString(2);
                BigDecimal cacheThisAmt = DataFormat.formatString(rs.getString(3));
                if (cacheThisAmt != null) {
                    PlayerData bd = new PlayerData(UUID.fromString(u), user, cacheThisAmt);
                    Cache.insertIntoCache(UID, bd);
                }
            } else {
                user = name;
                createAccount(UID.toString(), user, ServerINFO.InitialAmount, connection);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!user.equals(name) && !user.equals("#")) {
            Cache.removefromCache(UID);
            DataCon.prepareudpmessage(null, UID, null, null, null, null);
            updateUser(UID.toString(), name, connection);
            XConomy.getInstance().logger(" 名称已更改!", "<#>" + name);
        }
    }

}