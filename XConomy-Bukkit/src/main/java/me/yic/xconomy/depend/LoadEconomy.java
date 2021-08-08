package me.yic.xconomy.depend;/*
 *  This file (LoadEconomy.java) is a part of project XConomy
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

import me.yic.xconomy.depend.economy.EnterpriseHook;
import me.yic.xconomy.depend.economy.VaultHook;
import org.bukkit.Bukkit;

public class LoadEconomy{
    public static boolean haseco = false;
    public static boolean vault = false;
    public static boolean enterprise = false;

    public static boolean load() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            vault = true;
            haseco = true;
            VaultHook.load();
        }
        if (Bukkit.getPluginManager().getPlugin("Enterprise") != null) {
            enterprise = true;
            haseco = true;
            EnterpriseHook.load();
        }
        return haseco;
    }

    public static void unload() {
        if (vault) {
            VaultHook.unload();
        }
        if (enterprise) {
            EnterpriseHook.unload();
        }
    }
}
