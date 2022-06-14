/*
 *  This file (SyncBalanceAll.java) is a part of project XConomy
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
package me.yic.xconomy.data.syncdata;

import me.yic.xconomy.info.SyncType;

import java.math.BigDecimal;

public class SyncBalanceAll extends SyncData{

    private final boolean isall;
    private final Boolean c;
    private final BigDecimal amount;

    public SyncBalanceAll(String sign, boolean isall, Boolean c, BigDecimal amount){
        super(sign, SyncType.BALANCEALL, null);
        this.isall = isall;
        this.c = c;
        this.amount = amount;
    }

    public boolean getisOnline(){
        return !isall;
    }

    public Boolean getC(){
        return c;
    }

    public BigDecimal getAmount(){
        return amount;
    }
}
