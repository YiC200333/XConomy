package me.Yi.XConomy.Data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.Bukkit;

import com.zaxxer.hikari.HikariDataSource;

import me.Yi.XConomy.XConomy;

public class DataBaseCon {
    private static final String Drivera = "com.mysql.jdbc.Driver";
    //============================================================================================
    private static final File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
    private static final String Url = "jdbc:mysql://" + XConomy.config.getString("MySQL.host") + "/"
            + XConomy.config.getString("MySQL.database") + "?characterEncoding=utf-8&useSSL=false";
    private static final String User = XConomy.config.getString("MySQL.user");
    private static final String Pass = XConomy.config.getString("MySQL.pass");
    private static final Integer maxpsize = XConomy.config.getInt("Pool-Settings.maximum-pool-size");
    private static final Integer minidle = XConomy.config.getInt("Pool-Settings.minimum-idle");
    private static final Integer maxlife = XConomy.config.getInt("Pool-Settings.maximum-lifetime");
    private static final Long idletime = XConomy.config.getLong("Pool-Settings.idle-timeout");
    private static boolean secon = false;
    //============================================================================================
    private static final String Driverb = "org.sqlite.JDBC";
    private static final File userdata = new File(dataFolder, "data.db");
    //============================================================================================
    private Connection conn = null;
    private HikariDataSource hikari = null;

    private void createNewHikariConfiguration() throws SQLException {
        hikari = new HikariDataSource();
        hikari.setPoolName("XConomy");
        hikari.setJdbcUrl(Url);
        hikari.setUsername(User);
        hikari.setPassword(Pass);
        hikari.setMaximumPoolSize(maxpsize);
        hikari.setMinimumIdle(minidle);
        hikari.setMaxLifetime(maxlife);
        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
            hikari.setIdleTimeout(idletime);
        } else if (hikari.getMinimumIdle() == hikari.getMaximumPoolSize()) {
            hikari.setIdleTimeout(0);
        }
    }

    public boolean setGlobalConnection() {
        try {
            if (XConomy.allowHikariConnectionPooling()) {
                createNewHikariConfiguration();
                Connection co = getConnection();
                closeHikariConnection(co);
            } else {
                if (XConomy.config.getBoolean("Settings.mysql")) {
                    Class.forName(Drivera);
                    conn = DriverManager.getConnection(Url, User, Pass);
                } else {
                    Class.forName(Driverb);
                    conn = DriverManager.getConnection("jdbc:sqlite:" + userdata.toString());
                }
            }
            if (XConomy.config.getBoolean("Settings.mysql")) {
                if (secon) {
                    XConomy.getInstance().logger("MySQL重新连接成功");
                } else {
                    secon = true;
                }
            }
            return true;
        } catch (SQLException e) {
            XConomy.getInstance().logger("无法连接到数据库-----");
            XConomy.getInstance().logger(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            XConomy.getInstance().logger("JDBC驱动加载失败");
        }
        return false;
    }

    public Connection getConnection() {
        if (isAbleToConnect()) {
            boolean d = XConomy.allowHikariConnectionPooling();
            try {
                if (d) {
                    return hikari.getConnection();
                } else {
                    return conn;
                }
            } catch (SQLException e1) {
                if (d) {
                    hikari.close();
                    if (isAbleToConnect()) {
                        try {
                            return hikari.getConnection();
                        } catch (SQLException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                    }
                }
                e1.printStackTrace();
            }
        }
        return null;
    }

    public boolean isAbleToConnect() {
        try {
            if (XConomy.allowHikariConnectionPooling()) {
                if (hikari == null) {
                    return setGlobalConnection();
                }
                if (hikari.isClosed() == true) {
                    return setGlobalConnection();
                }
            } else {
                if (conn == null) {
                    return setGlobalConnection();
                }
                if (conn.isClosed() == true) {
                    return setGlobalConnection();
                }
            }
        } catch (SQLException e) {
            Arrays.stream(e.getStackTrace()).forEach(d -> Bukkit.getLogger().info(d.toString()));
            return false;
        }
        return true;
    }

    public void closeHikariConnection(Connection co) {
        if (XConomy.allowHikariConnectionPooling()) {
            try {
                hikari.evictConnection(co);
                co.close();
            } catch (SQLException e) {
                // // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (hikari != null) {
                hikari.close();
            }
        } catch (SQLException e) {
            if (XConomy.config.getBoolean("Settings.mysql")) {
                XConomy.getInstance().logger("MySQL连接断开失败");
            }
            e.printStackTrace();
        }
    }
}
