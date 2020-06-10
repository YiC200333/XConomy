package me.Yi.XConomy.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import me.Yi.XConomy.XConomy;

public class Messages {

	public static HashMap<String, String> mess = new HashMap<String, String>();

	public static String sysmess(String xx) {
		String ss = xx;
		String lang = XConomy.getInstance().lang();
		if (xx.equals("数据保存方式 - YML")) {
			if (lang.equalsIgnoreCase("English")) {
				ss = "Saving method - YML";
			}
			if (lang.equalsIgnoreCase("French")) {
				ss = "Méthode de sauvegarde - YML";
			}
			if (lang.equalsIgnoreCase("Spanish")) {
				ss = "Metodo de Guardado - YML";
			}
			if (lang.equalsIgnoreCase("Russian")) {
				ss = "Метод сохранения - YML";
			}
			if (lang.equalsIgnoreCase("ChineseTW")) {
				ss = "數據保存方式- YML";
			}
			if (lang.equalsIgnoreCase("Turkish")) {
				ss = "Method Kaydediliyor - YML";
			}
		} else if (xx.equals("数据文件创建完成")) {
			if (lang.equalsIgnoreCase("English")) {
				ss = "Data file created";
			}
			if (lang.equalsIgnoreCase("French")) {
				ss = "Fichier de données créé";
			}
			if (lang.equalsIgnoreCase("Spanish")) {
				ss = "Archivo de datos creado";
			}
			if (lang.equalsIgnoreCase("Russian")) {
				ss = "Файл данных создан";
			}
			if (lang.equalsIgnoreCase("ChineseTW")) {
				ss = "數據文件創建完成";
			}
			if (lang.equalsIgnoreCase("Turkish")) {
				ss = "Veri dosyası oluşturuldu";
			}
		} else if (xx.equals("数据文件创建异常")) {
			if (lang.equalsIgnoreCase("English")) {
				ss = "Exception in creating data file";
			}
			if (lang.equalsIgnoreCase("French")) {
				ss = "Exception à la création d'un fichier de données";
			}
			if (lang.equalsIgnoreCase("Spanish")) {
				ss = "Excepción al crear archivo de datos";
			}
			if (lang.equalsIgnoreCase("Russian")) {
				ss = "Исключение при создании файла данных";
			}
			if (lang.equalsIgnoreCase("ChineseTW")) {
				ss = "數據文件創建異常";
			}
			if (lang.equalsIgnoreCase("Turkish")) {
				ss = "Veri dosyası oluşturulurken hata oluştu";
			}
		} else if (xx.equals("找到数据文件")) {
			if (lang.equalsIgnoreCase("English")) {
				ss = "Data file found";
			}
			if (lang.equalsIgnoreCase("French")) {
				ss = "Fichier de données trouvé";
			}
			if (lang.equalsIgnoreCase("Spanish")) {
				ss = "Archivo de Datos Encontrado";
			}
			if (lang.equalsIgnoreCase("Russian")) {
				ss = "Файл данных найден";
			}
			if (lang.equalsIgnoreCase("ChineseTW")) {
				ss = "找到數據文件";
			}
			if (lang.equalsIgnoreCase("Turkish")) {
				ss = "Veri dosyası bulundu";
			}
		} else if (xx.equals("数据保存方式 - MySQL")) {
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
		} else if (xx.equals("MySQL连接正常")) {
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
		} else if (xx.equals("MySQL连接异常")) {
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
		} else if (xx.equals("MySQL重新连接成功")) {
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
		} else if (xx.equals("XConomy加载成功")) {
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
		} else if (xx.equals("XConomy已成功卸载")) {
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
		} else if (xx.equals("已开启BC同步")) {
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
		} else if (xx.equals("BC同步只支持MySQL")) {
			if (lang.equalsIgnoreCase("English")) {
				ss = "BungeeCord mode only supports MySQL";
			}
			if (lang.equalsIgnoreCase("French")) {
				ss = "Le mode BungeeCord ne supporte que MySQL";
			}
			if (lang.equalsIgnoreCase("Spanish")) {
				ss = "Modo BungeeCord solo soporta MySQL";
			}
			if (lang.equalsIgnoreCase("Russian")) {
				ss = "Режим BungeeCord поддерживает только MySQL";
			}
			if (lang.equalsIgnoreCase("ChineseTW")) {
				ss = "BC同步只支持MySQL";
			}
			if (lang.equalsIgnoreCase("Turkish")) {
				ss = "BungeeCord modu sadece MySQL destekler";
			}
		} else if (xx.equals("BC同步未开启")) {
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
		} else if (xx.equals("无法连接到数据库-----")) {
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
		} else if (xx.equals("JDBC驱动加载失败")) {
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
		} else if (xx.equals("MySQL连接断开失败")) {
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
		} else if (xx.equals("已创建一个新的语言文件")) {
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
		} else if (xx.equals("语言文件创建异常")) {
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
		} else if (xx.equals("发现 PlaceholderAPI")) {
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
		} else if (xx.equals("已是最新版本")) {
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
		} else if (xx.equals("检查更新失败")) {
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
		} else if (xx.equals("§amessage.yml重载成功")) {
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
			// -----------------------------------------------------------------------------------------
		} else if (xx.equals(" 名称已更改!")) {
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
		} else if (xx.equals("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令")) {
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
		} else if (xx.equals("§6控制台无法使用该指令")) {
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
			// -----------------------------------------------------------------------------------------
		} else if (xx.equals("&a&l数据缓存已保存 &7>>> ")) {
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
		} else if (xx.equals("&a&l 条")) {
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
