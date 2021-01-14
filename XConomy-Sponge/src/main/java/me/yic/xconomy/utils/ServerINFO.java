package me.yic.xconomy.utils;

public class ServerINFO {

    public static String ServerType;

    public static Boolean IsBungeeCordMode = false;

    public static Boolean IsSemiOnlineMode = false;

    public static String Lang;

    public static Boolean EnableConnectionPool = false;

    public static String Sign;

    public static Boolean DDrivers = false;

    public static Boolean ConnectionMode(){
        if (ServerType.equalsIgnoreCase("Sponge") && !DDrivers){
            return false;
        }
        return true;
    };
}