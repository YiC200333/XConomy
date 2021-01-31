/*
 *  This file (Convert.java) is a part of project XConomy
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
package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.sql.SQLCreateNewAccount;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Convert extends XConomy {

    public static File userData;
    public static File uidData;
    public static File baltopData;
    public static File nonPlayerData;
    public static FileConfiguration userFile;
    public static FileConfiguration uidFile;
    public static FileConfiguration baltopFile;
    public static FileConfiguration nonPlayerFile;

    @SuppressWarnings(value = {"ResultOfMethodCallIgnored", "ConstantConditions"})
    public static void convert(File dataFolder) {
        userData = new File(dataFolder, "data.yml");
        uidData = new File(dataFolder, "uid.yml");
        baltopData = new File(dataFolder, "baltop.yml");
        nonPlayerData = new File(dataFolder, "datanon.yml");
        userFile = YamlConfiguration.loadConfiguration(userData);
        uidFile = YamlConfiguration.loadConfiguration(uidData);
        nonPlayerFile = YamlConfiguration.loadConfiguration(nonPlayerData);
        baltopFile = YamlConfiguration.loadConfiguration(baltopData);

        if (userData.exists() | nonPlayerData.exists()) {
            if (userData.exists()) {
                getInstance().logger(null, "Old data file found");
                getInstance().logger(null, "Please wait for data conversion..........");
                ConfigurationSection section1 = userFile.getConfigurationSection("");
                for (String u : section1.getKeys(false)) {
                    String na = userFile.getString(u + ".username");
                    Double dd = Double.valueOf(userFile.getString(u + ".balance"));
                    SQLCreateNewAccount.convertData(u, na, dd);
                }
            }
            if (config.getBoolean("Settings.non-player-account")) {
                if (nonPlayerData.exists()) {
                    getInstance().logger(null, "Old non-player data file found");
                    getInstance().logger(null, "Please wait for data conversion..........");
                    ConfigurationSection section2 = nonPlayerFile.getConfigurationSection("");
                    for (String n : section2.getKeys(false)) {
                        Double dd = Double.valueOf(nonPlayerFile.getString(n + ".balance"));
                        SQLCreateNewAccount.convertNonPlayerData(n, dd);

                    }
                }
            }

            File oldDataFOlder = new File(dataFolder, "old");
            oldDataFOlder.mkdirs();

            File oldUserData = new File(oldDataFOlder, "data.yml");
            File oldUidData = new File(oldDataFOlder, "uid.yml");
            File oldBaltopData = new File(oldDataFOlder, "baltop.yml");
            File oldNonPlayerData = new File(oldDataFOlder, "datanon.yml");

            if (userData.exists() & !oldUserData.exists()) {
                userData.renameTo(oldUserData);
            }
            if (nonPlayerData.exists() & !oldNonPlayerData.exists()) {
                nonPlayerData.renameTo(oldNonPlayerData);
            }
            if (baltopData.exists() & !oldBaltopData.exists()) {
                baltopData.renameTo(oldBaltopData);
            }
            if (uidData.exists() & !oldUidData.exists()) {
                uidData.renameTo(oldUidData);
            }

            getInstance().logger(null, "Data conversion complete");
        }
    }
}
