/*
 *  This file (RecordInfo.java) is a part of project XConomy
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

public class RecordInfo {
    private final String type;

    private final String command;

    private final String comment;

    public RecordInfo(String type, String command, StringBuilder comment) {
        if (command == null) {
            this.type = type;
            this.command = "N/A";
            this.comment = "N/A";
        }else{
            if (comment == null) {
                this.type = type;
                this.command = command;
                this.comment = "N/A";
            }else{
                this.type = type;
                this.command = command;
                this.comment = comment.toString();
            }
        }
    }

    public String getType() {
        return type;
    }

    public String getCommand() {
        return command;
    }

    public String getComment() {
        return comment;
    }
}