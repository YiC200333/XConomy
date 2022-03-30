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

import me.yic.xconomy.XConomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class UpdateConfig {

    public static boolean update(FileConfiguration config, File cc) {
        boolean update = false;
        FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
        if (!ck.contains("UUID-mode") || !ck.contains("non-player-account")) {
            XConomy.getInstance().logger(null, 1, "==================================================");
            XConomy.getInstance().logger(null, 1, "The configuration file is an older version");
            XConomy.getInstance().logger(null, 1, "The plugin may occur configuration problems");
            XConomy.getInstance().logger(null, 1, "It is recommended to regenerate configuration file");
            XConomy.getInstance().logger(null, 1, "==================================================");
        }
        if (!ck.contains("Settings.offline-pay-transfer-tips")) {
            config.createSection("Settings.offline-pay-transfer-tips");
            config.set("Settings.offline-pay-transfer-tips", false);
            update = true;
        }
        return update;
    }
}
