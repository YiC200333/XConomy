package me.Yi.XConomy.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Langs {

	public static HashMap<String, String> mess = new HashMap<String, String>();

	public static void compare(String lang, File f) {
		if (lang.equalsIgnoreCase("English")) {
			Langs.english();
		} else if (lang.equalsIgnoreCase("ChineseTW")) {
			chinesetw();
		} else if (lang.equalsIgnoreCase("French")) {
			french();
		} else if (lang.equalsIgnoreCase("Spanish")) {
			spanish();
		} else if (lang.equalsIgnoreCase("Russian")) {
			russian();
		} else if (lang.equalsIgnoreCase("Turkish")) {
			turkish();
		} else {
			chinese();
		}
		List<String> messs = index();
		for (String mm : messs) {
			boolean md = false;
			if (!MessManage.mess.contains(mm)) {
				md = true;
				MessManage.mess.createSection(mm);
				MessManage.mess.set(mm, mess.get(mm));
			}

			try {
				if (md) {
					MessManage.mess.save(f);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static List<String> index() {
		List<String> ll = new ArrayList<String>();
		ll.add("prefix");
		ll.add("balance");
		ll.add("balance_other");
		ll.add("top_title");
		ll.add("top_text");
		ll.add("top_nodata");
		ll.add("pay");
		ll.add("pay_receive");
		ll.add("pay_fail");
		ll.add("pay_self");
		ll.add("noaccount");
		ll.add("invalid");
		ll.add("money_give");
		ll.add("money_give_receive");
		ll.add("money_take");
		ll.add("money_take_fail");
		ll.add("money_take_receive");
		ll.add("money_set");
		ll.add("money_set_receive");
		ll.add("no_permission");
		ll.add("help_title_full");
		ll.add("help1");
		ll.add("help2");
		ll.add("help3");
		ll.add("help4");
		ll.add("help5");
		ll.add("help6");
		ll.add("help7");
		ll.add("reload");
		return ll;

	}

	private static void english() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&aBalance: %balance%");
		mess.put("balance_other", "&a%player%'s balance: %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&cNo TOP10 data");
		mess.put("pay", "&cYou pay %player% %amount%");
		mess.put("pay_receive", "&aYou receive %amount% from %player%");
		mess.put("pay_fail", "&cYour balance is less than %amount%");
		mess.put("pay_self", "&cYou can't pay yourself");
		mess.put("noaccount", "&cTarget account does not exist");
		mess.put("invalid", "&cInvalid amount");
		mess.put("money_give", "&cYou give %player% %amount%");
		mess.put("money_give_receive", "&aAdministrator give you %amount%");
		mess.put("money_take", "&cYou take %amount% from %player%");
		mess.put("money_take_fail", "&c%player%'s balance is less than %amount%");
		mess.put("money_take_receive", "&cAdministrator take %amount% from your account");
		mess.put("money_set", "&cYou set %player%'s balance to %amount%");
		mess.put("money_set_receive", "&cAdministrator set your balance to %amount%");
		mess.put("no_permission", "&cYou don't have permission to use this command");
		// mess.put("help_title", "&6HELP");
		mess.put("help_title_full", "&6=============== [XConomy] HELP ===============");
		mess.put("help1", "&6balance/money  -  Displays your balance");
		mess.put("help2", "&6balance/money <player>  -  Displays <player>'s balance");
		mess.put("help3", "&6pay <player> <amount>  -  Pay <player> <amount>");
		mess.put("help4", "&6balancetop  -  Displays TOP10");
		mess.put("help5", "&6balance/money give <player> <amount>  -  give <player> <amount>");
		mess.put("help6", "&6balance/money take <player> <amount>  -  take <amount> from <player>");
		mess.put("help7", "&6balance/money set <player> <amount>  -  set <player>'s balance to <amount>");
		mess.put("help7", "&6balance/money set <player> <amount>  -  set <player>'s balance to <amount>");
	}

	private static void russian() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&aБаланс: %balance%");
		mess.put("balance_other", "&aБаланс игрока %player%: %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&cНет данных о ТОП10");
		mess.put("pay", "&cВы заплатили игроку %player% %amount%");
		mess.put("pay_receive", "&aВы получили %amount% от игрока %player%");
		mess.put("pay_fail", "&cВаш баланс меньше, чем %amount%");
		mess.put("pay_self", "&cВы не можете заплатить себе же");
		mess.put("noaccount", "&cУказанный аккаунт не существует");
		mess.put("invalid", "&cНекорректная сумма");
		mess.put("money_give", "&cВы выдали игроку %player% %amount%");
		mess.put("money_give_receive", "&aАдминистрация выдала вам %amount%");
		mess.put("money_take", "&cВы забрали %amount% у игрока %player%");
		mess.put("money_take_fail", "&cБаланс игрока %player% меньше, чем %amount%");
		mess.put("money_take_receive", "&cАдминистрация забрала %amount% с вашего аккаунта");
		mess.put("money_set", "&cВы установили баланс игрока %player% на %amount%");
		mess.put("money_set_receive", "&cАдминистрация установила ваш баланс на %amount%");
		mess.put("no_permission", "&cУ вас нет прав для использования этой команды");
		// mess.put("help_title", "&6ПОМОЩЬ");
		mess.put("help_title_full", "&6=============== [XConomy] ПОМОЩЬ ===============");
		mess.put("help1", "&6balance/money  -  Отображает ваш баланс");
		mess.put("help2", "&6balance/money <игрок>  -  Отображает баланс <игрока>");
		mess.put("help3", "&6pay <игрок> <сумма>  -  Заплатить <игроку> <сумму>");
		mess.put("help4", "&6balancetop  -  Отображает TOP10");
		mess.put("help5", "&6balance/money give <игрок> <сумма>  -  Дать <игроку> <сумму>");
		mess.put("help6", "&6balance/money take <игрок> <сумма>  -  Взять <сумму> у <игрока>");
		mess.put("help7", "&6balance/money set <игрок> <сумма>  -  Установить баланс <игрока> на <сумму>");
	}

	private static void spanish() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&aDinero: %balance%");
		mess.put("balance_other", "&a%player% dinero: %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&cNo se encontro datos sobre TOP10");
		mess.put("pay", "&cle pagaste a %player% por %amount%");
		mess.put("pay_receive", "&aRecibiste %amount% de: %player%");
		mess.put("pay_fail", "&cTienes menos de %amount%");
		mess.put("pay_self", "&cNo puedes pagarte a ti mismo");
		mess.put("noaccount", "&cEsa cuenta no existe");
		mess.put("invalid", "&cMonto invalido");
		mess.put("money_give", "&cRecibiste %player% %amount%");
		mess.put("money_give_receive", "&aUn Staff te dio %amount%");
		mess.put("money_take", "&cRecibiste %amount% de: %player%");
		mess.put("money_take_fail", "&c%player% su saldo es menor que %amount%");
		mess.put("money_take_receive", "&cUn Staff te quito %amount%");
		mess.put("money_set", "&cLe seteaste el dinero a %player%, %amount%");
		mess.put("money_set_receive", "&cUn Staff te seteo el dinero a %amount%");
		mess.put("no_permission", "&cNo tienes permisos para usar este comando");
		// mess.put("help_title", "&6HELP");
		mess.put("help_title_full", "&6=============== [XConomy] HELP ===============");
		mess.put("help1", "&6balance/money  -  Muestra cuanto dinero tienes");
		mess.put("help2", "&6balance/money <Jugador>  -  Muestra el dinero de: <player>");
		mess.put("help3", "&6pay <Jugador> <Monto>  -  Pay <Jugador> <Monto>");
		mess.put("help4", "&6balancetop  -  Muestra el TOP10");
		mess.put("help5", "&6balance/money give <Jugador> <Monto>  -  le das dinero a: <Jugador>, <Monto>");
		mess.put("help6", "&6balance/money take <Jugador> <Monto>  -  le sacas <Monto> a: <Jugador>");
		mess.put("help7", "&6balance/money set <Jugador> <Monto>  -  setea <Jugador> dinero a: <Monto>");
	}

	private static void chinese() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&a你的余额: %balance%");
		mess.put("balance_other", "&a%player% 的余额: %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&c无玩家经济数据");
		mess.put("pay", "&c你转账给%player% 余额  %amount%");
		mess.put("pay_receive", "&a你从 %player% 收到转账  %amount%");
		mess.put("pay_fail", "&c你的余额不足  %amount%");
		mess.put("pay_self", "&c你不能向自己转账");
		mess.put("noaccount", "&c目标帐号不存在");
		mess.put("invalid", "&c输入的金额无效");
		mess.put("money_give", "&c你给予了 %player% %amount%");
		mess.put("money_give_receive", "&a管理员给予你 %amount% 余额");
		mess.put("money_take", "&c你从  %player% 收取了 %amount%");
		mess.put("money_take_fail", "&c%player% 的余额不足  %amount%");
		mess.put("money_take_receive", "&c管理员扣除了  %amount% 余额");
		mess.put("money_set", "&c你将  %player% 的金额设置为 %amount%");
		mess.put("money_set_receive", "&c管理员设置你的余额为  %amount%");
		mess.put("no_permission", "&c你没有权限使用这个指令");
		// mess.put("help_title", "&6帮助");
		mess.put("help_title_full", "&6=============== [XConomy] 帮助  ===============");
		mess.put("help1", "&6balance/money  -  查询余额");
		mess.put("help2", "&6balance/money <玩家>  -  查询<玩家>余额");
		mess.put("help3", "&6pay <玩家> <金额>  -  转账给<玩家><金额>");
		mess.put("help4", "&6balancetop  -  查询余额排行榜");
		mess.put("help5", "&6balance/money give <玩家> <金额>  -  给与<玩家><金额>");
		mess.put("help6", "&6balance/money take <玩家> <金额>  -  从<玩家>取走<金额>");
		mess.put("help7", "&6balance/money set <玩家> <金额>  -  设置<玩家>金额为<金额>");
	}

	private static void chinesetw() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&a你的餘額： %balance%");
		mess.put("balance_other", "&a%player% 的餘額： %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%： %player% - %balance%");
		mess.put("top_nodata", "&c無玩家經濟資料");
		mess.put("pay", "&c你轉帳給 %player% 餘額 %amount%");
		mess.put("pay_receive", "&a你從 %player% 收到轉帳 %amount%");
		mess.put("pay_fail", "&c你的餘額不足 %amount%");
		mess.put("pay_self", "&c你不能向自己轉帳");
		mess.put("noaccount", "&c目標帳號不存在");
		mess.put("invalid", "&c輸入的金額無效");
		mess.put("money_give", "&c你給予了 %player% %amount%");
		mess.put("money_give_receive", "&a管理員給予你 %amount% 餘額");
		mess.put("money_take", "&c你從 %player% 收取了 %amount%");
		mess.put("money_take_fail", "&c%player% 的餘額不足 %amount%");
		mess.put("money_take_receive", "&c管理員扣除了 %amount% 餘額");
		mess.put("money_set", "&c你將 %player% 的金額設定為 %amount%");
		mess.put("money_set_receive", "&c管理員設定你的餘額為 %amount%");
		mess.put("no_permission", "&c你沒有許可權使用這個指令");
		mess.put("help_title_full", "&6=============== [XConomy]幫助 ===============");
		mess.put("help1", "&6balance/money  -  查詢餘額");
		mess.put("help2", "&6balance/money <玩家>  -  査詢<玩家>餘額");
		mess.put("help3", "&6pay <玩家> <金額>  -  轉帳給<玩家><金額>");
		mess.put("help4", "&6balancetop  -  查詢餘額排行榜");
		mess.put("help5", "&6balance/money give <玩家> <金額>  -  給與<玩家><金額>");
		mess.put("help6", "&6balance/money take <玩家> <金額>  -  從<玩家>取走<金額>");
		mess.put("help7", "&6balance/money set <玩家> <金額>  -  設定<玩家>金額為<金額>");
	}

	private static void french() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&aSolde: %balance%");
		mess.put("balance_other", "&aSolde de %player% : %balance%");
		mess.put("top_title", "&e========= TOP10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&cTOP10 indisponible");
		mess.put("pay", "&aVous avez versé &a%amount% à %player% ");
		mess.put("pay_receive", "&aVous avez reçu %amount% de %player%");
		mess.put("pay_fail", "&cVous n'avez que %amount%");
		mess.put("pay_self", "&cVous ne pouvez pas vous payer vous-même");
		mess.put("noaccount", "&cLe joueur n'existe pas");
		mess.put("invalid", "&cMontant invalide");
		mess.put("money_give", "&cVous avez give %amount% à %player%");
		mess.put("money_give_receive", "&aUn Administrateur vous a give %amount%");
		mess.put("money_take", "&cVous avez reçu %amount% de %player%");
		mess.put("money_take_fail", "&c%Le joueur ne possède que %amount%");
		mess.put("money_take_receive", "&cUn administrateur vous a pris %amount%");
		mess.put("money_set", "&cVous avez défini le solde de %player% à %amount%");
		mess.put("money_set_receive", "&cUn administrateur a défini votre solde à %amount%");
		mess.put("no_permission", "&cVous n'avez pas la permission d'utiliser cette commande");
		// mess.put("help_title", "&6HELP");
		mess.put("help_title_full", "&6=============== [XConomy] HELP ===============");
		mess.put("help1", "&6balance/money  -  Affiche le montant de votre Solde");
		mess.put("help2", "&6balance/money <joueur>  -  Affiche le montant du Solde du joueur");
		mess.put("help3", "&6pay <joueur> <montant>  -  Paye le joueur visé du montant défini");
		mess.put("help4", "&6balancetop  -  Affiche le TOP10");
		mess.put("help5", "&6balance/money give <joueur> <montant>  -  Donne un montant défini au joueur visé");
		mess.put("help6", "&6balance/money take <joueur> <montant>  -  Prend un montant défini au joueur visé");
		mess.put("help7", "&6balance/money set <joueur> <montant>  -  Défini le montant du Solde au joueur visé");
	}

	private static void turkish() {
		mess.put("prefix", "&6[XConomy]");
		mess.put("balance", "&aBakiye: %balance%");
		mess.put("balance_other", "&a%player% adlı oyuncunun bakiyesi: %balance%");
		mess.put("top_title", "&e========= EN IYI 10 =========");
		mess.put("top_text", "&e%index%: %player% - %balance%");
		mess.put("top_nodata", "&cEn iyi ilk 10 listesine ait veri bulunamadı");
		mess.put("pay", "&c%player% adlı oyuncuya %amount% gönderildi");
		mess.put("pay_receive", "&a%player% adlı oyuncudan %amount% aldın");
		mess.put("pay_fail", "&cBakiyen %amount%''dan az");
		mess.put("pay_self", "&cKendine para gönderemezsin");
		mess.put("noaccount", "&cBelirtilen hesap mevcut değil");
		mess.put("invalid", "&cGeçersiz miktar");
		mess.put("money_give", "&c%player% adlı oyuncuya %amount% verildi");
		mess.put("money_give_receive", "&aBir yönetici sana %amount% verdi");
		mess.put("money_take", "&c%player% adlı oyuncunun bakiyesinden %amount% alındı");
		mess.put("money_take_fail", "&c%player% adlı oyuncunun bakiyesi %amount%''dan az");
		mess.put("money_take_receive", "&cBir yönetici senden %amount% aldı");
		mess.put("money_set", "&c%player% adlı oyuncunun bakiyesi %amount% olarak ayarlandı");
		mess.put("money_set_receive", "&cBir yönetici bakiyeni %amount% olarak ayarladı");
		mess.put("no_permission", "&cBu komutu kullanabilmek için yetkin yok.");
		mess.put("help_title_full", "&6=============== [XConomy] YARDIM ===============");
		mess.put("help1", "&6balance/money  -  Bakiyeni gösterir");
		mess.put("help2", "&6balance/money <player>  -  Belirtilen oyuncunun bakiyesini gösterir");
		mess.put("help3", "&6pay <player> <amount>  -  Belirtilen oyuncuya belirtilen miktarı gönderir");
		mess.put("help4", "&6balancetop  -  En İyi 10''u gösterir");
		mess.put("help5", "&6balance/money give <player> <amount>  -  Belirtilen oyuncuya belirtilen miktarda para verir");
		mess.put("help6", "&6balance/money take <player> <amount>  -  Belirtilen oyuncudan belirtilen miktarda para alır");
		mess.put("help7", "&6balance/money set <player> <amount>  -  Belirtilen oyuncunun bakiyesini belirtilen miktar olarak ayarlar");
	}
}
