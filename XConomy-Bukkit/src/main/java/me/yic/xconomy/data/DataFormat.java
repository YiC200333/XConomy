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
import me.yic.xconomy.info.ServerINFO;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

    public static boolean isint = false;
    public static DecimalFormat decimalFormat;
    public static BigDecimal maxNumber;
    final static String displayformat = XConomy.config.getString("Currency.display-format");
    final static String pluralname = XConomy.config.getString("Currency.plural-name");
    final static String singularname = XConomy.config.getString("Currency.singular-name");

    public static BigDecimal formatString(String am) {
        BigDecimal bigDecimal = new BigDecimal(am);
        if (isint) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public static BigDecimal formatdouble(double am) {
        BigDecimal bigDecimal = BigDecimal.valueOf(am);
        if (isint) {
            return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String shown(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) > 0) {
            return ChatColor.translateAlternateColorCodes('&', displayformat
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", pluralname));
        }
        return ChatColor.translateAlternateColorCodes('&', displayformat
                .replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", singularname));
    }

    @SuppressWarnings("ConstantConditions")
    public static String shownd(double am) {
        if (am > 1) {
            return ChatColor.translateAlternateColorCodes('&', displayformat
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", pluralname));
        }
        return ChatColor.translateAlternateColorCodes('&', displayformat
                .replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", singularname));
    }

    public static boolean isMAX(BigDecimal am) {
        return am.compareTo(maxNumber) > 0;
    }

    public static void load() {
        maxNumber = setmaxnumber();
        isint = XConomy.config.getBoolean("Currency.integer-bal");
        String gpoint = XConomy.config.getString("Currency.thousands-separator");
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

        ServerINFO.PaymentTax = setpaymenttax();
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


    private static BigDecimal setpaymenttax() {
        double pt = XConomy.config.getDouble("Settings.payment-tax");
        if (pt < 0.0){
            pt = 0.0;
        }
        return formatdouble(pt).add(BigDecimal.ONE);
    }
}
