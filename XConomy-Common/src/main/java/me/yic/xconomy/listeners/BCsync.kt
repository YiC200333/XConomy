/*
 *  This file (BCsync.kt) is a part of project XConomy
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
package me.yic.xconomy.listeners

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import me.yic.xconomy.XConomyBungee
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.Server
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.*

class BCsync : Listener {

    @Suppress("Unused","UnstableApiUsage")
    @EventHandler
    fun on(event: PluginMessageEvent) {
        if (event.sender !is Server) {
            return
        }
        if (!event.tag.equals("xconomy:acb", ignoreCase = true)) {
            return
        }
        val input = ByteStreams.newDataInput(event.data)
        val senderServer = event.sender as Server
        val output = ByteStreams.newDataOutput()
        val type = input.readUTF()
        if (type.equals("balance", ignoreCase = true)) {
            output.writeUTF("balance")
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
        } else if (type.equals("message", ignoreCase = true)) {
            output.writeUTF("message")
            output.writeUTF(input.readUTF())
            val uid = input.readUTF()
            val p = ProxyServer.getInstance().getPlayer(UUID.fromString(uid))
            if (p != null) {
                output.writeUTF(uid)
                output.writeUTF(input.readUTF())
            } else {
                return
            }
        } else if (type.equals("balanceall", ignoreCase = true)) {
            output.writeUTF("balanceall")
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
        } else if (type.equals("broadcast", ignoreCase = true)) {
            output.writeUTF("broadcast")
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
        } else if (type.equals("updateplayer", ignoreCase = true)) {
            output.writeUTF("updateplayer")
            output.writeUTF(input.readUTF())
            output.writeUTF(input.readUTF())
        }
        for (s in ProxyServer.getInstance().servers.values) {
            if (s.name != senderServer.info.name && s.players.size > 0) {
                ProxyServer.getInstance().scheduler.runAsync(
                    XConomyBungee.getInstance()
                ) { SendMessTaskB(s, output) }
            }
        }
    }

    fun SendMessTaskB(s: ServerInfo, stream: ByteArrayDataOutput) {
        s.sendData("xconomy:aca", stream.toByteArray())
    }
}