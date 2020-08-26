package me.Yi.XConomy.Message;

import java.io.File;
import java.io.FileOutputStream;
import me.Yi.XConomy.XConomy;

public class Messages {

	public static String sysmess(String xx) {
		String ss = xx;
		String lang = XConomy.getInstance().lang();
		switch (xx) {
			case "数据保存方式 - SQLite":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Saving method - SQLite";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Méthode de sauvegarde - SQLite";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Metodo de Guardado - SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Метод сохранения - SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "數據保存方式- SQLite";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Method Kaydediliyor - SQLite";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "データを保存の方法 - SQLite";
				}
				break;
			case "SQLite连接正常":
				if (lang.equalsIgnoreCase("English")) {
					ss = "SQLite successfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "SQLite connecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Conectado con SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Успешное подключение к SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "SQLite連接正常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "SQLite bağlantısı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "SQLite連結正常";
				}
				break;
			case "SQLite连接异常":
				if (lang.equalsIgnoreCase("English")) {
					ss = "SQLite unsuccessfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Connexion infructueuse à SQLite";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Fallo al conectar con SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Не успешное подключение к SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "SQLite連接異常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "SQLite tekrar bağlantı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "SQLite連結エラー";
				}
				break;
			case "自定义文件夹路径不存在":
				if (lang.equalsIgnoreCase("English")) {
					ss = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "自定義文件夾路徑不存在";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "選択したのフォルダは不存在です";
				}
                break;
//		    case "数据文件创建异常":
//			if (lang.equalsIgnoreCase("English")) {
//				ss = "Exception in creating data file";
//			}
//			if (lang.equalsIgnoreCase("French")) {
//				ss = "Exception à la création d'un fichier de données";
//			}
//			if (lang.equalsIgnoreCase("Spanish")) {
//				ss = "Excepción al crear archivo de datos";
//			}
//			if (lang.equalsIgnoreCase("Russian")) {
//				ss = "Исключение при создании файла данных";
//			}
//			if (lang.equalsIgnoreCase("ChineseTW")) {
//				ss = "數據文件創建異常";
//			}
//			if (lang.equalsIgnoreCase("Turkish")) {
//				ss = "Veri dosyası oluşturulurken hata oluştu";
//			}
//			if (lang.equalsIgnoreCase("Japanese")) {
//				ss = "データファイル作成異常";
//			}
//				break;
//		    case "找到数据文件":
//			if (lang.equalsIgnoreCase("English")) {
//				ss = "Data file found";
//			}
//			if (lang.equalsIgnoreCase("French")) {
//				ss = "Fichier de données trouvé";
//			}
//			if (lang.equalsIgnoreCase("Spanish")) {
//				ss = "Archivo de Datos Encontrado";
//			}
//			if (lang.equalsIgnoreCase("Russian")) {
//				ss = "Файл данных найден";
//			}
//			if (lang.equalsIgnoreCase("ChineseTW")) {
//				ss = "找到數據文件";
//			}
//			if (lang.equalsIgnoreCase("Turkish")) {
//				ss = "Veri dosyası bulundu";
//			}
//			if (lang.equalsIgnoreCase("Japanese")) {
//				ss = "データファイルが発見されました";
//			}
//				break;
			case "数据保存方式 - MySQL":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Saving method - MySQL";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Méthode de sauvegarde - MySQL";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Metodo de Guardado - MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Метод сохранения - MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "數據保存方式- MySQL";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Method Kaydediliyor - MySQL";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "データを保存の方法 - MySQL";
				}
				break;
			case "MySQL连接正常":
				if (lang.equalsIgnoreCase("English")) {
					ss = "MySQL successfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "MySQL connecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Conectado con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Успешное подключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "MySQL連接正常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "MySQL bağlantısı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "MySQL連結正常";
				}
				break;
			case "MySQL连接异常":
				if (lang.equalsIgnoreCase("English")) {
					ss = "MySQL unsuccessfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Connexion infructueuse à MySQL";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Fallo al conectar con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Не успешное подключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "MySQL連接異常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "MySQL tekrar bağlantı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "MySQL連結異常";
				}
				break;
			case "MySQL重新连接成功":
				if (lang.equalsIgnoreCase("English")) {
					ss = "MySQL successfully reconnected";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "MySQL reconnecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Reconectado con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Успешное переподключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "MySQL重新連接成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "MySQL bağlantısı başarısız";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "MySQL再連結成功";
				}
				break;
			case "XConomy加载成功":
				if (lang.equalsIgnoreCase("English")) {
					ss = "XConomy successfully enabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "XConomy activé avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "XConomy Habilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "XConomy успешно включен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "XConomy加載成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "XConomy aktif edildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "XConomyロード完了";
				}
				break;
			case "XConomy已成功卸载":
				if (lang.equalsIgnoreCase("English")) {
					ss = "XConomy successfully disabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "XConomy désactivé avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "XConomy Deshabilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "XConomy успешно выключен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "XConomy已成功卸載";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "XConomy devredışı bırakıldı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "XConomyアンインストール成功";
				}
				break;
			case "已开启BC同步":
				if (lang.equalsIgnoreCase("English")) {
					ss = "BungeeCord mode enabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Mode BungeeCord activé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Modo BungeeCord Habilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Режим BungeeCord включен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "已開啟BC同步";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "BungeeCord modu aktif edildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "BungeeCord mode enabled";
				}
				break;
			case "SQLite文件路径设置错误":
				if (lang.equalsIgnoreCase("English")) {
					ss = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "SQLite文件路徑設置錯誤";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "SQLiteファイル位置(Path)設置エラー";
				}
				break;
			case "BC同步未开启":
				if (lang.equalsIgnoreCase("English")) {
					ss = "BungeeCord mode disabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Mode BungeeCord désactivé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Modo BungeeCord Deshabilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Режим BungeeCord выключен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "BC同步未開啓";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "BungeeCord modu kapatıldı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "BungeeCord mode disabled";
				}
				break;
			case "无法连接到数据库-----":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Unable to connect to database -----";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Impossible de se connecter à la base de données -----";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Incapaz de conectar a la Base de Datos -----";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Невозможно подключиться к базе данных -----";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "無法連接到數據庫-----";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Veritabanına bağlanılamıyor -----";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "データベースが連結できませんでした -----";
				}
				break;
			case "JDBC驱动加载失败":
				if (lang.equalsIgnoreCase("English")) {
					ss = "JDBC driver load failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Le chargement du pilote JDBC a échoué";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Controlador JDBC Fallo al Cargar";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Не удалось загрузить драйвер JDBC";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "JBDC驅動加載失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "JDBC sürücüsü yüklenemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "JDBCドライブロードエラー";
				}
				break;
			case "MySQL连接断开失败":
				if (lang.equalsIgnoreCase("English")) {
					ss = "MySQL disconnect failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Logging out of MySQL failed";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "La desconexion con MySQL fallo";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Не удалось отключиться от MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "MySQL連接斷開失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "MySQL bağlantısı kesilemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "MySQL切断エラー";
				}
				break;
			case "已创建一个新的语言文件":
				if (lang.equalsIgnoreCase("English")) {
					ss = "The new language file has been created";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Le nouveau fichier linguistique a été créé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "El nuevo Archivo de Lenguaje fue creado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Новый языковой файл был создан";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "已創建一個新的語言檔案";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Yeni dil dosyası oluşturuldu";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "新しいの言語ファイルが作成できました";
				}
				break;
			case "语言文件创建异常":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Exception in creating language file";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Exception à la création d'un fichier linguistique";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Excepcion al crear archivo de lenguaje";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Исключение при создании языкового файла";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "語言檔案創建异常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Dil dosyası oluşturulurken hata oluştu";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "新しいの言語ファイルが作成エラー";
				}
				break;
			case "发现 PlaceholderAPI":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "發現 PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "PlaceholderAPIが発見された";
				}
				break;
			case "已是最新版本":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Is the latest version";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Est-ce la dernière version";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Es la version mas nueva";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Последняя версия";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "已是最新版本";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "En son sürüm";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "既に最新バージョンです";
				}
				break;
			case "检查更新失败":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Check update failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Échec de la mise à jour des contrôles";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Fallo al buscar actualizaciones";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Проверить обновление не удалось";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "檢查更新失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Güncelleme kontrol edilemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "アップデート検査が失敗しました";
				}
				break;
			case "§amessage.yml重载成功":
				if (lang.equalsIgnoreCase("English")) {
					ss = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "§amessage.yml重載成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "§amessage.ymlリロード";
				}
				break;
			case "XConomy 的连接池 不支持 Vault 变量的 baltop 功能":
				if (lang.equalsIgnoreCase("English")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "XConomy 的連接池不支援 Vault 變數的 baltop 功能";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "XConomy does not support the baltop function of vault papi with connnection pool";
				}
				break;
			case "请在 PlaceholderAPI 的 config.yml 中设置 expansions.vault.baltop.enabled 为 false":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "請在 PlaceholderAPI 的 config.yml 中設定 expansions.vault.baltop.enabled 為 false ";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "Please set 'expansions.vault.baltop.enabled' to false in the config.yml of PlaceholderAPI";
				}
				break;
			case "或者在 XConomy 的 config.yml 中设置 Pool-Settings.usepool 为 false":
				if (lang.equalsIgnoreCase("English")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "或者在 XConomy 的 config.yml 中設置 Pool-Settings.usepool 為 false";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "Or set 'Pool-Settings.usepool' to false in the config.yml of XConomy";
				}
				// -----------------------------------------------------------------------------------------
				break;
			case " 名称已更改!":
				if (lang.equalsIgnoreCase("English")) {
					ss = " username has been changed";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = " le nom d'utilisateur a été modifié";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = " El Nombre de usuario fue cambiado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = " имя пользователя было изменено";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = " 名稱已更改！";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = " kullanıcı adı değiştirildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = " username 変更成功";
				}
				break;
			case "§cBC模式开启的情况下,无法在无人的服务器中使用OP命令":
				if (lang.equalsIgnoreCase("English")) {
					ss = "§cWhen the BC mode is enabled, the op command cannot be used in the server without player";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "§cLorsque le mode BC est activé, la commande op ne peut pas être utilisée dans le serveur sans joueur";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "§cCuando el modo BC esta habilitado, el comando op no puede ser utilizado en el servidor sin jugadores";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "§cКогда режим BC включен, команда op не может использоваться на сервере без игрока";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "§cBC模式開啟的情况下，無法在無人的服務器中使用OP命令";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "§cBC modu etkinleştirildiğinde, op komutları oyuncu sunucuda değilse kullanılamaz";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "§cBCモードONの状態で，人がないの時 OP命令が使用できません";
				}
				break;
			case "§6控制台无法使用该指令":
				if (lang.equalsIgnoreCase("English")) {
					ss = "§6This command cannot be used by the console";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "§6Cette commande ne peut pas être utilisée par la console";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "§6Este comando no puede ser utilizado por la Consola";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "§6Эта команда не может быть использована консолью";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "§6控制台無法使用該指令";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "§6Bu komut sadece konsoldan uygulanabilir";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "§6コンソールがこの命令を使えできません";
				}
				// -----------------------------------------------------------------------------------------
				break;
			case "&a&l数据缓存已保存 &7>>> ":
				if (lang.equalsIgnoreCase("English")) {
					ss = "&a&lData cache saved &7>>> ";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "&a&lCache de données sauvegardé &7>>>> ";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "&a&lCache de datos guardado &7>>> ";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "&a&lКэш данных сохранен &7>>> ";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "&a&l數據緩存已保存 &7>>> ";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "&a&lVeri önbelleği kaydedildi &7>>> ";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "&a&lファイルキャッシュが保存成功 &7>>>";
				}
				break;
			case "&a&l 条":
				if (lang.equalsIgnoreCase("English")) {
					ss = "&a&l players";
				}
				if (lang.equalsIgnoreCase("French")) {
					ss = "&a&l joueurs de";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					ss = "&a&l jugadores";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					ss = "&a&l игроков";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					ss = "&a&l 條";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					ss = "&a&l oyuncuları";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					ss = "&a&l 列";
				}
				break;
		}
		return ss;
	}

	public static void tranname(String lang, File xx) {
		if (lang.equalsIgnoreCase("French")) {
			tranp("#============================== Translator - Xx_Fluoxe_xX ==============================", xx);
		} else if (lang.equalsIgnoreCase("Spanish")) {
			tranp("#============================== Translator - gabyfig ==============================", xx);
		} else if (lang.equalsIgnoreCase("Russian")) {
			tranp("#============================== Translator - Trimitor ==============================", xx);
		} else if (lang.equalsIgnoreCase("Turkish")) {
			tranp("#============================== Translator - erkutay007 ==============================", xx);
		}else if (lang.equalsIgnoreCase("Japanese")) {
			tranp("#============================== Translator - シロカミ ==============================", xx);
		}
	}
	
	public static String getau() {
		if (XConomy.getInstance().lang().equalsIgnoreCase("Chinese")
				| XConomy.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
			return "伊C";
		} else{
			return "YiC";
		}
	}

	public static void tranp(String mm, File xx) {
		try {
			FileOutputStream f = new FileOutputStream(xx, true);
			f.write(mm.getBytes());
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
