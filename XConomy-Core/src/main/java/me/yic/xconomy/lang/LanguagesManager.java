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

import me.yic.xconomy.adapter.comp.CConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LanguagesManager {
    public static CConfig messageFile;

    public static void compare(String lang, File f) {
        messageFile = new CConfig("/lang", "/" + lang.toLowerCase() + ".yml");

        List<String> messages = index();
        for (String message : messages) {
            boolean renew = false;
            if (!MessagesManager.messageFile.contains(message)) {
                renew = true;
                MessagesManager.messageFile.createSection(message);
                MessagesManager.messageFile.set(message, messageFile.getString(message));
            }

            try {
                if (renew) {
                    MessagesManager.messageFile.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> index() {
        List<String> ll = new ArrayList<>();
        ll.add("prefix");
        ll.add("balance");
        ll.add("balance_other");
        ll.add("top_title");
        ll.add("sum_text");
        ll.add("top_text");
        ll.add("top_subtitle");
        ll.add("top_nodata");
        ll.add("top_out");
        ll.add("top_hidden");
        ll.add("top_displayed");
        ll.add("pay");
        ll.add("pay_receive");
        ll.add("pay_fail");
        ll.add("pay_self");
        ll.add("paytoggle_true");
        ll.add("paytoggle_false");
        ll.add("paytoggle_other_true");
        ll.add("paytoggle_other_false");
        ll.add("no_account");
        ll.add("invalid_amount");
        ll.add("over_maxnumber");
        ll.add("over_maxnumber_receive");
        ll.add("money_give");
        ll.add("money_give_receive");
        ll.add("money_take");
        ll.add("money_take_fail");
        ll.add("money_take_receive");
        ll.add("money_set");
        ll.add("money_set_receive");
        ll.add("no_receive_permission");
        ll.add("no_permission");
        ll.add("no_data");
        ll.add("delete_data");
        ll.add("delete_data_admin");
        ll.add("global_permissions_change");
        ll.add("personal_permissions_change");
        ll.add("help_title_full");
        ll.add("help1");
        ll.add("help2");
        ll.add("help3");
        ll.add("help4");
        ll.add("help5");
        ll.add("help6");
        ll.add("help7");
        ll.add("help8");
        ll.add("help9");
        ll.add("help10");
        ll.add("help11");
        ll.add("help12");
        ll.add("help13");
        ll.add("help14");
        return ll;

    }

    public static void translateFile(String string, File file) {
        try {
            FileOutputStream f = new FileOutputStream(file, true);
            f.write(string.getBytes());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void translatorName(File file) {
        String ta = messageFile.getString("translation_authors");
        if (!ta.equalsIgnoreCase("") && !ta.equalsIgnoreCase("none")) {
            translateFile("#========== Translation_authors - " + ta + " ==========", file);
        }
    }
}
