package me.yic.xconomy.api.event;/*
 *  This file (NonPlayerAccountEvent.java) is a part of project XConomy
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

import java.math.BigDecimal;

public class NonPlayerAccountEvent extends AccountEvent {

    public NonPlayerAccountEvent(String account, BigDecimal balance, BigDecimal amount, Boolean isadd, String method) {
        super(account, balance, amount, isadd, method);
    }

}
