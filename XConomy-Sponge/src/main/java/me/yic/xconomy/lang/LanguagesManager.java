package me.yic.xconomy.lang;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LanguagesManager extends Languages {

    public static void compare(String lang) {
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
        } else {
            chinese(mess);
        }

        List<String> messages = index();
        for (String message : messages) {
            boolean renew = false;
            if (MessagesManager.messageFile.getNode(message).isEmpty()) {
                renew = true;
                MessagesManager.messageFile.getNode(message).appendListNode();
                MessagesManager.messageFile.getNode(message).setValue(mess.get(message));
            }

            try {
                if (renew) {
                    MessagesManager.loader.save(MessagesManager.messageFile);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void translatorName(String lang, File file) {
        translatorName(lang, file);
    }
}
