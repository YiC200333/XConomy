package me.yic.xconomy.lang;

public class Messages {

    public static String gettag(String message) {
        String tag = message;
        switch (message) {
            case "数据保存方式":
                tag = "saving-mode";
                break;
            case "SQLite连接正常":
                tag = "sqlite-success";
                break;
            case "SQLite连接异常":
                tag = "sqlite-fail";
                break;
            case "自定义文件夹路径不存在":
                tag = "no-custom-path";
                break;
            case "MySQL连接正常":
                tag = "mysql-success";
                break;
            case "MySQL连接异常":
                tag = "mysql-fail";
                break;
            case "MySQL重新连接成功":
                tag = "mysql-reconnect-success";
                break;
            case "缓存文件创建异常":
                tag = "cache-file-creation-exception";
                break;
            case "升级数据库表格。。。":
                tag = "upgrade-database";
                break;
            case "XConomy加载成功":
                tag = "enable-success";
                break;
            case "XConomy已成功卸载":
                tag = "disable-success";
                break;
            case "已开启BungeeCord同步":
                tag = "enable-bungeecord";
                break;
            case "SQLite文件路径设置错误":
                tag = "custom-path-error";
                break;
            case "文件夹创建异常":
                tag = "create-folder-fail";
                break;
            case "BungeeCord同步未开启":
                tag = "not-enable-bungeecord";
                break;
            case "无法连接到数据库-----":
                tag = "unable-connect";
                break;
            case "JDBC驱动加载失败":
                tag = "jdbc-fail";
                break;
            case "MySQL连接断开失败":
                tag = "mysql-disconnect-fail";
                break;
            case "已创建一个新的语言文件":
                tag = "create-language-file-success";
                break;
            case "语言文件创建异常":
                tag = "create-language-file-fail";
                break;
            case "发现 PlaceholderAPI":
                tag = "found-placeholderapi";
                break;
            case "发现 DatabaseDrivers":
                tag = "found-databasedrivers";
                break;
            case "已是最新版本":
                tag = "is-new-version";
                break;
            case "检查更新失败":
                tag = "check-version-fail";
                break;
            case "发现新版本 ":
                tag = "found-version";
                break;
            case "§amessage.yml重载成功":
                tag = "messege-reload";
                break;
            case "vault-baltop-tips-a":
                tag = "vault-baltop-tips-a";
                break;
            case "vault-baltop-tips-b":
                tag = "vault-baltop-tips-b";
                break;
            case " 名称已更改!":
                tag = "username-modified";
                break;
            case "§cBC模式开启的情况下,无法在无人的服务器中使用OP命令":
                tag = "no-player-tips";
                break;
            case "§c该指令不支持在半正版模式中使用":
                tag = "semi-mode-ban-commands";
                break;
            case "§6控制台无法使用该指令":
                tag = "console-ban-commands";
                break;
        }
        return tag;
    }


}
