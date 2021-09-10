/*
 *  This file (DatabaseConnection.java) is a part of project XConomy
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
package me.yic.xconomy.utils;

import com.zaxxer.hikari.HikariDataSource;
import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
    private String driver = "com.mysql.jdbc.Driver";
    //============================================================================================
    private final File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
    private String url = "";
    private final int maxPoolSize = DataBaseINFO.DataBaseINFO.getInt("Pool-Settings.maximum-pool-size");
    private final int minIdle = DataBaseINFO.DataBaseINFO.getInt("Pool-Settings.minimum-idle");
    private final int maxLife = DataBaseINFO.DataBaseINFO.getInt("Pool-Settings.maximum-lifetime");
    private final Long idleTime = DataBaseINFO.DataBaseINFO.getLong("Pool-Settings.idle-timeout");
    private boolean secon = false;
    //============================================================================================
    public int waittimeout = 10;
    //============================================================================================
    public File userdata = new File(dataFolder, "data.db");
    //============================================================================================
    private Connection connection = null;
    private HikariDataSource hikari = null;
    private boolean isfirstry = true;

    private void createNewHikariConfiguration() {
        hikari = new HikariDataSource();
        hikari.setPoolName("[XConomy]");
        hikari.setJdbcUrl(url);
        hikari.setUsername(DataBaseINFO.getuser());
        hikari.setPassword(DataBaseINFO.getpass());
        hikari.setMaximumPoolSize(maxPoolSize);
        hikari.setMinimumIdle(minIdle);
        hikari.setMaxLifetime(maxLife);
        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikari.addDataSourceProperty("userServerPrepStmts", "true");
        if (ServerINFO.DDrivers) {
            hikari.setDriverClassName(driver);
        }
        if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
            hikari.setIdleTimeout(idleTime);
        } else {
            hikari.setIdleTimeout(0);
        }
    }

    private void setDriver() {
        if (ServerINFO.DDrivers) {
            switch (DataBaseINFO.getStorageType()) {
                case 1:
                    driver = ("me.yic.libs.sqlite.JDBC");
                    break;
                case 2:
                    driver = ("me.yic.libs.mysql.cj.jdbc.Driver");
                    break;
            }
        } else {
            switch (DataBaseINFO.getStorageType()) {
                case 1:
                    driver = ("org.sqlite.JDBC");
                    break;
                case 2:
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException e) {
                        driver = ("com.mysql.jdbc.Driver");
                        break;
                    }
                    driver = ("com.mysql.cj.jdbc.Driver");
                    break;
            }
        }
    }

    public boolean setGlobalConnection() {
        url = DataBaseINFO.geturl();
        setDriver();
        try {
            if (ServerINFO.EnableConnectionPool) {
                createNewHikariConfiguration();
                Connection connection = getConnection();
                closeHikariConnection(connection);
            } else {
                Class.forName(driver);
                switch (DataBaseINFO.getStorageType()) {
                    case 1:
                        connection = DriverManager.getConnection("jdbc:sqlite:" + userdata.toString());
                        break;
                    case 2:
                        connection = DriverManager.getConnection(url, DataBaseINFO.getuser(), DataBaseINFO.getpass());
                        break;
                }
            }

            if (secon) {
                DataBaseINFO.loggersysmess("重新连接成功");
            } else {
                secon = true;
            }
            return true;

        } catch (SQLException e) {
            XConomy.getInstance().logger("无法连接到数据库-----", null);
            e.printStackTrace();
            close();
            return false;

        } catch (ClassNotFoundException e) {
            XConomy.getInstance().logger("JDBC驱动加载失败", null);
        }

        return false;
    }

    public Connection getConnectionAndCheck() {
        if (!canConnect()) {
            return null;
        }
        try {
            return getConnection();
        } catch (SQLException e1) {
            if (isfirstry) {
                isfirstry = false;
                close();
                return getConnectionAndCheck();
            } else {
                isfirstry = true;
                XConomy.getInstance().logger("无法连接到数据库-----", null);
                close();
                e1.printStackTrace();
                return null;
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (ServerINFO.EnableConnectionPool) {
            return hikari.getConnection();
        } else {
            return connection;
        }
    }

    public boolean canConnect() {
        try {
            if (ServerINFO.EnableConnectionPool) {
                if (hikari == null) {
                    return setGlobalConnection();
                }

                if (hikari.isClosed()) {
                    return setGlobalConnection();
                }

            } else {
                if (connection == null) {
                    return setGlobalConnection();
                }

                if (connection.isClosed()) {
                    return setGlobalConnection();
                }

                if (DataBaseINFO.getStorageType() == 2) {
                    if (!connection.isValid(waittimeout)) {
                        secon = false;
                        return setGlobalConnection();
                    }
                }
            }
        } catch (SQLException e) {
            Arrays.stream(e.getStackTrace()).forEach(d -> Bukkit.getLogger().info(d.toString()));
            return false;
        }
        return true;
    }

    public void closeHikariConnection(Connection connection) {
        if (!ServerINFO.EnableConnectionPool) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (hikari != null) {
                hikari.close();
            }
        } catch (SQLException e) {
            DataBaseINFO.loggersysmess("连接断开失败");
            e.printStackTrace();
        }
    }
}
