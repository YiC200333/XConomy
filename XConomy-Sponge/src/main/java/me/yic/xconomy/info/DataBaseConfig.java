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
import ninja.leaping.configurate.ConfigurationNode;

public class DataBaseConfig {

    public static ConfigurationNode config;

    public DataBaseConfig() {
        if (getStorageType() != 1) {
            canasync = !config.getNode("Settings","disable-async").getBoolean();
        }

        setHikariConnectionPooling();
    }

    public boolean EnableConnectionPool = false;
    public boolean DDrivers = false;
    public boolean canasync = false;


    public final String ENCODING = config.getNode("MySQL","property","encoding").getString();


    @SuppressWarnings("ConstantConditions")
    public int getStorageType() {
        if (config.getNode("Settings", "storage-type").getString().equalsIgnoreCase("MySQL")) {
            return 2;
        }
        return 1;
    }

    public void setHikariConnectionPooling() {
        if (getStorageType() == 0 || getStorageType() == 1) {
            return;
        }
        EnableConnectionPool = config.getNode("Settings", "usepool").getBoolean();
    }

    public boolean isMySQL() {
        return getStorageType() == 2;
    }

    public String gethost() {
        if (getStorageType() == 1) {
            return config.getNode("SQLite", "path").getString();
        } else if (getStorageType() == 2) {
            return config.getNode("MySQL", "host").getString();
        }
        return "";
    }

    public String getuser() {
        if (getStorageType() == 2) {
            return config.getNode("MySQL", "user").getString();
        }
        return "";
    }

    public String getpass() {
        if (getStorageType() == 2) {
            return config.getNode("MySQL", "pass").getString();
        }
        return "";
    }

    public String gettablesuffix() {
        if (getStorageType() == 2) {
            return config.getNode("MySQL", "table-suffix").getString();
        }
        return "";
    }


    @SuppressWarnings("ConstantConditions")
    public String geturl() {
        if (getStorageType() == 2) {
            String url = "jdbc:mysql://" + config.getNode("MySQL", "host").getString()
                    + ":" + config.getNode("MySQL", "port").getString() + "/"
                    + config.getNode("MySQL", "database").getString() + "?characterEncoding="
                    + config.getNode("MySQL", "property", "encoding").getString() + "&useSSL="
                    + config.getNode("MySQL", "property", "usessl").getString();
            if (config.getNode("MySQL", "property", "timezone").getString() != null &&
                    !config.getNode("MySQL", "property", "timezone").getString().equals("")) {
                url = url + "&serverTimezone=" + config.getNode("MySQL", "property", "timezone").getString();
            }
            if (config.getNode("MySQL", "property", "allowPublicKeyRetrieval").getBoolean()) {
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