/*
 *  This file (XCurrency.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataFormat;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

@SuppressWarnings("NullableProblems")
public class XCurrency implements Currency {

    @Override
    public Text getDisplayName() {
        return Text.of(XConomy.Config.SINGULAR_NAME);
    }

    @Override
    public Text getPluralDisplayName() {
        return Text.of(XConomy.Config.PLURAL_NAME);
    }


    @Override
    public Text getSymbol() {
        return Text.of(XConomy.Config.THOUSANDS_SEPARATOR);
    }

    @Override
    public Text format(BigDecimal amount, int numFractionDigits) {
        return Text.of(DataFormat.formatBigDecimal(amount));
    }

    @Override
    public int getDefaultFractionDigits() {
        if (DataFormat.isint) {
            return 0;
        }
        return 2;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String getId() {
        return "xconomy";
    }

    @Override
    public String getName() {
        return "XConomy";
    }
}
