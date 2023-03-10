/*
 *  This file (Messages.kt) is a part of project XConomy
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
package me.yic.xconomy.lang

class Messages {
    fun gettag(message: String?): String? {
        var tag = message
        when (message) {
            "数据保存方式" -> tag = "saving-mode"
            "自定义文件夹路径不存在" -> tag = "no-custom-path"
            "连接正常" -> tag = "connect-success"
            "连接异常" -> tag = "connect-fail"
            "重新连接成功" -> tag = "reconnect-success"
            "连接断开失败" -> tag = "disconnect-fail"
            "缓存文件创建异常" -> tag = "cache-file-creation-exception"
            "升级数据库表格。。。" -> tag = "upgrade-database"
            "Redis监听线程创建中" -> tag = "redis-create"
            "订阅Redis频道成功, channel " -> tag = "redis-subscribe"
            "取消订阅Redis频道" -> tag = "redis-unsubscribe"
            "XConomy加载成功" -> tag = "enable-success"
            "XConomy已成功卸载" -> tag = "disable-success"
            "已开启BungeeCord同步" -> tag = "enable-bungeecord"
            "SQLite文件路径设置错误" -> tag = "custom-path-error"
            "文件夹创建异常" -> tag = "create-folder-fail"
            "BungeeCord同步未开启" -> tag = "not-enable-bungeecord"
            "无法连接到数据库-----" -> tag = "unable-connect"
            "JDBC驱动加载失败" -> tag = "jdbc-fail"
            "已创建一个新的语言文件" -> tag = "create-language-file-success"
            "语言文件创建异常" -> tag = "create-language-file-fail"
            "发现 PlaceholderAPI" -> tag = "found-placeholderapi"
            "发现 DatabaseDrivers" -> tag = "found-databasedrivers"
            "已是最新版本" -> tag = "is-new-version"
            "检查更新失败" -> tag = "check-version-fail"
            "发现新版本 " -> tag = "found-version"
            "§amessage.yml重载成功" -> tag = "messege-reload"
            "vault-baltop-tips-a" -> tag = "vault-baltop-tips-a"
            "vault-baltop-tips-b" -> tag = "vault-baltop-tips-b"
            " 名称已更改!" -> tag = "username-modified"
            "§cBC模式开启的情况下,无法在无人的服务器中使用OP命令" -> tag = "no-player-tips"
            "§c该指令不支持在半正版模式中使用" -> tag = "semi-mode-ban-commands"
            "§6控制台无法使用该指令" -> tag = "console-ban-commands"
            "连接池未启用" -> tag = "pool-disable"
            "未找到 'org.slf4j.Logger'" -> tag = "slf4j-unfound"
            "收到不同版本插件的数据，无法同步，当前插件版本 " -> tag = "different-version"
        }
        return tag
    }

}