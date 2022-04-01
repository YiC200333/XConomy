/*
 *  This file (PlayerData.java) is a part of project XConomy
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
package me.yic.xconomy.utils;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerData {
    private final UUID u;
    private final String name;
    private BigDecimal balance;

    public PlayerData(UUID u, String name, BigDecimal balance) {
        this.u = u;
        this.name = name;
        this.balance = balance;
    }

    public UUID getUniqueId() {
        return u;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isValid() {
        return this.u != null && this.balance != null;
    }

}