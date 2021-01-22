/*
 *  This file (SendMessTaskS.java) is a part of project XConomy
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
 */package me.yic.xconomy.task;

import com.google.common.io.ByteArrayDataOutput;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import org.spongepowered.api.Sponge;

import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS implements Runnable {
    private final ByteArrayDataOutput stream;
    private final String type;
    private final UUID u;
    private final String player;
    private final Boolean isAdd;
    private final BigDecimal balance;
    private final BigDecimal amount;
    private final BigDecimal newbalance;
    private final String command;

    public SendMessTaskS(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                         BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        this.stream = stream;
        this.type = type;
        this.u = u;
        this.player = player;
        this.isAdd = isAdd;
        this.balance = balance;
        this.amount = amount;
        this.newbalance = newbalance;
        this.command = command;
    }

    @Override
    public void run() {
        Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(stream.toByteArray()));
        if (u != null) {
            DataCon.save(type, u, player, isAdd, balance, amount, newbalance, command);
        }
    }



}