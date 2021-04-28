/*
 *  This file (LanguagesManager.java) is a part of project XConomy
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
package me.yic.xconomy.lang;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LanguagesManager extends Languages {

    public static void compare(String lang, File f) {
        HashMap<String, String> mess = new HashMap<>();
        if (lang.equalsIgnoreCase("English")) {
            english(mess);
        } else if (lang.equalsIgnoreCase("ChineseTW")) {
            chinesetw(mess);
        } else if (lang.equalsIgnoreCase("French")) {
            french(mess);
        } else if (lang.equalsIgnoreCase("Spanish")) {
            spanish(mess);
        } else if (lang.equalsIgnoreCase("Russian")) {
            russian(mess);
        } else if (lang.equalsIgnoreCase("Turkish")) {
            turkish(mess);
        } else if (lang.equalsIgnoreCase("Japanese")) {
            japanese(mess);
        } else if (lang.equalsIgnoreCase("German")) {
            german(mess);
        } else if (lang.equalsIgnoreCase("Indonesia")) {
            indonesia(mess);
        } else {
            chinese(mess);
        }

        List<String> messages = index();
        for (String message : messages) {
            boolean renew = false;
            if (!MessagesManager.messageFile.contains(message)) {
                renew = true;
                MessagesManager.messageFile.createSection(message);
                MessagesManager.messageFile.set(message, mess.get(message));
            }

            try {
                if (renew) {
                    MessagesManager.messageFile.save(f);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
