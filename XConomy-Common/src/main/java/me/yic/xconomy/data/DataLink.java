/*
 *  This file (DataLink.java) is a part of project XConomy
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

import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.PlayerData;

import java.math.BigDecimal;
import java.util.UUID;


@SuppressWarnings("unused")
public class DataLink {

    public static void updatelogininfo(UUID uid) {
    }

    public static <T> void getPlayerData(T key) {
        if (key instanceof UUID) {
            SQL.getPlayerData((UUID) key);
        } else if (key instanceof String) {
            SQL.getPlayerData((String) key);
        }
    }

    public static void getBalNonPlayer(String u) {
        SQL.getNonPlayerData(u);
    }

    public static void getTopBal() {
        SQL.getBaltop();
    }

    public static void setTopBalHide(UUID u, int type) {
        SQL.hidetop(u, type);
    }

    public static String getBalSum() {
        if (SQL.sumBal() == null) {
            return "0.0";
        }
        return SQL.sumBal();
    }

    public static void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        SQL.save(pd, isAdd, amount, ri);
    }


    public static void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
    }

    public static void saveNonPlayer(String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd, RecordInfo ri) {
        SQL.saveNonPlayer(account, amount, newbalance, isAdd, ri);
    }

}
