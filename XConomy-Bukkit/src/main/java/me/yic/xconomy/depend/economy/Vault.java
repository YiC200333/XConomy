/*
 *  This file (Vault.java) is a part of project XConomy
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
package me.yic.xconomy.depend.economy;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.utils.PlayerData;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Vault extends AbstractEconomy {

    @Override
    public EconomyResponse bankBalance(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean createPlayerAccount(String name) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String name, String arg1) {
        return createPlayerAccount(name);
    }

    @Override
    public String currencyNamePlural() {
        return XConomy.Config.PLURAL_NAME;
    }

    @Override
    public String currencyNameSingular() {
        return XConomy.Config.SINGULAR_NAME;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        if (XConomy.Config.BUNGEECORD_ENABLE && Bukkit.getOnlinePlayers().isEmpty() && !XConomy.Config.DISABLE_CACHE) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
                    "[BungeeCord] No player in server");
        }

        double bal = getBalance(name);
        BigDecimal amountFormatted = DataFormat.formatdouble(amount);

        if (DataFormat.isMAX(DataFormat.formatdouble(bal).add(amountFormatted))) {
            return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Max balance!");
        }

        if (isNonPlayerAccount(name)) {
            CacheNonPlayer.change(name, amountFormatted, true, "PLUGIN");
            return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
        }

        PlayerData pd = DataCon.getPlayerData(name);
        if (pd.getUniqueId() == null) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "No Account!");
        }

        DataCon.change("PLUGIN", pd.getUniqueId(), amountFormatted, true, "N/A");
        return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer pp, double amount) {
        if (XConomy.Config.BUNGEECORD_ENABLE && Bukkit.getOnlinePlayers().isEmpty() && !XConomy.Config.DISABLE_CACHE) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
                    "[BungeeCord] No player in server");
        }

        if (DataCon.getPlayerData(pp.getUniqueId()).getUniqueId() == null) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "No Account!");
        }

        double bal = getBalance(pp);
        BigDecimal amountFormatted = DataFormat.formatdouble(amount);

        if (DataFormat.isMAX(DataFormat.formatdouble(bal).add(amountFormatted))) {
            return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Max balance!");
        }

        DataCon.change("PLUGIN", pp.getUniqueId(), amountFormatted, true, "N/A");
        return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String name, String arg1, double amount) {
        return depositPlayer(name, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer pp, String arg1, double amount) {
        return depositPlayer(pp, amount);
    }

    @Override
    public String format(double sum) {
        return DataFormat.shownd(sum);
    }

    @Override
    public int fractionalDigits() {
        if (DataFormat.isint) {
            return 0;
        }
        return 2;
    }

    @Override
    public double getBalance(String name) {
        if (isNonPlayerAccount(name)) {
            return CacheNonPlayer.getBalanceFromCacheOrDB(name).doubleValue();
        }
        return DataCon.getPlayerData(name).getbalance().doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer aa) {
        UUID uuid = aa.getUniqueId();
        return DataCon.getPlayerData(uuid).getbalance().doubleValue();
    }

    @Override
    public double getBalance(String name, String arg1) {
        return getBalance(name);
    }

    @Override
    public double getBalance(OfflinePlayer aa, String arg1) {
        return getBalance(aa);
    }

    @Override
    public List<String> getBanks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return "XConomy";
    }

    @Override
    public boolean has(String name, double amount) {
        return getBalance(name) >= amount;
    }

    @Override
    public boolean has(String name, String arg1, double amount) {
        return has(name, amount);
    }

    @Override
    public boolean has(OfflinePlayer pp, double amount) {
        return getBalance(pp) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer pp, String arg1, double amount) {
        return has(pp, amount);
    }


    @Override
    public boolean hasAccount(String name) {
        if (isNonPlayerAccount(name)) {
            return true;
        }
        return DataCon.getPlayerData(name).getUniqueId() != null;
    }

    @Override
    public boolean hasAccount(String name, String arg1) {
        return hasAccount(name);
    }

    @Override
    public boolean hasAccount(OfflinePlayer pp) {
        return DataCon.getPlayerData(pp.getUniqueId()).getUniqueId() != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer pp, String arg1) {
        return hasAccount(pp);
    }

    @Override
    public boolean hasBankSupport() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        if (XConomy.Config.BUNGEECORD_ENABLE && Bukkit.getOnlinePlayers().isEmpty() && !XConomy.Config.DISABLE_CACHE) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
                    "[BungeeCord] No player in server");
        }

        double bal = getBalance(name);
        BigDecimal amountFormatted = DataFormat.formatdouble(amount);

        if (bal < amount) {
            return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Insufficient balance!");
        }

        if (isNonPlayerAccount(name)) {
            CacheNonPlayer.change(name, amountFormatted, false, "PLUGIN");
            return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
        }

        PlayerData pd = DataCon.getPlayerData(name);
        if (pd.getUniqueId() == null) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "No Account!");
        }

        DataCon.change("PLUGIN", pd.getUniqueId(), amountFormatted, false, "N/A");
        return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer pp, double amount) {
        if (XConomy.Config.BUNGEECORD_ENABLE && Bukkit.getOnlinePlayers().isEmpty() && !XConomy.Config.DISABLE_CACHE) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
                    "[BungeeCord] No player in server");
        }

        if (DataCon.getPlayerData(pp.getUniqueId()).getUniqueId() == null) {
            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "No Account!");
        }

        double bal = getBalance(pp);
        BigDecimal amountFormatted = DataFormat.formatdouble(amount);

        if (bal < amount) {
            return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Insufficient balance!");
        }

        DataCon.change("PLUGIN", pp.getUniqueId(), amountFormatted, false, "N/A");
        return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String arg1, double amount) {
        return withdrawPlayer(name, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer pp, String arg1, double amount) {
        return withdrawPlayer(pp, amount);
    }

    private boolean isNonPlayerAccount(String name) {
        if (!XConomy.Config.NON_PLAYER_ACCOUNT) {
            return false;
        }

        if (name.length() >= 17) {
            return true;
        }

        if (CacheNonPlayer.bal.containsKey(name)) {
            return true;
        }

        return DataCon.getPlayerData(name).getUniqueId() == null;
    }

}
