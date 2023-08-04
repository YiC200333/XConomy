package me.yic.xconomy.depend.economy;/*
 *  This file (EnterpriseHook.java) is a part of project XConomy
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

import com.github.sanctum.economy.construct.implement.AdvancedEconomy;
import me.yic.xconomy.XConomy;
import org.bukkit.plugin.ServicePriority;

public class EnterpriseHook extends XConomy {

    private static AdvancedEconomy provider;

    public static void load() {
        provider = new Enterprise();
        getInstance().getServer().getServicesManager().register(AdvancedEconomy.class, provider, getInstance(), ServicePriority.Normal);
    }

    public static void unload() {
        getInstance().getServer().getServicesManager().unregister(AdvancedEconomy.class, provider);
    }
}
