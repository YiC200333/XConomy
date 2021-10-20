package me.yic.xconomy.api.event;/*
 *  This file (PlayerAccountEvent.java) is a part of project XConomy
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

import org.bukkit.event.HandlerList;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerAccountEvent extends AccountEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private UUID u;
    private String reason;

    public PlayerAccountEvent(UUID u, String account, BigDecimal balance, BigDecimal amount, Boolean isadd, String reason, String method) {
        super(account, balance, amount, isadd, method);
        this.u = u;
        this.reason = reason;
    }

    public UUID getUniqueId() {
        return this.u;
    }

    public String getreason() {
        return this.reason;
    }
}
