/*
 *  This file (XCVirtualAccount.java) is a part of project XConomy
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
package me.yic.xconomy.depend.economyapi;

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheNonPlayer;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.*;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
public class XCVirtualAccount implements VirtualAccount {

    private final String account;

    public XCVirtualAccount(String account) {
        this.account = account;
    }

    @Override
    public Text getDisplayName() {
        return Text.of("XConomy");
    }

    @Override
    public BigDecimal getDefaultBalance(Currency currency) {
        return DataFormat.formatString(ServerINFO.InitialAmount.toString());
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        return true;
    }

    @Override
    public BigDecimal getBalance(Currency currency, Set<Context> contexts) {
        if (XCEconomyCommon.isNonPlayerAccount(account)) {
            return CacheNonPlayer.getBalanceFromCacheOrDB(account);
        }

        UUID uuid = Cache.translateUUID(account, null);
        if (uuid != null) {
            if (Cache.getBalanceFromCacheOrDB(uuid) != null) {
                return Cache.getBalanceFromCacheOrDB(uuid);
            } else {
                return BigDecimal.ZERO;
            }
        }

        return getDefaultBalance(currency);
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) {
        return null;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED,
                    DummyObjectProvider.createFor(TransactionType.class, "SET"));
        }
        if (DataFormat.isMAX(amount)) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED,
                    DummyObjectProvider.createFor(TransactionType.class, "SET"));
        }
        if (XCEconomyCommon.isNonPlayerAccount(account)) {
            CacheNonPlayer.change(account, amount, null, "PLUGIN");
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS,
                    DummyObjectProvider.createFor(TransactionType.class, "SET"));
        }
        UUID playeruuid = Cache.translateUUID(account, null);
        if (playeruuid == null) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED,
                    DummyObjectProvider.createFor(TransactionType.class, "SET"));
        }
        Cache.change("PLUGIN", playeruuid, account, amount, null, "SETBALANCE");
        return new XCTransactionResult(this,
                currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS,
                DummyObjectProvider.createFor(TransactionType.class, "SET"));
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) {
        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED,
                    DummyObjectProvider.createFor(TransactionType.class, "RESET"));
        }
        if (XCEconomyCommon.isNonPlayerAccount(account)) {
            CacheNonPlayer.change(account, BigDecimal.ZERO, null, "PLUGIN");
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS,
                    DummyObjectProvider.createFor(TransactionType.class, "RESET"));
        }
        UUID playeruuid = Cache.translateUUID(account, null);
        if (playeruuid == null) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED,
                    DummyObjectProvider.createFor(TransactionType.class, "RESET"));
        }
        Cache.change("PLUGIN", playeruuid, account, getDefaultBalance(currency), null, "RESETBALANCE");
        return new XCTransactionResult(this,
                currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS,
                DummyObjectProvider.createFor(TransactionType.class, "RESET"));
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {

        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        BigDecimal bal = getBalance(currency, contexts);
        BigDecimal amountFormatted = DataFormat.formatBigDecimal(amount);

        if (DataFormat.isMAX(bal.add(amountFormatted))) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        if (XCEconomyCommon.isNonPlayerAccount(account)) {
            CacheNonPlayer.change(account, amount, true, "PLUGIN");
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.DEPOSIT);
        }
        UUID playeruuid = Cache.translateUUID(account, null);
        if (playeruuid == null) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }
        Cache.change("PLUGIN", playeruuid, account, amountFormatted, true, "N/A");
        return new XCTransactionResult(this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.DEPOSIT);

    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }

        BigDecimal bal = getBalance(currency, contexts);
        BigDecimal amountFormatted = DataFormat.formatBigDecimal(amount);

        if (bal.compareTo(amount) < 0) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }
        if (XCEconomyCommon.isNonPlayerAccount(account)) {
            CacheNonPlayer.change(account, amount, false, "PLUGIN");
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.WITHDRAW);
        }
        UUID playeruuid = Cache.translateUUID(account, null);
        if (playeruuid == null) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }
        Cache.change("PLUGIN", playeruuid, account, amountFormatted, false, "N/A");
        return new XCTransactionResult(this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.WITHDRAW);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (ServerINFO.IsBungeeCordMode & Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return new XCTransferResult(to,this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        TransactionResult resultto = to.deposit(currency, amount, cause, contexts);
        if (resultto.getResult() == ResultType.FAILED){
            return new XCTransferResult(to,this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        TransactionResult result = withdraw(currency, amount, cause, contexts);
        if (result.getResult() == ResultType.FAILED){
            to.deposit(currency, amount, cause, contexts);
            return new XCTransferResult(to,this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        return new XCTransferResult(to,this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.TRANSFER);
    }

    @Override
    public String getIdentifier() {
        return account;
    }

    @Override
    public Set<Context> getActiveContexts() {
        return new HashSet<>();
    }

}
