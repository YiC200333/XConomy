/*
 *  This file (MessageConfig.java) is a part of project XConomy
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


public enum MessageConfig {
    NO_ACCOUNT("no_account"),
    PAY_RECEIVE("pay_receive"),
    DELETE_DATA("delete_data"),
    DELETE_DATA_ADMIN("delete_data_admin"),
    PAYTOGGLE_TRUE("paytoggle_true"),
    PAYTOGGLE_FALSE("paytoggle_false"),
    PAYTOGGLE_OTHER_TRUE("paytoggle_other_true"),
    PAYTOGGLE_OTHER_FALSE("paytoggle_other_false");

    final String value;


    MessageConfig(String value){
        this.value = value;
    }


    @Override
    public String toString() {
        return this.value;
    }
}
