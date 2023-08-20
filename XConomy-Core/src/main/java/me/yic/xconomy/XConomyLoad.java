/*
 *  This file (XConomyLoad.java) is a part of project XConomy
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

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.info.DataBaseConfig;
import me.yic.xconomy.info.DefaultConfig;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.utils.RedisConnection;

public class XConomyLoad{
    public static boolean DDrivers = false;

    public static DataBaseConfig DConfig;
    public static DefaultConfig Config;

    public static void LoadConfig(){

        Config = new DefaultConfig();
        DConfig = new DataBaseConfig();

        MessagesManager.loadsysmess();
        MessagesManager.loadlangmess();

        DConfig.Initialization();
        Config.setSyncData();
    }

    public static void Initial(){
        DataCon.baltop();

        if (Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD)) {
            if ((DConfig.getStorageType() == 0 || DConfig.getStorageType() == 1)
                    && (DConfig.gethost().equalsIgnoreCase("Default"))) {
                XConomy.getInstance().logger("SQLite文件路径设置错误", 1, null);
                XConomy.getInstance().logger("BungeeCord同步未开启", 1, null);
                Config.SYNCDATA_TYPE = SyncChannalType.OFF;
            } else {
                AdapterManager.PLUGIN.registerIncomingPluginChannel("xconomy:aca", "me.yic.xconomy.listeners.SPsync");
                AdapterManager.PLUGIN.registerOutgoingPluginChannel("xconomy:acb");
                XConomy.getInstance().logger("已开启BungeeCord同步", 0, null);
            }
        }

        DataFormat.load();
    }

    public static void Unload() {
        if (Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD)) {
            AdapterManager.PLUGIN.unregisterIncomingPluginChannel("xconomy:aca", "me.yic.xconomy.listeners.SPsync");
            AdapterManager.PLUGIN.unregisterOutgoingPluginChannel("xconomy:acb");
        }else if(Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            RedisConnection.close();
        }

        //AdapterManager.ScheduledThreadPool.shutdown();
        //AdapterManager.FixedThreadPool.shutdown();
        SQL.close();
    }

    public static boolean getSyncData_Enable(){
        return !Config.SYNCDATA_TYPE.equals(SyncChannalType.OFF);
    }

}
