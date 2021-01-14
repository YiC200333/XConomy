package me.yic.xconomy.data;/*
 *  This file (DataFormat.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

    public static boolean isInteger = false;
    public static DecimalFormat decimalFormat;
    public static BigDecimal maxNumber;
    final static String displayformat = XConomy.config.getNode("Currency","display-format").getString();
    final static String pluralname = XConomy.config.getNode("Currency","plural-name").getString();
    final static String singularname = XConomy.config.getNode("Currency","singular-name").getString();

    public static BigDecimal formatString(String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (isInteger) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public static BigDecimal formatDouble(Double am) {
        BigDecimal bigDecimal = BigDecimal.valueOf(am);
        if (isInteger) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String shown(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) > 0) {
            return displayformat.replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", pluralname);
        }
        return displayformat
                .replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", singularname);
    }

    @SuppressWarnings("ConstantConditions")
    public static String shownd(Double am) {
        if (am > 1) {
            return displayformat.replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", pluralname);
        }
        return displayformat.replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", singularname);
    }

    public static boolean isMAX(BigDecimal am) {
        return am.compareTo(maxNumber) > 0;
    }

    public static void load() {
        maxNumber = setmaxnumber();
        isInteger = XConomy.config.getNode("Currency","integer-bal").getBoolean();
        String format = "###,##0";
        String gpoint = XConomy.config.getString("Currency.thousands-separator");

        if (isInteger) {
            decimalFormat = new DecimalFormat(format);
            return;
        }

        format = format + ".00";
        decimalFormat = new DecimalFormat(format);
        DecimalFormatSymbols spoint = new DecimalFormatSymbols();
        spoint.setDecimalSeparator('.');
        if (gpoint != null && gpoint.length() == 1) {
            spoint.setGroupingSeparator(gpoint.charAt(0));
        }
        decimalFormat.setDecimalFormatSymbols(spoint);
    }


    private static BigDecimal setmaxnumber() {
        String maxn = XConomy.config.getString("Currency.max-number");
        BigDecimal defaultmaxnumber = new BigDecimal("10000000000000000");
        if (maxn == null) {
            return defaultmaxnumber;
        }
        if (maxn.length() > 17) {
            return defaultmaxnumber;
        }
        BigDecimal mnumber = new BigDecimal(maxn);
        if (mnumber.compareTo(defaultmaxnumber) >= 0) {
            return defaultmaxnumber;
        } else {
            return mnumber;
        }
    }
}
