/*
 *  This file (PermissionINFO.java) is a part of project XConomy
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
package me.yic.xconomy.info;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionINFO {
    public static boolean globalpayment = true;

    private static final Map<UUID, Boolean> payment = new HashMap<>();

    public static boolean getGlobalPayment() {
        return globalpayment;
    }

    public static Boolean getPaymentPermission(UUID u) {
        return payment.getOrDefault(u, null);
    }

    public static void setPaymentPermission(UUID u, Boolean b) {
        if (b == null){
            payment.remove(u);
        }else {
            payment.put(u, b);
        }
    }
}