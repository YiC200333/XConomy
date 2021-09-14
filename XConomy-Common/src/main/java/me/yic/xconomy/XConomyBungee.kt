/*
 *  This file (XConomyBungee.kt) is a part of project XConomy
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
package me.yic.xconomy

import me.yic.xconomy.listeners.BCPlayerEvent
import me.yic.xconomy.listeners.BCsync
import net.md_5.bungee.api.plugin.Plugin

class XConomyBungee : Plugin() {

    companion object {
        private var instance: XConomyBungee? = null
        fun getInstance(): XConomyBungee? {
            return instance
        }
    }

    override fun onEnable() {
        instance = this
        getProxy().registerChannel("xconomy:aca")
        getProxy().registerChannel("xconomy:acb")
        getProxy().registerChannel("xconomy:global")
        getProxy().getPluginManager().registerListener(this, BCsync())
        getProxy().getPluginManager().registerListener(this, BCPlayerEvent())
        getLogger().info("XConomy successfully enabled!")
        getLogger().info("===== YiC =====")
    }

    override fun onDisable() {
        getLogger().info("XConomy successfully disabled!")
    }
}