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
package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.adapter.iDataLink;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.data.sql.SQLCreateNewAccount;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.PlayerData;

import java.math.BigDecimal;
import java.util.UUID;


@SuppressWarnings("unused")
public class DataLink implements iDataLink {


    @Override
    public boolean create() {
        return false;
    }

    @Override
    public void newPlayer(CPlayer a) {

    }

    @Override
    public boolean newPlayer(UUID uid, String name) {
        return false;
    }

    @Override
    public CPlayer getplayer(PlayerData pd) {
        return null;
    }

    @Override
    public void updatelogininfo(UUID uid) {

    }

    @Override
    public void selectlogininfo(CPlayer pp) {

    }

    @Override
    public <T> void getPlayerData(T key) {

    }

    @Override
    public void getBalNonPlayer(String u) {

    }

    @Override
    public void getTopBal() {

    }

    @Override
    public void setTopBalHide(UUID u, int type) {

    }

    @Override
    public String getBalSum() {
        return null;
    }

    @Override
    public void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {

    }

    @Override
    public void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {

    }

    @Override
    public void saveNonPlayer(String account, BigDecimal amount, BigDecimal newbalance, Boolean isAdd, RecordInfo ri) {

    }
}
