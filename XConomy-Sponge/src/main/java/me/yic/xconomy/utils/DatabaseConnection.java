package me.yic.xconomy.utils;/*
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

import com.zaxxer.hikari.HikariDataSource;
import me.yic.xconomy.XConomy;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
    private String driver = "com.mysql.jdbc.Driver";
    //============================================================================================
    private final File dataFolder = new File(XConomy.getInstance().configDir.toFile(), "playerdata");
    private String url = "jdbc:mysql://" + XConomy.config.getNode("MySQL","host").getString() + "/"
            + XConomy.config.getNode("MySQL","database").getString() + "?characterEncoding="
            + XConomy.config.getNode("MySQL","encoding").getString() + "&useSSL=false";
    private final String username = XConomy.config.getNode("MySQL","user").getString();
    private final String password = XConomy.config.getNode("MySQL","pass").getString();
    private final Integer maxPoolSize = XConomy.config.getNode("Pool-Settings","maximum-pool-size").getInt();
    private final Integer minIdle = XConomy.config.getNode("Pool-Settings","minimum-idle").getInt();
    private final Integer maxLife = XConomy.config.getNode("Pool-Settings","maximum-lifetime").getInt();
    private final Long idleTime = XConomy.config.getNode("Pool-Settings","idle-timeout").getLong();
    private boolean secon = false;
    public Integer waittimeout = 10;
    //============================================================================================
    public File userdata = new File(dataFolder, "data.db");
    //============================================================================================
    private Connection connection = null;
    private HikariDataSource hikari = null;
    private boolean isfirstry = true;

    private void createNewHikariConfiguration() {
        hikari = new HikariDataSource();
        hikari.setPoolName("XConomy");
        hikari.setJdbcUrl(url);
        hikari.setUsername(username);
        hikari.setPassword(password);
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
            if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                driver = ("me.yic.libs.mysql.cj.jdbc.Driver");
            } else {
                driver = ("me.yic.libs.sqlite.JDBC");
            }
        } else {
            if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                driver = ("com.mysql.jdbc.Driver");
            } else {
                driver = ("org.sqlite.JDBC");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setTimezone() {
        if (XConomy.config.getNode("MySQL","timezone").getString() != null &&
                !XConomy.config.getNode("MySQL","timezone").getString().equals("")) {
            url = url + "&serverTimezone=" + XConomy.config.getNode("MySQL","timezone").getString();
        }
    }

    public boolean setGlobalConnection() {
        setTimezone();
        setDriver();
        try {
            if (ServerINFO.EnableConnectionPool) {
                createNewHikariConfiguration();
                Connection connection = getConnection();
                closeHikariConnection(connection);
            } else {
                Class.forName(driver);
                if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                    connection = DriverManager.getConnection(url, username, password);
                } else {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + userdata.toString());
                }
            }

            if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                if (secon) {
                    XConomy.getInstance().logger("MySQL重新连接成功", null);
                } else {
                    secon = true;
                }
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

                if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                    if (!connection.isValid(waittimeout)) {
                        secon = false;
                        return setGlobalConnection();
                    }
                }
            }
        } catch (SQLException e) {
            Arrays.stream(e.getStackTrace()).forEach(d -> XConomy.getInstance().logger(null,d.toString()));
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
            if (XConomy.config.getNode("Settings","mysql").getBoolean()) {
                XConomy.getInstance().logger("MySQL连接断开失败", null);
            }
            e.printStackTrace();
        }
    }
}
