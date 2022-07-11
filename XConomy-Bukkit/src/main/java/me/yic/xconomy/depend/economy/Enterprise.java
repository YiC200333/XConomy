package me.yic.xconomy.depend.economy;/*
 *  This file (Enterprise.java) is a part of project XConomy
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

import com.github.sanctum.economy.construct.EconomyAction;
import com.github.sanctum.economy.construct.EconomyPriority;
import com.github.sanctum.economy.construct.account.Account;
import com.github.sanctum.economy.construct.account.Wallet;
import com.github.sanctum.economy.construct.account.permissive.AccountType;
import com.github.sanctum.economy.construct.currency.normal.EconomyCurrency;
import com.github.sanctum.economy.construct.implement.AdvancedEconomy;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.syncdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Enterprise implements AdvancedEconomy {


    @Override
    public Plugin getPlugin() {
        return XConomy.getInstance();
    }

    @Override
    public String getVersion() {
        return XConomy.getInstance().getDescription().getVersion();
    }

    @Override
    public EconomyCurrency getCurrency() {
        return null;
    }

    @Override
    public EconomyCurrency getCurrency(String s) {
        return null;
    }

    @Override
    public EconomyPriority getPriority() {
        return null;
    }

    @Override
    public String format(BigDecimal bigDecimal) {
        return DataFormat.shown(bigDecimal);
    }

    @Override
    public String format(BigDecimal bigDecimal, Locale locale) {
        return DataFormat.shown(bigDecimal);
    }

    @Override
    public BigDecimal getMaxWalletSize() {
        return DataFormat.maxNumber;
    }

    @Override
    public boolean isMultiWorld() {
        return false;
    }

    @Override
    public boolean isMultiCurrency() {
        return false;
    }

    @Override
    public boolean hasMultiAccountSupport() {
        return false;
    }

    @Override
    public boolean hasWalletSizeLimit() {
        return false;
    }

    @Override
    public Account getAccount(String s) {
        return null;
    }

    @Override
    public Account getAccount(String s, AccountType accountType) {
        return null;
    }

    @Override
    public Account getAccount(String s, String s1) {
        return null;
    }

    @Override
    public Account getAccount(OfflinePlayer offlinePlayer, AccountType accountType) {
        return null;
    }

    @Override
    public Account getAccount(OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public Account getAccount(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public Account getAccount(UUID uuid) {
        return null;
    }

    @Override
    public Account getAccount(UUID uuid, AccountType accountType) {
        return null;
    }

    @Override
    public Account getAccount(String s, UUID uuid) {
        return null;
    }

    @Override
    public Wallet getWallet(String name) {
        PlayerData pd = DataCon.getPlayerData(name);
        if (pd != null) {
            return getWallet(pd.getUniqueId());
        }
        return null;
    }

    @Override
    public Wallet getWallet(OfflinePlayer offlinePlayer) {
        return new EnterpriseWallet(offlinePlayer);
    }

    @Override
    public Wallet getWallet(UUID uuid) {
        return new EnterpriseWallet(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, String s) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, String s, String s1) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, String s, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, String s, String s1, String s2) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, String s, String s1, String s2, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, OfflinePlayer offlinePlayer, String s) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, OfflinePlayer offlinePlayer, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, OfflinePlayer offlinePlayer, String s, String s1) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, OfflinePlayer offlinePlayer, String s, String s1, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, UUID uuid) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, UUID uuid, String s) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, UUID uuid, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, UUID uuid, String s, String s1) {
        return null;
    }

    @Override
    public EconomyAction createAccount(AccountType accountType, UUID uuid, String s, String s1, BigDecimal bigDecimal) {
        return null;
    }

    @Override
    public EconomyAction deleteWalletAccount(Wallet wallet) {
        return null;
    }

    @Override
    public EconomyAction deleteWalletAccount(Wallet wallet, String s) {
        return null;
    }

    @Override
    public EconomyAction deleteAccount(String s) {
        return null;
    }

    @Override
    public EconomyAction deleteAccount(String s, String s1) {
        return null;
    }

    @Override
    public EconomyAction deleteAccount(Account account) {
        return null;
    }

    @Override
    public EconomyAction deleteAccount(Account account, String s) {
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return null;
    }

    @Override
    public List<String> getAccountList() {
        return null;
    }


}
