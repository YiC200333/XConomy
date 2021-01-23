/*
 *  This file (XConomyAPI.java) is a part of project XConomy
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
package me.yic.xconomy;

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.utils.PluginINFO;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("unused")
public class XConomyAPI {

    public String getversion() {
        return PluginINFO.VERSION;
    }

    public Boolean isbungeecordmode() {
        return ServerINFO.IsBungeeCordMode;
    }

    public UUID translateUUID(String playername) {
        return Cache.translateUUID(playername, null);
    }

    public BigDecimal formatdouble(String amount) {
        return DataFormat.formatString(amount);
    }

    public String getdisplay(BigDecimal balance) {
        return DataFormat.shown(balance);
    }

    public BigDecimal getbalance(UUID uid) {
        return Cache.getBalanceFromCacheOrDB(uid);
    }

    public Boolean ismaxnumber(BigDecimal amount) {
        return DataFormat.isMAX(amount);
    }

    public Integer changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd) {
        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return 1;
        }
        BigDecimal bal = getbalance(u);
        if (isadd) {
            if (ismaxnumber(bal.add(amount))) {
                return 3;
            }
        } else {
            if (bal.compareTo(amount) < 0) {
                return 2;
            }
        }
        Cache.change("PLUGIN_API", u, playername, amount, isadd, "N/A");
        return 0;
    }

    public List<String> getbalancetop() {
        return Cache.baltop_papi;
    }

    public BigDecimal getsumbalance() {
        return Cache.sumbalance;
    }
}
