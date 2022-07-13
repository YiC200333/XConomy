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
import net.kyori.adventure.text.Component;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

public class XCurrency implements Currency {

    @Override
    public Component displayName() {
        return Component.text(XConomy.Config.SINGULAR_NAME);
    }

    @Override
    public Component pluralDisplayName() {
        return Component.text(XConomy.Config.PLURAL_NAME);
    }


    @Override
    public Component symbol() {
        return Component.text(XConomy.Config.THOUSANDS_SEPARATOR);
    }

    @Override
    public Component format(BigDecimal amount, int numFractionDigits) {
        return Component.text(DataFormat.formatBigDecimal(amount).toString());
    }

    @Override
    public int defaultFractionDigits() {
        if (DataFormat.isint) {
            return 0;
        }
        return 2;
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
