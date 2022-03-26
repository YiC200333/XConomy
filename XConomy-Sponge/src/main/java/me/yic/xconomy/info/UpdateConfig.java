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
import ninja.leaping.configurate.ConfigurationNode;

public class UpdateConfig {

    public static boolean update(ConfigurationNode config) {
        boolean update = false;
        if (config.getNode("UUID-mode").isVirtual() || config.getNode("non-player-account").isVirtual()) {
            XConomy.getInstance().logger(null, 1, "==================================================");
            XConomy.getInstance().logger(null, 1, "The configuration file is an older version");
            XConomy.getInstance().logger(null, 1, "The plugin may occur configuration problems");
            XConomy.getInstance().logger(null, 1, "It is recommended to regenerate configuration file");
            XConomy.getInstance().logger(null, 1, "==================================================");
        }
        //if (config.getNode("Settings","ranking-size").isVirtual()) {
        //    config.getNode("Settings","ranking-size").setValue(10);
        //    update = true;
        //}
        return update;
    }
}
