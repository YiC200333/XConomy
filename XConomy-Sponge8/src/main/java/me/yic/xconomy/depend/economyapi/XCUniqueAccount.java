/*
 *  This file (XCUniqueAccount.java) is a part of project XConomy
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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.syncdata.PlayerData;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class XCUniqueAccount implements UniqueAccount {

    private final UUID uuid;
    //private final String name;

    public XCUniqueAccount(User player) {
        this.uuid = player.uniqueId();
        //this.name = player.getName();
    }

    @Override
    public Component displayName() {
        return Component.text("XConomy");
    }

    @Override
    public BigDecimal defaultBalance(Currency currency) {
        return DataFormat.formatdouble(XConomyLoad.Config.INITIAL_BAL);
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        return DataCon.getPlayerData(uuid) != null;
    }

    @Override
    public boolean hasBalance(Currency currency, Cause cause) {
        return hasBalance(currency, (Set<Context>) null);
    }

    @Override
    public BigDecimal balance(Currency currency, Set<Context> contexts) {
        PlayerData pd = DataCon.getPlayerData(uuid);
        if (pd != null) {
            return pd.getBalance();
        } else {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public BigDecimal balance(Currency currency, Cause cause) {
        return balance(currency, (Set<Context>) null);
    }

    @Override
    public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
        return null;
    }

    @Override
    public Map<Currency, BigDecimal> balances(Cause cause) {
        return null;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
        if (AdapterManager.BanModiftyBalance()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED);
        }

        if (DataFormat.isMAX(amount)) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED);
        }
        DataCon.changeplayerdata("PLUGIN", uuid, amount, null, "SETBALANCE", null);
        return new XCTransactionResult(this,
                currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS);
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
        return setBalance(currency, amount, (Set<Context>) null);
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
        return null;
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
         if (AdapterManager.BanModiftyBalance()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED);
        }

        DataCon.changeplayerdata("PLUGIN", uuid, defaultBalance(currency), null, "RESETBALANCE", null);
        return new XCTransactionResult(this,
                currency, BigDecimal.ZERO, contexts, ResultType.SUCCESS);
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause) {
        return resetBalance(currency, (Set<Context>) null) ;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
         if (AdapterManager.BanModiftyBalance()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        BigDecimal bal = balance(currency, contexts);
        BigDecimal amountFormatted = DataFormat.formatBigDecimal(amount);

        if (DataFormat.isMAX(bal.add(amountFormatted))) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        DataCon.changeplayerdata("PLUGIN", uuid, amountFormatted, true, null, null);
        return new XCTransactionResult(this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.DEPOSIT);

    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
        return deposit(currency, amount, (Set<Context>) null);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
         if (AdapterManager.BanModiftyBalance()) {
            return new XCTransactionResult(this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }

        BigDecimal bal = balance(currency, contexts);
        BigDecimal amountFormatted = DataFormat.formatBigDecimal(amount);

        if (bal.compareTo(amount) < 0) {
            return new XCTransactionResult(this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }

        DataCon.changeplayerdata("PLUGIN", uuid, amountFormatted, false, null, null);
        return new XCTransactionResult(this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.WITHDRAW);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
        return withdraw(currency, amount, (Set<Context>) null);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
         if (AdapterManager.BanModiftyBalance()) {
            return new XCTransferResult(to, this,
                    currency, BigDecimal.ZERO, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        TransactionResult resultto = to.deposit(currency, amount, contexts);
        if (resultto.result() == ResultType.FAILED) {
            return new XCTransferResult(to, this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        TransactionResult result = withdraw(currency, amount, contexts);
        if (result.result() == ResultType.FAILED) {
            to.deposit(currency, amount, contexts);
            return new XCTransferResult(to, this,
                    currency, amount, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        return new XCTransferResult(to, this,
                currency, amount, contexts, ResultType.SUCCESS, TransactionTypes.TRANSFER);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
        return transfer(to, currency, amount, (Set<Context>) null);
    }

    @Override
    public String identifier() {
        return uuid.toString();
    }


    @Override
    public UUID uniqueId() {
        return uuid;
    }
}
