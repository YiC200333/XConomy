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
import me.yic.xconomy.comp.CPlayer;
import me.yic.xconomy.comp.CScheduler;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.data.ImportData;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.SyncUUID;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLCreateNewAccount extends SQL {

    public static boolean newPlayer(UUID uid, String name, CPlayer player) {
        if (DataCon.containinfieldslist(name)) {
            kickplayer(player, 2, "");
            return false;
        }
        Connection connection = database.getConnectionAndCheck();
        switch (XConomy.Config.UUIDMODE) {
            case ONLINE:
            case OFFLINE:
                String ouid = GetUUID.getUUID(player, name).toString();
                if (ouid.equalsIgnoreCase(uid.toString())) {
                    checkUserOnline(ouid, name, connection);
                } else {
                    kickplayer(player, 1, ouid);
                    database.closeHikariConnection(connection);
                    return false;
                }
                break;
            default:
                boolean doubledata = checkUser(uid, name, player, connection);
                if (!doubledata) {
                    selectUser(uid, name, connection);
                }
                break;
        }
        database.closeHikariConnection(connection);
        return true;
    }

    public static void newPlayer(CPlayer player) {
        newPlayer(player.getUniqueId(), player.getName(), player);
    }

    private static void kickplayer(CPlayer player, int x, String DUID) {
        if (player != null && player.isOnline()) {
            String reason = "[XConomy] The same data exists in the server without different UUID\nUsername - "
                    + player.getName() + "\nUUID[C] - " + player.getUniqueId() + "\nUUID[D] - " + DUID;
            if (x == 1) {
                reason = "[XConomy] UUID mismatch\nUsername - "
                        + player.getName() + "\nUUID[C] - " + player.getUniqueId() + "\nUUID[O] - " + DUID;
            } else if (x == 2) {
                reason = "[XConomy] Username does not mismatch requirements";
            }
            final String freason = reason;
            if (player.isOnline()) {
                player.kickPlayer(freason);
            }
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
                    syncOnlineUUID(oldname, name, UUID.fromString(uid));
                    XConomy.getInstance().logger(" 名称已更改!", 0, "<#>" + name);
                }
            } else {
                createAccount(uid, name, connection);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkUser(UUID uuid, String name, CPlayer player, Connection connection) {
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
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String uid = rs.getString(1);
                if (!uuid.toString().equals(uid)) {
                    doubledata = true;
                    if (!XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                        kickplayer(player, 0, uid);
                    } else {
                        createDUUIDLink(uuid.toString(), uid, connection);
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


    private static void createAccount(String UID, String user, Connection co_a) {
        try {
            String query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?)";
            //String query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) "
            //        + "ON DUPLICATE KEY UPDATE UID = ?";

            PreparedStatement statement = co_a.prepareStatement(query);
            statement.setString(1, UID);
            statement.setString(2, user);

            statement.setDouble(3, ImportData.getBalance(user, XConomy.Config.INITIAL_BAL).doubleValue());

            statement.setInt(4, 0);

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

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDUUIDLink(String UUID, String DUUID, Connection co_a) {
        try {
            String query;
            if (XConomy.DConfig.isMySQL()) {
                query = "INSERT INTO " + tableUUIDName + "(UUID,DUUID) values(?,?) ON DUPLICATE KEY UPDATE DUUID = ?";
            }else{
                query = "INSERT INTO " + tableUUIDName + "(UUID,DUUID) values(?,?)";
            }
            PreparedStatement statement = co_a.prepareStatement(query);
            statement.setString(1, UUID);
            statement.setString(2, DUUID);
            if (XConomy.DConfig.isMySQL()) {
                statement.setString(3, DUUID);
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
                    PlayerData bd = new PlayerData(XConomy.Config.BUNGEECORD_SIGN, UUID.fromString(u), user, cacheThisAmt);
                    Cache.insertIntoCache(UID, bd);
                }
            } else {
                user = name;
                createAccount(UID.toString(), user, connection);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!user.equals(name) && !user.equals("#")) {

            updateUser(UID.toString(), name, connection);
            syncOnlineUUID(user, name, UID);
            XConomy.getInstance().logger(" 名称已更改!", 0, "<#>" + name);
        }
    }

    //private static void updateUUID(String UID, String user, Connection co_a) {
    //    try {
    //        PreparedStatement statement = co_a.prepareStatement("update " + tableName + " set UID = ? where player = ?");
    //        statement.setString(1, UID);
    //        statement.setString(2, user);
    //        statement.executeUpdate();
    //        statement.close();
    //    } catch (SQLException e) {
    //        e.printStackTrace();
    //    }
    //}

    private static void syncOnlineUUID(String oldname, String newname, UUID newUUID) {
        Cache.syncOnlineUUIDCache(oldname, newname, newUUID);
        if (XConomy.Config.BUNGEECORD_ENABLE) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(output);
                oos.writeUTF(XConomy.syncversion);
                oos.writeObject(new SyncUUID(XConomy.Config.BUNGEECORD_SIGN, newUUID, newname, oldname));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SendPluginMessage.SendMessTask("xconomy:acb", output);
        }
    }
}