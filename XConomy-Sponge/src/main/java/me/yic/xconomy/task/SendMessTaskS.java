package me.yic.xconomy.task;/*
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
 */

import com.google.common.io.ByteArrayDataOutput;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;

import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS {

    public static void Scheduler(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                                 BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() ->
                SendMess(stream, type, u, player, isAdd, balance, amount, newbalance, command));

    }

    public static void SendMess(ByteArrayDataOutput stream, String type, UUID u, String player, Boolean isAdd,
                                BigDecimal balance, BigDecimal amount, BigDecimal newbalance, String command) {
        if (ServerINFO.IsBungeeCordMode) {
            Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                    Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> stream.toByteArray());
        }
        if (u != null) {
            DataCon.save(type, u, player, isAdd, balance, amount, newbalance, command);
        }
    }
}