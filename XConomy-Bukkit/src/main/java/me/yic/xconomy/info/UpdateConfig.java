/*
 *  This file (UpdateConfig.java) is a part of project XConomy
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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class UpdateConfig {

    public static boolean update(FileConfiguration config, File cc) {
        boolean update = false;
        FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
        if (!ck.contains("Settings.ranking-size")) {
            config.createSection("Settings.ranking-size");
            config.set("Settings.ranking-size", 10);
            update = true;
        }
        if (!ck.contains("Settings.lines-per-page")) {
            config.createSection("Settings.lines-per-page");
            config.set("Settings.lines-per-page", 5);
            update = true;
        }
        if (!ck.contains("Settings.payment-tax")) {
            config.createSection("Settings.payment-tax");
            config.set("Settings.payment-tax", 0);
            update = true;
        }
        if (!ck.contains("Settings.disable-cache")) {
            config.createSection("Settings.disable-cache");
            config.set("Settings.disable-cache", false);
            update = true;
        }
        if (!ck.contains("Settings.UUID-mode")) {
            config.createSection("Settings.UUID-mode");
            if (ck.getBoolean("Settings.semi-online-mode")) {
                config.set("Settings.UUID-mode", "SemiOnline");
            }else{
                config.set("Settings.UUID-mode", "Default");
            }
            update = true;
        }
        return update;
    }
}
