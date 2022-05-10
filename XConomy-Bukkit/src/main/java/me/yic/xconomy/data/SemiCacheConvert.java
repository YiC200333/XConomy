/*
 *  This file (CacheSemiOnline.java) is a part of project XConomy
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
package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.data.sql.SQLCreateNewAccount;
import me.yic.xconomy.utils.UUIDMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;

@SuppressWarnings(value = {"ResultOfMethodCallIgnored", "ConstantConditions"})
public class SemiCacheConvert {

    public static void start() {
        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
            File dataFolder = new File(XConomy.getInstance().getDataFolder(), "cache");
            if (!dataFolder.exists()) {
                return;
            }
            File cachesubuuid = new File(dataFolder, "cache_subuuid.yml");
            if (!cachesubuuid.exists()) {
                return;
            }

            File newcachesubuuid = new File(dataFolder, "cache_subuuid_f.yml");
            FileConfiguration CacheSubUUID = YamlConfiguration.loadConfiguration(cachesubuuid);
            Connection connection = SQL.database.getConnectionAndCheck();
            ConfigurationSection section = CacheSubUUID.getConfigurationSection("");
            XConomy.getInstance().logger(null, 1,"==================================================");
            XConomy.getInstance().logger(null, 1,"Found Old Semi-mode Cache. Conversion start");
            int count = 0;
            for (String key : section.getKeys(false)) {
                SQLCreateNewAccount.createDUUIDLink(CacheSubUUID.getString(key), key, connection);
                count += 1;
            }
            XConomy.getInstance().logger(null, 1,"Finished conversion ----- Count: " + count);
            XConomy.getInstance().logger(null, 1,"==================================================");
            if (!newcachesubuuid.exists()) {
                cachesubuuid.renameTo(newcachesubuuid);
            }
            SQL.database.closeHikariConnection(connection);
        }
    }
}