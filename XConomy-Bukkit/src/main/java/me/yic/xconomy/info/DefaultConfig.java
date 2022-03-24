package me.yic.xconomy.info;/*
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

import me.yic.xconomy.XConomy;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;

public class DefaultConfig {
    public static FileConfiguration config;

    public boolean IS_ONLINEMODE = IsOnlineMode();
    public boolean IS_SEMIONLINEMODE = IsSemiOnlineMode();

    public String LANGUAGE = config.getString("Settings.language");
    public boolean CHECK_UPDATE = config.getBoolean("Settings.check-update");
    public int REFRESH_TIME = Math.max(config.getInt("Settings.refresh-time"), 30);
    public boolean ECO_COMMAND = config.getBoolean("Settings.eco-command");
    public boolean DISABLE_ESSENTIAL = config.getBoolean("Settings.disable-essentials");
    public double INITIAL_BAL = config.getDouble("Settings.initial-bal");
    public BigDecimal PAYMENT_TAX = BigDecimal.ZERO;
    public int RANKING_SIZE = getrankingsize();
    public int LINES_PER_PAGE = config.getInt("Settings.lines-per-page");
    public boolean NON_PLAYER_ACCOUNT = config.getBoolean("Settings.non-player-account");
    public boolean DISABLE_CACHE = config.getBoolean("Settings.disable-cache");
    public boolean TRANSACTION_RECORD = config.getBoolean("Settings.transaction-record");
    public boolean USERNAME_IGNORE_CASE = config.getBoolean("Settings.username-ignore-case");

    public String SINGULAR_NAME = config.getString("Currency.singular-name");
    public String PLURAL_NAME = config.getString("Currency.plural-name");
    public boolean INTEGER_BAL = config.getBoolean("Currency.integer-bal");
    public String THOUSANDS_SEPARATOR = config.getString("Currency.thousands-separator");
    public String DISPLAY_FORMAT = config.getString("Currency.display-format");
    public String MAX_NUMBER = config.getString("Currency.max-number");

    public boolean BUNGEECORD_ENABLE = false;
    public String BUNGEECORD_SIGN = config.getString("BungeeCord.sign");


    private int getrankingsize(){
        return Math.min(config.getInt("Settings.ranking-size"), 100);
    }


    @SuppressWarnings("ConstantConditions")
    private boolean IsOnlineMode(){
        return config.getString("Settings.UUID-mode").equalsIgnoreCase("Online");
    }

    @SuppressWarnings("ConstantConditions")
    private boolean IsSemiOnlineMode(){
        return config.getString("Settings.UUID-mode").equalsIgnoreCase("SemiOnline");
    }

    public void setBungeecord() {
        if (!config.getBoolean("BungeeCord.enable")) {
            return;
        }

        if (XConomy.DConfig.getStorageType() == 0 || XConomy.DConfig.getStorageType() == 1) {
            BUNGEECORD_ENABLE = !XConomy.DConfig.gethost().equalsIgnoreCase("Default");
        }

        BUNGEECORD_ENABLE = true;

    }
}
