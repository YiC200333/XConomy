/*
 *  This file (BCPlayerEvent.kt) is a part of project XConomy
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
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class BCPlayerEvent: Listener {

    @Suppress("Unused","UnstableApiUsage")
    @EventHandler
    fun join(event: ServerConnectedEvent) {
        val playername = event.player.name
        val output = ByteStreams.newDataOutput()
        output.writeUTF("Join")
        output.writeUTF(playername)
        for (s in ProxyServer.getInstance().servers.values) {
            if (s.players.size > 0) {
                ProxyServer.getInstance().scheduler.runAsync(
                    XConomyBungee.getInstance()
                ) { SendMessTaskB(s, output) }
            }
        }
    }

    @Suppress("Unused","UnstableApiUsage")
    @EventHandler
    fun quit(event: PlayerDisconnectEvent) {
        val playername = event.player.name
        val output = ByteStreams.newDataOutput()
        output.writeUTF("Quit")
        output.writeUTF(playername)
        for (s in ProxyServer.getInstance().servers.values) {
            if (s.players.size > 0) {
                ProxyServer.getInstance().scheduler.runAsync(
                    XConomyBungee.getInstance()
                ) { SendMessTaskB(s, output) }
            }
        }
    }

    fun SendMessTaskB(s: ServerInfo, stream: ByteArrayDataOutput) {
        s.sendData("xconomy:global", stream.toByteArray())
    }
}