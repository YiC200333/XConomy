/*
 *  This file (XConomyBungee.java) is a part of project XConomy
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
package me.yic.xconomy;

import me.yic.xconomy.listeners.BCPlayerEvent;
import me.yic.xconomy.listeners.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class XConomyBungee extends Plugin {
    private static XConomyBungee instance;

    @Override
    public void onEnable() {

        instance = this;

        getProxy().registerChannel("xconomy:aca");
        getProxy().registerChannel("xconomy:acb");
        getProxy().registerChannel("xconomy:global");
        getProxy().getPluginManager().registerListener(this, new BCsync());
        getProxy().getPluginManager().registerListener(this, new BCPlayerEvent());

        getLogger().info("XConomy successfully enabled!");
        getLogger().info("===== YiC =====");

    }

    @Override
    public void onDisable() {
        getLogger().info("XConomy successfully disabled!");
    }

    public static XConomyBungee getInstance() {
        return instance;
    }
}
