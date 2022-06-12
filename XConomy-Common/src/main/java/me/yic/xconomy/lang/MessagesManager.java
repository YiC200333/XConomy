/*
 *  This file (MessagesManager.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.comp.CConfig;

import java.io.*;

public class MessagesManager {
    private static final XConomy plugin = XConomy.getInstance();
    public static CConfig messageFile;
    public static CConfig langFile;
    public static Messages smessageList = new Messages();


    public static void loadsysmess() {
        langFile = new CConfig("!/lang/" + XConomy.Config.LANGUAGE.toLowerCase() + ".yml");
    }


    public static void loadlangmess() {
        File mfile = new File(XConomy.getInstance().getDataFolder(), "message.yml");
        boolean translate = false;
        if (!mfile.exists()) {
            try {
                if (!mfile.createNewFile()) {
                    plugin.logger("create-language-file-fail", 1, null);
                }
                translate = true;
                plugin.logger("create-language-file-success", 0, null);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.logger("create-language-file-fail", 1, null);
            }
        }

        messageFile = new CConfig(mfile);
        LanguagesManager.compare(XConomy.Config.LANGUAGE, mfile);
        if (translate) {
            Languages.translatorName(XConomy.Config.LANGUAGE, mfile);
        }
    }

    public static String systemMessage(String message) {
        return langFile.getString(smessageList.gettag(message));
    }

    public static String getAuthor() {
        if (XConomy.Config.LANGUAGE.equalsIgnoreCase("Chinese")
                | XConomy.Config.LANGUAGE.equalsIgnoreCase("ChineseTW")) {
            return "伊C";
        } else {
            return "YiC";
        }
    }

    public static String getTranslatorS() {
        String trm = langFile.getString("translation-author");
        if (!trm.equalsIgnoreCase("None")) {
            return trm;
        } else {
            return null;
        }
    }
}
