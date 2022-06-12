package me.yic.xconomy.api.event;/*
 *  This file (AccountEvent.java) is a part of project XConomy
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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class AccountEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String account;
    private final BigDecimal balance;
    private final BigDecimal amount;
    private final Boolean isadd;
    private final String method;

    public AccountEvent(String account, BigDecimal balance, BigDecimal amount, Boolean isadd, String method) {
        this.account = account;
        this.balance = balance;
        this.amount = amount;
        this.isadd = isadd;
        this.method = method;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getaccountname() {
        return this.account;
    }

    public BigDecimal getbalance() {
        return this.balance;
    }

    public BigDecimal getamount() {
        return this.amount;
    }

    public Boolean getisadd() {
        return this.isadd;
    }

    public String getmethod() {
        return this.method;
    }
}
