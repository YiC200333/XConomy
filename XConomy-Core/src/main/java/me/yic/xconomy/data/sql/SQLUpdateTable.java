/*
 *  This file (SQLUpdateTable.java) is a part of project XConomy
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

import java.sql.*;

public class SQLUpdateTable extends SQL {

    public static void updataTable() {
        Connection connection = database.getConnectionAndCheck();
        try {

            PreparedStatement statementa = connection.prepareStatement("select * from " + tableName + " where hidden = '1'");

            statementa.executeQuery();
            statementa.close();
            database.closeHikariConnection(connection);

        } catch (SQLException e) {
            try {
                XConomy.getInstance().logger("升级数据库表格。。。", 0, tableName);

                PreparedStatement statementb = connection.prepareStatement("alter table " + tableName + " add column hidden int(5) not null default '0'");

                statementb.executeUpdate();
                statementb.close();
                database.closeHikariConnection(connection);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void updataTable_record() {
        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.TRANSACTION_RECORD) {
            Connection connection = database.getConnectionAndCheck();
            try {
                //PreparedStatement statementa = connection.prepareStatement("desc " + tableRecordName + " datetime");
                //ResultSet rs = statementa.executeQuery();
                //if (!rs.next()) {
                //    XConomy.getInstance().logger("升级数据库表格。。。", 0, tableRecordName);
                //    Timestamp dd = new Timestamp((new Date()).getTime());
                //    PreparedStatement statementb = connection.prepareStatement("alter table " + tableRecordName + " add column datetime datetime not null default ?");
                //    statementb.setTimestamp(1, dd);
                //    statementb.executeUpdate();
                //    statementb.close();
                //}

                PreparedStatement statementa = connection.prepareStatement("desc " + tableRecordName + " date");
                ResultSet rs = statementa.executeQuery();
                if (rs.next()) {
                    XConomy.getInstance().logger("升级数据库表格。。。", 0, tableRecordName);
                    PreparedStatement statementb = connection.prepareStatement("alter table " + tableRecordName + " rename to " + tableRecordName + "_old");
                    statementb.executeUpdate();
                    statementb.close();
                    createTable();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            database.closeHikariConnection(connection);
        }
    }
}