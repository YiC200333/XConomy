/*
 *  This file (AdapterManager.java) is a part of project XConomy
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

import me.yic.xconomy.adapter.comp.CChat;
import me.yic.xconomy.adapter.comp.CPlugin;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.lang.MessagesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AdapterManager {

    public static List<String> Tab_PlayerList = new ArrayList<>();
    public static boolean foundvaultpe = false;
    public static boolean foundvaultOfflinePermManager = false;

    public final static CPlugin PLUGIN = new CPlugin();

    public static ScheduledExecutorService FixedThreadPool;

    public static String translateColorCodes(MessageConfig message) {
        return CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message.toString()));
    }

    public static String translateColorCodes(String message) {
        return CChat.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message));
    }


    public static boolean BanModiftyBalance() {
        if (!XConomyLoad.getSyncData_Enable()){
            return false;
        }
        if (!PLUGIN.getOnlinePlayersisEmpty()){
            return false;
        }
        if (XConomyLoad.Config.DISABLE_CACHE){
            return false;
        }
        return !XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS);
    }

    public static void runTaskAsynchronously(Runnable runnable){
        FixedThreadPool.execute(runnable);
    }

    public static void runTaskLaterAsynchronously(Runnable runnable, long seconds){
        FixedThreadPool.schedule(runnable, seconds, TimeUnit.SECONDS);
    }

    public static ScheduledFuture<?> runTaskTimerAsynchronously(Runnable runnable, long seconds){
        return FixedThreadPool.scheduleAtFixedRate(runnable, 1, seconds, TimeUnit.SECONDS);
    }
}
