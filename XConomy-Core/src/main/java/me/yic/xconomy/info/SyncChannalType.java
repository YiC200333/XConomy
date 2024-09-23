/*
 *  This file (SyncChannalType.java) is a part of project XConomy
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


public enum SyncChannalType {

    OFF("Off"),
    BUNGEECORD("BungeeCord"),
    REDIS("Redis");

    final String value;


    SyncChannalType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return this.value;
    }

    public boolean equals(SyncChannalType Other) {
        return this.value.equals(Other.value);
    }
}
