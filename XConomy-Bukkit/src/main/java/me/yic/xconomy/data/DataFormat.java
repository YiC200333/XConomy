/*
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
package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.info.DefaultConfig;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

    public static boolean isint = false;
    public static DecimalFormat decimalFormat;
    public static BigDecimal maxNumber;
    final static String displayformat = XConomy.Config.DISPLAY_FORMAT;
    final static String pluralname = XConomy.Config.PLURAL_NAME;
    final static String singularname = XConomy.Config.SINGULAR_NAME;

    public static BigDecimal formatString(String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (isint) {
            return bigDecimal.setScale(0, RoundingMode.DOWN);
        } else {
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        }
    }

    public static BigDecimal formatdouble(double am) {
        BigDecimal bigDecimal = BigDecimal.valueOf(am);
        if (isint) {
            return bigDecimal.setScale(0, RoundingMode.DOWN);
        } else {
            return bigDecimal.setScale(2, RoundingMode.DOWN);
        }
    }


    public static String shown(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) == 0) {
            return ChatColor.translateAlternateColorCodes('&', displayformat
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%format_balance%", getformatbalance(am))
                    .replace("%currencyname%", singularname));
        }
        return ChatColor.translateAlternateColorCodes('&', displayformat
                .replace("%balance%", decimalFormat.format(am))
                .replace("%format_balance%", getformatbalance(am))
                .replace("%currencyname%", pluralname));
    }


    public static String shown(double am) {
        return shown(BigDecimal.valueOf(am));
    }

    public static String PEshownf(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) == 0) {
            return ChatColor.translateAlternateColorCodes('&', displayformat
                    .replace("%balance%", getformatbalance(am))
                    .replace("%format_balance%", getformatbalance(am))
                    .replace("%currencyname%", singularname));
        }
        return ChatColor.translateAlternateColorCodes('&', displayformat
                .replace("%balance%", getformatbalance(am))
                .replace("%format_balance%", getformatbalance(am))
                .replace("%currencyname%", pluralname));
    }

    public static boolean isMAX(BigDecimal am) {
        return am.compareTo(maxNumber) > 0;
    }

    public static void load() {
        maxNumber = setmaxnumber();
        isint = XConomy.Config.INTEGER_BAL;
        String gpoint = XConomy.Config.THOUSANDS_SEPARATOR;
        decimalFormat = new DecimalFormat();

        if (!isint) {
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
        }

        if (gpoint != null && !gpoint.equals("") && gpoint.length() == 1) {
            DecimalFormatSymbols spoint = new DecimalFormatSymbols();
            spoint.setGroupingSeparator(gpoint.charAt(0));
            decimalFormat.setDecimalFormatSymbols(spoint);
        }

        XConomy.Config.PAYMENT_TAX = setpaymenttax();
    }


    private static BigDecimal setmaxnumber() {
        String maxn = XConomy.Config.MAX_NUMBER;
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


    private static BigDecimal setpaymenttax() {
        double pt = DefaultConfig.config.getDouble("Settings.payment-tax");
        if (pt < 0.0) {
            pt = 0.0;
        }
        return formatdouble(pt).add(BigDecimal.ONE);
    }

    private static String getformatbalance(BigDecimal bal) {
        if (XConomy.Config.FORMAT_BALANCE != null) {
            if (bal.doubleValue() < XConomy.Config.FORMAT_BALANCE.get(0)) {
                return decimalFormat.format(bal);
            }
            BigDecimal x = BigDecimal.ZERO;
            for (int b : XConomy.Config.FORMAT_BALANCE) {
                if (bal.doubleValue() > b) {
                    x = BigDecimal.valueOf(b);
                } else {
                    break;
                }
            }
            BigDecimal aa = bal.divide(x, 3, RoundingMode.DOWN);
            return DataFormat.decimalFormat.format(aa) + DefaultConfig.config.getString("Currency.format-balance." + x);
        } else {
            return decimalFormat.format(bal);
        }
    }
}
