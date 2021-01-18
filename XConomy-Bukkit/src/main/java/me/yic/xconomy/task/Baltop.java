/*
 *  This file (Baltop.java) is a part of project XConomy
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
package me.yic.xconomy.task;

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.SQL;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Baltop extends BukkitRunnable {

    @Override
    public void run() {
        Cache.baltop_papi.clear();
        Cache.baltop.clear();
        SQL.getBaltop();
        Cache.sumbal();
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Cache.clearCache();
        }
    }
}
