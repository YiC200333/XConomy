package me.yic.xconomy.message;

import org.bukkit.configuration.file.FileConfiguration;

public class Messages {

    public static FileConfiguration langFile;

    public static String systemMessage(String message) {
        String newMessage = message;
        switch (message) {
            case "数据保存方式":
                newMessage = langFile.getString("saving-mode");
                break;
            case "SQLite连接正常":
                newMessage = langFile.getString("sqlite-success");
                break;
            case "SQLite连接异常":
                newMessage = langFile.getString("sqlite-fail");
                break;
            case "自定义文件夹路径不存在":
                newMessage = langFile.getString("no-custom-path");
                break;
            case "MySQL连接正常":
                newMessage = langFile.getString("mysql-success");
                break;
            case "MySQL连接异常":
                newMessage = langFile.getString("mysql-fail");
                break;
            case "MySQL重新连接成功":
                newMessage = langFile.getString("mysql-reconnect-success");
                break;
            case "缓存文件创建异常":
                newMessage = langFile.getString("cache-file-creation-exception");
                break;
            case "升级数据库表格。。。":
                newMessage = langFile.getString("upgrade-database");
                break;
            case "XConomy加载成功":
                newMessage = langFile.getString("enable-success");
                break;
            case "XConomy已成功卸载":
                newMessage = langFile.getString("disable-success");
                break;
            case "已开启BungeeCord同步":
                newMessage = langFile.getString("enable-bungeecord");
                break;
            case "SQLite文件路径设置错误":
                newMessage = langFile.getString("custom-path-error");
                break;
            case "文件夹创建异常":
                newMessage = langFile.getString("create-folder-fail");
                break;
            case "BungeeCord同步未开启":
                newMessage = langFile.getString("not-enable-bungeecord");
                break;
            case "无法连接到数据库-----":
                newMessage = langFile.getString("unable-connect");
                break;
            case "JDBC驱动加载失败":
                newMessage = langFile.getString("jdbc-fail");
                break;
            case "MySQL连接断开失败":
                newMessage = langFile.getString("mysql-disconnect-fail");
                break;
            case "已创建一个新的语言文件":
                newMessage = langFile.getString("create-language-file-success");
                break;
            case "语言文件创建异常":
                newMessage = langFile.getString("create-language-file-fail");
                break;
            case "发现 PlaceholderAPI":
                newMessage = langFile.getString("found-placeholderapi");
                break;
            case "发现 DatabaseDrivers":
                newMessage = langFile.getString("found-databasedrivers");
                break;
            case "已是最新版本":
                newMessage = langFile.getString("is-new-version");
                break;
            case "检查更新失败":
                newMessage = langFile.getString("check-version-fail");
                break;
            case "发现新版本 ":
                newMessage = langFile.getString("found-version");
                break;
            case "§amessage.yml重载成功":
                newMessage = langFile.getString("messege-reload");
                break;
            case "vault-baltop-tips-a":
                newMessage = langFile.getString("vault-baltop-tips-a");
                break;
            case "vault-baltop-tips-b":
                newMessage = langFile.getString("vault-baltop-tips-b");
                break;
            case " 名称已更改!":
                newMessage = langFile.getString("username-modified");
                break;
            case "§cBC模式开启的情况下,无法在无人的服务器中使用OP命令":
                newMessage = langFile.getString("no-player-tips");
                break;
            case "§c该指令不支持在半正版模式中使用":
                newMessage = langFile.getString("semi-mode-ban-commands");
                break;
            case "§6控制台无法使用该指令":
                newMessage = langFile.getString("console-ban-commands");
                break;
        }
        return newMessage;
    }


}
