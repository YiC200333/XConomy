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

import ninja.leaping.configurate.ConfigurationNode;

public class UpdateConfig {

    public static boolean update(ConfigurationNode config) {
        boolean update = false;
        if (config.getNode("Settings","ranking-size").isVirtual()) {
            config.getNode("Settings","ranking-size").setValue(10);
            update = true;
        }
        if (config.getNode("Settings","lines-per-page").isVirtual()) {
            config.getNode("Settings","lines-per-page").setValue(5);
            update = true;
        }
        if (config.getNode("Settings","payment-tax").isVirtual()) {
            config.getNode("Settings","payment-tax").setValue(0);
            update = true;
        }
        if (config.getNode("Settings","payment-tax").isVirtual()) {
            config.getNode("Settings","payment-tax").setValue(0);
            update = true;
        }
        if (config.getNode("Settings","disable-cache").isVirtual()) {
            config.getNode("Settings","disable-cache").setValue(false);
            update = true;
        }
        return update;
    }
}
