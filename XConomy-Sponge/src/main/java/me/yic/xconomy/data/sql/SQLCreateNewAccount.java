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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLCreateNewAccount extends SQL {

    public static void newPlayer(String uid, String name) {
        Connection connection = database.getConnectionAndCheck();
        createAccount(uid, name, Double.parseDouble(XConomy.Config.INITIAL_BAL), connection);
        database.closeHikariConnection(connection);
    }

    public static void newPlayer(Player player) {
        if (DataCon.containinfieldslist(player.getName())) {
            kickplayer(player, 2);
        }
        Connection connection = database.getConnectionAndCheck();
        switch (XConomy.Config.UUIDMODE) {
            case ONLINE:
            case OFFLINE:
                String ouid = GetUUID.getUUID(player, player.getName()).toString();
                if (ouid.equalsIgnoreCase(player.getUniqueId().toString())) {
                    checkUserOnline(ouid, player.getName(), connection);
                } else {
                    kickplayer(player, 1);
                    database.closeHikariConnection(connection);
                    return;
                }
                break;
            default:
                boolean doubledata = checkUser(player, connection);
                if (!doubledata) {
                    selectUser(player.getUniqueId(), player.getName(), connection);
                }
        }
        database.closeHikariConnection(connection);
    }

    private static void kickplayer(Player player, int x) {
        String reason = "[XConomy] The same data exists in the server without different UUID";
        if (x == 1) {
            reason = "[XConomy] UUID mismatch";
        } else if (x == 2) {
            reason = "[XConomy] Username does not mismatch requirements";
        }
        final String freason = reason;
        if (player.isOnline()) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() ->
                    player.kick(Text.of(freason)));
        }
    }

    private static void checkUserOnline(String uid, String name, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
            statement.setString(1, uid);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String oldname = rs.getString(2);
                if (!name.equals(oldname)) {
                    updateUser(uid, name, connection);
                    syncOnlineUUID(oldname, name, uid);
                    XConomy.getInstance().logger(" 名称已更改!", 0, "<#>" + name);
                }
            } else {
                createAccount(uid, name, Double.parseDouble(XConomy.Config.INITIAL_BAL), connection);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkUser(Player player, Connection connection) {
        boolean doubledata = false;
        try {
            String query;
            if (XConomy.Config.USERNAME_IGNORE_CASE) {
                if (XConomy.DConfig.isMySQL()) {
                    query = "select * from " + tableName + " where player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ? COLLATE NOCASE";
                }
            } else {
                if (XConomy.DConfig.isMySQL()) {
                    query = "select * from " + tableName + " where binary player = ?";
                } else {
                    query = "select * from " + tableName + " where player = ?";
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getName());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String uid = rs.getString(1);
                if (!player.getUniqueId().toString().equals(uid)) {
                    doubledata = true;
                    if (!XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                        kickplayer(player, 0);
                    } else {
                        CacheSemiOnline.CacheSubUUID_checkUser(uid, player);
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


    private static void createAccount(String UID, String user, double amount, Connection co_a) {
        try {
            String query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?)";
            //String query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) "
            //        + "ON DUPLICATE KEY UPDATE UID = ?";

            PreparedStatement statement = co_a.prepareStatement(query);
            statement.setString(1, UID);
            statement.setString(2, user);
            statement.setDouble(3, amount);
            statement.setInt(4, 0);

            if (XConomy.DConfig.isMySQL()) {
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
            String query = "INSERT INTO " + tableNonPlayerName + "(account,balance) values(?,?)";

            PreparedStatement statement = co.prepareStatement(query);
            statement.setString(1, account);
            statement.setDouble(2, bal);

            if (XConomy.DConfig.isMySQL()) {
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


    @SuppressWarnings("ConstantConditions")
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
                if (cacheThisAmt != null && !XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    PlayerData bd = new PlayerData(UUID.fromString(u), user, cacheThisAmt);
                    Cache.insertIntoCache(UID, bd);
                }
            } else {
                user = name;
                createAccount(UID.toString(), user, Double.parseDouble(XConomy.Config.INITIAL_BAL), connection);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!user.equals(name) && !user.equals("#")) {

            updateUser(UID.toString(), name, connection);
            syncOnlineUUID(user, name, UID.toString());
            XConomy.getInstance().logger(" 名称已更改!", 0, "<#>" + name);
        }
    }


    @SuppressWarnings("UnstableApiUsage")
    private static void syncOnlineUUID(String oldname, String newname, String newUUID) {
        Cache.syncOnlineUUIDCache(oldname, newname, UUID.fromString(newUUID));
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF(XConomy.Config.BUNGEECORD_SIGN);
            output.writeUTF(XConomy.syncversion);
            output.writeUTF("syncOnlineUUID");
            output.writeUTF(oldname);
            output.writeUTF(newname);
            output.writeUTF(newUUID);
            SendPluginMessage.SendMessTask("xconomy:acb", output);
        }
    }
}