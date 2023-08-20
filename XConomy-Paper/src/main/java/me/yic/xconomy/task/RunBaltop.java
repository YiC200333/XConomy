/*
 *  This file (RunBaltop.java) is a part of project XConomy
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

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class RunBaltop {
    private static ScheduledTask refresherTask = null;
    public static void runstart() {
        int time = XConomyLoad.Config.REFRESH_TIME;
        refresherTask = Bukkit.getAsyncScheduler().runAtFixedRate((Plugin) XConomy.getInstance(), ScheduledTask -> new Baltop().run(), time * 20L, time * 20L, TimeUnit.MILLISECONDS);
    }

    public static void stop() {
        if (refresherTask != null) {
            refresherTask.cancel();
        }
    }
}
