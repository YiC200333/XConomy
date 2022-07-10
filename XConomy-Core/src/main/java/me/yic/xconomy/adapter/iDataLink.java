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
package me.yic.xconomy.adapter;


import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.PlayerData;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface iDataLink {

    boolean create();

    void newPlayer(CPlayer a);

    boolean newPlayer(UUID uid, String name);

    CPlayer getplayer(PlayerData pd);

    void updatelogininfo(UUID uid);

    void selectlogininfo(CPlayer pp);

    <T> void getPlayerData(T key);

    void getBalNonPlayer(String u);

    void getTopBal();

    void setTopBalHide(UUID u, int type);

    String getBalSum();

    void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri);


    void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri);

    void saveNonPlayer(String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd, RecordInfo ri);
}
