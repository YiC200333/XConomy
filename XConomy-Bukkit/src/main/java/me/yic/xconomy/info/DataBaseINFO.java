/*
 *  This file (DataBaseINFO.java) is a part of project XConomy
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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataBaseINFO {

    public static FileConfiguration DataBaseINFO;
    public static boolean canasync = false;

    public static void load() {
        File file = new File(XConomy.getInstance().getDataFolder(), "database.yml");
        if (!file.exists()) {
            XConomy.getInstance().saveResource("database.yml", false);
        }
        DataBaseINFO = YamlConfiguration.loadConfiguration(file);

        if (getStorageType() != 1) {
            canasync = !DataBaseINFO.getBoolean("Settings.disable-async");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static int getStorageType() {
        if (DataBaseINFO.getString("Settings.storage-type").equalsIgnoreCase("MySQL")) {
            return 2;
        }
        return 1;
    }

    public static boolean isMySQL() {
        return getStorageType() == 2;
    }

    public static String gethost() {
        if (getStorageType() == 1) {
            return DataBaseINFO.getString("SQLite.path");
        } else if (getStorageType() == 2) {
            return DataBaseINFO.getString("MySQL.host");
        }
        return "";
    }

    public static String getuser() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getString("MySQL.user");
        }
        return "";
    }

    public static String getpass() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getString("MySQL.pass");
        }
        return "";
    }

    public static String gettablesuffix() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getString("MySQL.table-suffix");
        }
        return "";
    }


    @SuppressWarnings("ConstantConditions")
    public static String geturl() {
        if (getStorageType() == 2) {
            String url = "jdbc:mysql://" + DataBaseINFO.getString("MySQL.host")
                    + ":" + DataBaseINFO.getString("MySQL.port") + "/"
                    + DataBaseINFO.getString("MySQL.database") + "?characterEncoding="
                    + DataBaseINFO.getString("MySQL.property.encoding") + "&useSSL="
                    + DataBaseINFO.getString("MySQL.property.usessl");
            if (DataBaseINFO.getString("MySQL.property.timezone") != null &&
                    !DataBaseINFO.getString("MySQL.property.timezone").equals("")) {
                url = url + "&serverTimezone=" + DataBaseINFO.getString("MySQL.property.timezone");
            }
            if (DataBaseINFO.getBoolean("MySQL.property.allowPublicKeyRetrieval")) {
                url = url + "&allowPublicKeyRetrieval=true";
            }
            return url;
        }
        return "";
    }

    public static void loggersysmess(String tag) {
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