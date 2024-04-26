/*
 *  This file (DefaultConfig.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.comp.CConfig;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DefaultConfig {
    public static CConfig config;

    public DefaultConfig() {
        //setMaxThread();
        setUUIDMode();
        setnonplayeraccount();
        setformatbalance();
        setpaytips();
    }
    public boolean ISOLDCONFIG = false;

    public UUIDMode UUIDMODE = UUIDMode.DEFAULT;
    public boolean IMPORTMODE = config.getBoolean("Importdata-mode");

    public String LANGUAGE = config.getString("Settings.language");
    public boolean CHECK_UPDATE = config.getBoolean("Settings.check-update");
    public int REFRESH_TIME = Math.max(config.getInt("Settings.refresh-time"), 30);
    public boolean ECO_COMMAND = config.getBoolean("Settings.eco-command");
    public boolean DISABLE_ESSENTIAL = config.getBoolean("Settings.disable-essentials");
    public double INITIAL_BAL = config.getDouble("Settings.initial-bal");
    public BigDecimal PAYMENT_TAX = BigDecimal.ZERO;
    public int RANKING_SIZE = getrankingsize();
    public int LINES_PER_PAGE = config.getInt("Settings.lines-per-page");
    public boolean DISABLE_CACHE = config.getBoolean("Settings.disable-cache");

    public boolean TRANSACTION_RECORD = config.getBoolean("Settings.transaction-record");
    public boolean PAY_TIPS = false;
    public boolean USERNAME_IGNORE_CASE = config.getBoolean("Settings.username-ignore-case");

    public boolean NON_PLAYER_ACCOUNT = config.getBoolean("non-player-account.enable");
    public List<String> NON_PLAYER_ACCOUNT_SUBSTRING = null;

    public String SINGULAR_NAME = config.getString("Currency.singular-name");
    public String PLURAL_NAME = config.getString("Currency.plural-name");
    public boolean INTEGER_BAL = config.getBoolean("Currency.integer-bal");
    public int ROUNDING_MODE = config.getInt("Currency.rounding-mode");
    public String THOUSANDS_SEPARATOR = config.getString("Currency.thousands-separator");
    public String DISPLAY_FORMAT = config.getString("Currency.display-format");
    public String MAX_NUMBER = config.getString("Currency.max-number");
    public List<BigDecimal> FORMAT_BALANCE = null;
    public LinkedHashMap<BigDecimal, String> FORMAT_BALANCE_C = null;

    public SyncChannalType SYNCDATA_TYPE = SyncChannalType.OFF;

    public String SYNCDATA_SIGN = config.getString("SyncData.sign");

    //==================================================
    public String RE_WORLD = config.getString("Region-Thread.world");
    public int RE_X = config.getInt("Region-Thread.range-x");
    public int RE_Y = config.getInt("Region-Thread.range-y");

    private int getrankingsize() {
        return Math.min(config.getInt("Settings.ranking-size"), 100);
    }


    private void setUUIDMode() {
        if (config.getString("UUID-mode").equalsIgnoreCase("Online")) {
            UUIDMODE = UUIDMode.ONLINE;
        } else if (config.getString("UUID-mode").equalsIgnoreCase("Offline")) {
            UUIDMODE = UUIDMode.OFFLINE;
            USERNAME_IGNORE_CASE = false;
        } else if (config.getString("UUID-mode").equalsIgnoreCase("SemiOnline")) {
            UUIDMODE = UUIDMode.SEMIONLINE;
        }
        XConomy.getInstance().logger(null, 0, UUIDMODE.toString());
    }

    private void setformatbalance() {
        FORMAT_BALANCE = new ArrayList<>();
        try {
            FORMAT_BALANCE_C = config.getConfigurationSectionSort("Currency.format-balance");
            FORMAT_BALANCE.addAll(FORMAT_BALANCE_C.keySet());
        } catch (Exception e) {
            e.printStackTrace();
            FORMAT_BALANCE = null;
            XConomy.getInstance().logger(null, 1, "Error getting balance custom format");
            return;
        }
        if (FORMAT_BALANCE.isEmpty()){
            FORMAT_BALANCE = null;
            XConomy.getInstance().logger(null, 1, "Error getting balance custom format");
        }
    }

/*    public void setMaxThread(){
        if (config.contains("Settings.core-poolsize")) {
            MAX_THREAD = config.getInt("Settings.core-poolsize");
            if (MAX_THREAD <= 1){
                MAX_THREAD = 1;
            }
        }
        AdapterManager.ScheduledThreadPool = Executors.newScheduledThreadPool(MAX_THREAD / 2);
        AdapterManager.FixedThreadPool = Executors.newFixedThreadPool(MAX_THREAD / 2);
    }*/


    private void setnonplayeraccount() {
        if (NON_PLAYER_ACCOUNT) {
            if (config.getBoolean("non-player-account.whitelist.enable")) {
                NON_PLAYER_ACCOUNT_SUBSTRING = config.getStringList("non-player-account.whitelist.fields-list");
            }
        }
    }

    public void setSyncData() {
        if (!config.contains("SyncData.enable")) {
            ISOLDCONFIG = true;
            return;
        }


//        if (XConomyLoad.DConfig.getStorageType() == 0 || XConomyLoad.DConfig.getStorageType() == 1) {
//            SYNCDATA_ENABLE = !XConomyLoad.DConfig.gethost().equalsIgnoreCase("Default");
//        }

        if (config.getBoolean("SyncData.enable")) {
            String channeltype = config.getString("SyncData.channel-type");
            if (channeltype.equalsIgnoreCase("BungeeCord")) {
                SYNCDATA_TYPE = SyncChannalType.BUNGEECORD;
            }else if (channeltype.equalsIgnoreCase("Redis")) {
                SYNCDATA_TYPE = SyncChannalType.REDIS;
            }
        }
    }

    private void setpaytips() {
        if (TRANSACTION_RECORD) {
            PAY_TIPS = config.getBoolean("Settings.offline-pay-transfer-tips");
        }
    }
}
