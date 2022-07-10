/*
 *  This file (SQLLogin.java) is a part of project XConomy
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
import me.yic.xconomy.adapter.comp.CChat;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.utils.UUIDMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SQLLogin extends SQL {

    public static void updatelogininfo(UUID uuid) {
        Connection connection = database.getConnectionAndCheck();
        Date dd = new Date();
        String sd = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dd);
        try {
            String sql = "INSERT INTO " + tableLoginName + " (UUID,last_time) values(?,?) ON DUPLICATE KEY UPDATE last_time = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.setString(1, DataCon.getPlayerData(uuid).getUniqueId().toString());
            }else{
                statement.setString(1, uuid.toString());
            }
            statement.setString(2, sd);
            statement.setString(3, sd);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.closeHikariConnection(connection);
    }


    public static void getPlayerlogin(CPlayer pp) {
        try {
            Connection connection = database.getConnectionAndCheck();
            PreparedStatement statement = connection.prepareStatement("select player, amount from " + tableRecordName +
                    " where operation = 'WITHDRAW' and type = 'PLAYER_COMMAND' and command like('pay " + pp.getName() + "%') and datetime > " +
                    "(select last_time from " + tableLoginName + " where UUID = ?);");
            if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                statement.setString(1, DataCon.getPlayerData(pp.getUniqueId()).getUniqueId().toString());
            }else{
                statement.setString(1, pp.getUniqueId().toString());
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String otherp = rs.getString(1);
                double amount = rs.getDouble(2);
                sendMessages(pp, otherp, amount);
            }

            rs.close();
            statement.close();
            database.closeHikariConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessages(CPlayer sender, String name, double amount) {
        String PREFIX = CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString("prefix"));
        String message = CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(MessageConfig.PAY_RECEIVE.toString()));
        message = PREFIX + message.replace("%player%", name).replace("%amount%", DataFormat.shown(amount));
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

}