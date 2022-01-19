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
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.info.ServerINFO;
import me.yic.xconomy.utils.OnlineUUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLCreateNewAccount extends SQL {

    public static void newPlayer(Player player) {
        Connection connection = database.getConnectionAndCheck();
        String uid = DataCon.getOfflineUUID(player.getName()).toString();
        if (ServerINFO.IsOnlineMode) {
            uid = OnlineUUID.doGetUUID(player);
            if (!uid.equalsIgnoreCase(player.getUniqueId().toString())) {
                kickplayer(player);
                database.closeHikariConnection(connection);
                return;
            }
        } else {
            if (!uid.equalsIgnoreCase(player.getUniqueId().toString())) {
                kickplayer(player);
                database.closeHikariConnection(connection);
                return;
            }
        }

        checkUser(uid, player.getName(), connection);
        database.closeHikariConnection(connection);
    }

    private static void kickplayer(Player player) {
        if (player.isOnline()) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() ->
                    player.kick(Text.of("[XConomy] UUID mismatch")));
        }
    }

    private static void checkUser(String uid, String name, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
            statement.setString(1, uid);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (!name.equals(rs.getString(2))) {
                    Cache.removefromCache(UUID.fromString(uid));
                    DataCon.prepareudpmessage(null, UUID.fromString(uid), null, null, null, null);
                    updateUser(uid, name, connection);
                    updateUserS(uid, name, connection);
                    XConomy.getInstance().logger(" 名称已更改!", "<#>" + name);
                }
            } else {
                createAccount(uid, name, ServerINFO.InitialAmount, connection);
                updateUserS(uid, name, connection);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    private static void updateUserS(String UID, String user, Connection co_a) {
        try {
            PreparedStatement statement = co_a.prepareStatement("update " + tableName + " set player = ? where player = ? and UID <> ?");
            statement.setString(1, "*" + user + "********************");
            statement.setString(2, user);
            statement.setString(3, UID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}