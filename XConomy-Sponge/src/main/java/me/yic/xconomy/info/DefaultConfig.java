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

import com.google.common.reflect.TypeToken;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.UUIDMode;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.math.BigDecimal;
import java.util.*;

public class DefaultConfig {
    public static ConfigurationNode config;

    public DefaultConfig() {
        setUUIDMode();
        setnonplayeraccount();
        setformatbalance();
    }

    public UUIDMode UUIDMODE = UUIDMode.DEFAULT;

    public String LANGUAGE = config.getNode("Settings", "language").getString();
    public boolean CHECK_UPDATE = config.getNode("Settings", "check-update").getBoolean();
    public int REFRESH_TIME = Math.max(config.getNode("Settings", "refresh-time").getInt(), 30);
    public boolean ECO_COMMAND = config.getNode("Settings", "eco-command").getBoolean();
    //public boolean DISABLE_ESSENTIAL = config.getNode("Settings", "disable-essentials").getBoolean();
    public String INITIAL_BAL = config.getNode("Settings", "initial-bal").getString();
    public BigDecimal PAYMENT_TAX = BigDecimal.ZERO;
    public int RANKING_SIZE = getrankingsize();
    public int LINES_PER_PAGE = config.getNode("Settings", "lines-per-page").getInt();
    public boolean NON_PLAYER_ACCOUNT = config.getNode("non-player-account","enable").getBoolean();
    public List<String> NON_PLAYER_ACCOUNT_SUBSTRING = null;
    public boolean DISABLE_CACHE = config.getNode("Settings", "disable-cache").getBoolean();
    public boolean TRANSACTION_RECORD = config.getNode("Settings", "transaction-record").getBoolean();
    public boolean USERNAME_IGNORE_CASE = config.getNode("Settings", "username-ignore-case").getBoolean();

    public String SINGULAR_NAME = config.getNode("Currency", "singular-name").getString();
    public String PLURAL_NAME = config.getNode("Currency", "plural-name").getString();
    public boolean INTEGER_BAL = config.getNode("Currency", "integer-bal").getBoolean();
    public String THOUSANDS_SEPARATOR = config.getNode("Currency", "thousands-separator").getString();
    public String DISPLAY_FORMAT = config.getNode("Currency", "display-format").getString();
    public String MAX_NUMBER = config.getNode("Currency", "max-number").getString();
    public List<Integer> FORMAT_BALANCE = null;
    public HashMap<Integer, String> FORMAT_BALANCE_C = new HashMap<>();

    public boolean BUNGEECORD_ENABLE = false;
    public String BUNGEECORD_SIGN = config.getNode("BungeeCord", "sign").getString();


    private int getrankingsize() {
        return Math.min(config.getNode("Settings", "ranking-size").getInt(), 100);
    }

    @SuppressWarnings("ConstantConditions")
    private void setUUIDMode() {
        if (config.getNode("UUID-mode").getString().equalsIgnoreCase("Online")) {
            UUIDMODE = UUIDMode.ONLINE;
        } else if (config.getNode("UUID-mode").getString().equalsIgnoreCase("Offline")) {
            if (!USERNAME_IGNORE_CASE) {
                UUIDMODE = UUIDMode.OFFLINE;
            }
        } else if (config.getNode("UUID-mode").getString().equalsIgnoreCase("SemiOnline")) {
            UUIDMODE = UUIDMode.SEMIONLINE;
        }
        XConomy.getInstance().logger(null, 0, UUIDMODE.toString());
    }


    private void setformatbalance() {
        FORMAT_BALANCE = new ArrayList<>();
        try {
            ConfigurationNode section = config.getNode("Currency", "format-balance");
            for (Map.Entry<Object, ? extends ConfigurationNode> key : section.getChildrenMap().entrySet()) {
                int x = Integer.parseInt(key.getKey().toString());
                if (x > 0) {
                    FORMAT_BALANCE.add(x);
                    FORMAT_BALANCE_C.put(x, key.getValue().getString());
                }
            }
            Collections.sort(FORMAT_BALANCE);
        } catch (Exception ignored) {
            FORMAT_BALANCE = null;
            XConomy.getInstance().logger(null, 1, "Error getting balance custom format");
        }
        if (FORMAT_BALANCE.isEmpty()){
            FORMAT_BALANCE = null;
            XConomy.getInstance().logger(null, 1, "Error getting balance custom format");
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private void setnonplayeraccount() {
        if (NON_PLAYER_ACCOUNT) {
            if (config.getNode("non-player-account","whitelist","enable").getBoolean()) {
                try {
                    NON_PLAYER_ACCOUNT_SUBSTRING = config.getNode("non-player-account","whitelist","fields-list").getList(TypeToken.of(String.class));
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setBungeecord() {
        if (!config.getNode("BungeeCord","enable").getBoolean()) {
            return;
        }

        if (XConomy.DConfig.getStorageType() == 0 || XConomy.DConfig.getStorageType() == 1) {
            BUNGEECORD_ENABLE = !XConomy.DConfig.gethost().equalsIgnoreCase("Default");
        }

        BUNGEECORD_ENABLE = true;

    }
}
