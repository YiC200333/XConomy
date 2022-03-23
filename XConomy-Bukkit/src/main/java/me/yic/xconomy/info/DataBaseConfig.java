/*
 *  This file (DataBaseConfig.java) is a part of project XConomy
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
package me.yic.xconomy.info;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.lang.MessagesManager;
import org.bukkit.configuration.file.FileConfiguration;

public class DataBaseConfig {

    public static FileConfiguration config;

    public DataBaseConfig() {
        if (getStorageType() != 1) {
            canasync = !config.getBoolean("Settings.disable-async");
        }

        setHikariConnectionPooling();
    }

    public boolean EnableConnectionPool = false;
    public boolean DDrivers = false;
    public boolean canasync = false;


    public final String ENCODING = config.getString("MySQL.property.encoding");


    @SuppressWarnings("ConstantConditions")
    public int getStorageType() {
        if (config.getString("Settings.storage-type").equalsIgnoreCase("MySQL")) {
            return 2;
        }
        return 1;
    }

    public void setHikariConnectionPooling() {
        if (config.getBoolean("Settings.usepool")) {
            try {
                Class.forName("org.slf4j.Logger");
            } catch (ClassNotFoundException e) {
                XConomy.getInstance().logger("未找到 'org.slf4j.Logger'", 1, null);
                EnableConnectionPool = false;
            }
            if (getStorageType() == 0 || getStorageType() == 1) {
                EnableConnectionPool = false;
            }
            EnableConnectionPool = !XConomy.foundvaultpe;
            if (!EnableConnectionPool){
                XConomy.getInstance().logger("连接池未启用", 0, null);
            }
        }
    }

    public boolean isMySQL() {
        return getStorageType() == 2;
    }

    public String gethost() {
        if (getStorageType() == 1) {
            return config.getString("SQLite.path");
        } else if (getStorageType() == 2) {
            return config.getString("MySQL.host");
        }
        return "";
    }

    public String getuser() {
        if (getStorageType() == 2) {
            return config.getString("MySQL.user");
        }
        return "";
    }

    public String getpass() {
        if (getStorageType() == 2) {
            return config.getString("MySQL.pass");
        }
        return "";
    }

    public String gettablesuffix() {
        if (getStorageType() == 2) {
            return config.getString("MySQL.table-suffix");
        }
        return "";
    }


    @SuppressWarnings("ConstantConditions")
    public String geturl() {
        if (getStorageType() == 2) {
            String url = "jdbc:mysql://" + config.getString("MySQL.host")
                    + ":" + config.getString("MySQL.port") + "/"
                    + config.getString("MySQL.database") + "?characterEncoding="
                    + ENCODING + "&useSSL="
                    + config.getString("MySQL.property.usessl");
            if (config.getString("MySQL.property.timezone") != null &&
                    !config.getString("MySQL.property.timezone").equals("")) {
                url = url + "&serverTimezone=" + config.getString("MySQL.property.timezone");
            }
            if (config.getBoolean("MySQL.property.allowPublicKeyRetrieval")) {
                url = url + "&allowPublicKeyRetrieval=true";
            }
            return url;
        }
        return "";
    }

    public void loggersysmess(String tag) {
        String mess = MessagesManager.systemMessage(tag);
        switch (getStorageType()) {
            case 1:
                XConomy.getInstance().logger(null, 0, mess.replace("%type%", "SQLite"));
                break;
            case 2:
                XConomy.getInstance().logger(null, 0, mess.replace("%type%", "MySQL"));
                break;
        }
    }

}