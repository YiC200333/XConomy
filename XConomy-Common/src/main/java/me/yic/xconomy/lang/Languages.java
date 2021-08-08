/*
 *  This file (Languages.java) is a part of project XConomy
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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Languages {

    public static List<String> index() {
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
        ll.add("no_account");
        ll.add("invalid_amount");
        ll.add("over_maxnumber");
        ll.add("money_give");
        ll.add("money_give_receive");
        ll.add("money_take");
        ll.add("money_take_fail");
        ll.add("money_take_receive");
        ll.add("money_set");
        ll.add("money_set_receive");
        ll.add("no_receive_permission");
        ll.add("no_permission");
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
        return ll;

    }

    public static void english(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aBalance: %balance%");
        mess.put("balance_other", "&a%player%'s balance: %balance%");
        mess.put("top_title", "&e========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&fServer Total - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 refreshed every 5 minutes");
        mess.put("top_nodata", "&cNo TOP10 data");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&cYou pay %player% %amount%");
        mess.put("pay_receive", "&aYou receive %amount% from %player%");
        mess.put("pay_fail", "&cYour balance is less than %amount%");
        mess.put("pay_self", "&cYou can't pay yourself");
        mess.put("no_account", "&cTarget account does not exist");
        mess.put("invalid_amount", "&cInvalid amount");
        mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
        mess.put("money_give", "&cYou give %player% %amount%");
        mess.put("money_give_receive", "&aAdministrator give you %amount%");
        mess.put("money_take", "&cYou take %amount% from %player%");
        mess.put("money_take_fail", "&c%player%'s balance is less than %amount%");
        mess.put("money_take_receive", "&cAdministrator take %amount% from your account");
        mess.put("money_set", "&cYou set %player%'s balance to %amount%");
        mess.put("money_set_receive", "&cAdministrator set your balance to %amount%");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cYou don't have permission to use this command");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] HELP <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Displays your balance");
        mess.put("help2", "&6balance/money <player>  -  Displays <player>'s balance");
        mess.put("help3", "&6pay <player> <amount>  -  Pay <player> <amount>");
        mess.put("help4", "&6balancetop  -  Displays TOP10");
        mess.put("help5", "&6balance/money give <player> <amount>  -  give <player> <amount>");
        mess.put("help6", "&6balance/money take <player> <amount>  -  take <amount> from <player>");
        mess.put("help7", "&6balance/money set <player> <amount>  -  set <player>'s balance to <amount>");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  give <all/online player> <amount>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  take <amount> from <all/online player>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }

    public static void russian(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aБаланс: %balance%");
        mess.put("balance_other", "&aБаланс игрока %player%: %balance%");
        mess.put("top_title", "&e========= ТОП10 <Page %page%> =========");
        mess.put("sum_text", "&fСумарно по серверу: %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7ТОП10 обновляется каждые 5 минут");
        mess.put("top_nodata", "&cНет данных о ТОП10");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&cВы заплатили игроку %player% %amount%");
        mess.put("pay_receive", "&aВы получили %amount% от игрока %player%");
        mess.put("pay_fail", "&cВаш баланс меньше, чем %amount%");
        mess.put("pay_self", "&cВы не можете заплатить самому себе");
        mess.put("no_account", "&cУказанный аккаунт не существует");
        mess.put("invalid_amount", "&cНекорректная сумма");
        mess.put("over_maxnumber", "&cУ данного игрока достигнут лимит средств");
        mess.put("money_give", "&cВы выдали игроку %player% %amount%");
        mess.put("money_give_receive", "&aАдминистрация выдала вам %amount%");
        mess.put("money_take", "&cВы забрали %amount% у игрока %player%");
        mess.put("money_take_fail", "&cБаланс игрока %player% меньше, чем %amount%");
        mess.put("money_take_receive", "&cАдминистрация забрала %amount% с вашего аккаунта");
        mess.put("money_set", "&cВы установили баланс игрока %player% на %amount%");
        mess.put("money_set_receive", "&cАдминистрация установила ваш баланс на %amount%");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cУ вас нет прав для использования этой команды");
        // mess.put("help_title", "&6ПОМОЩЬ");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] ПОМОЩЬ <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Отображает ваш баланс");
        mess.put("help2", "&6balance/money <игрок>  -  Отображает баланс <игрока>");
        mess.put("help3", "&6pay <игрок> <сумма>  -  Заплатить <игроку> <сумму>");
        mess.put("help4", "&6balancetop  -  Отображает ТОП10");
        mess.put("help5", "&6balance/money give <игрок> <сумма>  -  Выдать <игроку> <сумму>");
        mess.put("help6", "&6balance/money take <игрок> <сумма>  -  Забрать <сумму> у <игрока>");
        mess.put("help7", "&6balance/money set <игрок> <сумма>  -  Установить баланс <игрока> на <сумму>");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  Выдать <всем/онлайн игрокам> <сумму>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  Забрать <сумму> у <всех/онлайн игроков>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }

    public static void spanish(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aDinero: %balance%");
        mess.put("balance_other", "&a%player% dinero: %balance%");
        mess.put("top_title", "&e========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&fServer Total - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 refreshed every 5 minutes");
        mess.put("top_nodata", "&cNo se encontro datos sobre TOP10");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&cle pagaste a %player% por %amount%");
        mess.put("pay_receive", "&aRecibiste %amount% de: %player%");
        mess.put("pay_fail", "&cTienes menos de %amount%");
        mess.put("pay_self", "&cNo puedes pagarte a ti mismo");
        mess.put("no_account", "&cEsa cuenta no existe");
        mess.put("invalid_amount", "&cMonto invalido");
        mess.put("over_maxnumber", "&cLa cuenta del usuario supera la cantidad maxima!");
        mess.put("money_give", "&cRecibiste %player% %amount%");
        mess.put("money_give_receive", "&aUn Staff te dio %amount%");
        mess.put("money_take", "&cRecibiste %amount% de: %player%");
        mess.put("money_take_fail", "&c%player% su saldo es menor que %amount%");
        mess.put("money_take_receive", "&cUn Staff te quito %amount%");
        mess.put("money_set", "&cLe seteaste el dinero a %player%, %amount%");
        mess.put("money_set_receive", "&cUn Staff te seteo el dinero a %amount%");
        mess.put("no_receive_permission", "&cEl usuario no tiene permisos para recibir dinero!");
        mess.put("no_permission", "&cNo tienes permisos para usar este comando");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] HELP <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Muestra cuanto dinero tienes");
        mess.put("help2", "&6balance/money <Jugador>  -  Muestra el dinero de: <player>");
        mess.put("help3", "&6pay <Jugador> <Monto>  -  Pay <Jugador> <Monto>");
        mess.put("help4", "&6balancetop  -  Muestra el TOP10");
        mess.put("help5", "&6balance/money give <Jugador> <Monto>  -  le das dinero a: <Jugador>, <Monto>");
        mess.put("help6", "&6balance/money take <Jugador> <Monto>  -  le sacas <Monto> a: <Jugador>");
        mess.put("help7", "&6balance/money set <Jugador> <Monto>  -  setea <Jugador> dinero a: <Monto>");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  give <all/online player> <amount>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  take <amount> from <all/online player>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }

    public static void chinese(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&a你的余额: %balance%");
        mess.put("balance_other", "&a%player% 的余额: %balance%");
        mess.put("top_title", "&e========= TOP10 <第 %page% 页> =========");
        mess.put("sum_text", "&f服务器总金额 - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10每5分钟刷新一次");
        mess.put("top_nodata", "&c无玩家经济数据");
        mess.put("top_out", "&c在 TOP10 以外");
        mess.put("top_hidden", "&a%player% 已隐藏");
        mess.put("top_displayed", "&a%player% 已显示");
        mess.put("pay", "&c你转账给%player% 余额  %amount%");
        mess.put("pay_receive", "&a你从 %player% 收到转账  %amount%");
        mess.put("pay_fail", "&c你的余额不足  %amount%");
        mess.put("pay_self", "&c你不能向自己转账");
        mess.put("no_account", "&c目标帐号不存在");
        mess.put("invalid_amount", "&c输入的金额无效");
        mess.put("over_maxnumber", "&c目标帐号金额超出最大值");
        mess.put("money_give", "&c你给予了 %player% %amount%");
        mess.put("money_give_receive", "&a管理员给予你 %amount% 余额");
        mess.put("money_take", "&c你从  %player% 收取了 %amount%");
        mess.put("money_take_fail", "&c%player% 的余额不足  %amount%");
        mess.put("money_take_receive", "&c管理员扣除了  %amount% 余额");
        mess.put("money_set", "&c你将  %player% 的金额设置为 %amount%");
        mess.put("money_set_receive", "&c管理员设置你的余额为  %amount%");
        mess.put("no_receive_permission", "&c目标帐号没有权限接收转账!");
        mess.put("no_permission", "&c你没有权限使用这个指令");
        // mess.put("help_title", "&6帮助");
        mess.put("global_permissions_change", "&a%permission% 指令的全局权限已被设置为 %value%");
        mess.put("personal_permissions_change", "&a%player% 的 %permission% 权限已被设置为 %value%");
        mess.put("help_title_full", "&6=============== [XConomy] 帮助 <第 %page% 页> ===============");
        mess.put("help1", "&6balance/money  -  查询余额");
        mess.put("help2", "&6balance/money <玩家>  -  查询<玩家>余额");
        mess.put("help3", "&6pay <玩家> <金额>  -  转账给<玩家><金额>");
        mess.put("help4", "&6balancetop  -  查询余额排行榜");
        mess.put("help5", "&6balance/money give <玩家> <金额>  -  给与<玩家><金额>");
        mess.put("help6", "&6balance/money take <玩家> <金额>  -  从<玩家>取走<金额>");
        mess.put("help7", "&6balance/money set <玩家> <金额>  -  设置<玩家>金额为<金额>");
        mess.put("help8", "&6balance/money give * <all/online> <金额> <理由>  -  给与<所有/在线玩家><金额>");
        mess.put("help9", "&6balance/money take * <all/online> <金额> <理由>  -  从<所有/在线玩家>取走<金额>");
        mess.put("help10", "&6balancetop hide/display <player>  -  将<玩家>的数据从Top10上隐藏或显示");
    }

    public static void chinesetw(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&a你的餘額： %balance%");
        mess.put("balance_other", "&a%player% 的餘額： %balance%");
        mess.put("top_title", "&e========= TOP10 <第 %page% 頁> =========");
        mess.put("sum_text", "&f伺服器總金額 - %balance%");
        mess.put("top_text", "&e%index%： %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10每五分鐘刷新一次");
        mess.put("top_nodata", "&c無玩家經濟資料");
        mess.put("top_out", "&c在 TOP10 以外");
        mess.put("top_hidden", "&a%player% 已隱藏");
        mess.put("top_displayed", "&a%player% 已顯示");
        mess.put("pay", "&c你轉帳給 %player% 餘額 %amount%");
        mess.put("pay_receive", "&a你從 %player% 收到轉帳 %amount%");
        mess.put("pay_fail", "&c你的餘額不足 %amount%");
        mess.put("pay_self", "&c你不能向自己轉帳");
        mess.put("no_account", "&c目標帳號不存在");
        mess.put("invalid_amount", "&c輸入的金額無效");
        mess.put("over_maxnumber", "&c目標賬號金額超出最大值");
        mess.put("money_give", "&c你給予了 %player% %amount%");
        mess.put("money_give_receive", "&a管理員給予你 %amount% 餘額");
        mess.put("money_take", "&c你從 %player% 收取了 %amount%");
        mess.put("money_take_fail", "&c%player% 的餘額不足 %amount%");
        mess.put("money_take_receive", "&c管理員扣除了 %amount% 餘額");
        mess.put("money_set", "&c你將 %player% 的金額設定為 %amount%");
        mess.put("money_set_receive", "&c管理員設定你的餘額為 %amount%");
        mess.put("no_receive_permission", "&c目標賬號沒有權限接收轉賬!");
        mess.put("no_permission", "&c你沒有許可權使用這個指令");
        mess.put("global_permissions_change", "&a%permission% 指令的全域許可權已被設定為 %value%");
        mess.put("personal_permissions_change", "&a%player% 的 %permission% 許可權已被設定為 %value%");
        mess.put("help_title_full", "&6=============== [XConomy]幫助 <第 %page% 頁> ===============");
        mess.put("help1", "&6balance/money  -  查詢餘額");
        mess.put("help2", "&6balance/money <玩家>  -  査詢<玩家>餘額");
        mess.put("help3", "&6pay <玩家> <金額>  -  轉帳給<玩家><金額>");
        mess.put("help4", "&6balancetop  -  查詢餘額排行榜");
        mess.put("help5", "&6balance/money give <玩家> <金額>  -  給與<玩家><金額>");
        mess.put("help6", "&6balance/money take <玩家> <金額>  -  從<玩家>取走<金額>");
        mess.put("help7", "&6balance/money set <玩家> <金額>  -  設定<玩家>金額為<金額>");
        mess.put("help8", "&6balance/money give * <all/online> <金額> <理由>  -  給與<所有/在綫玩家><金额>");
        mess.put("help9", "&6balance/money take * <all/online> <金額> <理由>  -  從<所有/在綫玩家>取走<金額>");
        mess.put("help10", "&6balancetop hide/display <player>  -  將<玩家>的數據從Top10上隱藏或顯示");
    }

    public static void french(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aSolde: %balance%");
        mess.put("balance_other", "&aSolde de %player% : %balance%");
        mess.put("top_title", "&e========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&fServer Total - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 refreshed every 5 minutes");
        mess.put("top_nodata", "&cTOP10 indisponible");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&aVous avez versé &a%amount% à %player% ");
        mess.put("pay_receive", "&aVous avez reçu %amount% de %player%");
        mess.put("pay_fail", "&cVous n'avez que %amount%");
        mess.put("pay_self", "&cVous ne pouvez pas vous payer vous-même");
        mess.put("no_account", "&cLe joueur n'existe pas");
        mess.put("invalid_amount", "&cMontant invalide");
        mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
        mess.put("money_give", "&cVous avez give %amount% à %player%");
        mess.put("money_give_receive", "&aUn Administrateur vous a give %amount%");
        mess.put("money_take", "&cVous avez reçu %amount% de %player%");
        mess.put("money_take_fail", "&c%Le joueur ne possède que %amount%");
        mess.put("money_take_receive", "&cUn administrateur vous a pris %amount%");
        mess.put("money_set", "&cVous avez défini le solde de %player% à %amount%");
        mess.put("money_set_receive", "&cUn administrateur a défini votre solde à %amount%");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cVous n'avez pas la permission d'utiliser cette commande");
        // mess.put("help_title", "&6HELP");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] HELP <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Affiche le montant de votre Solde");
        mess.put("help2", "&6balance/money <joueur>  -  Affiche le montant du Solde du joueur");
        mess.put("help3", "&6pay <joueur> <montant>  -  Paye le joueur visé du montant défini");
        mess.put("help4", "&6balancetop  -  Affiche le TOP10");
        mess.put("help5", "&6balance/money give <joueur> <montant>  -  Donne un montant défini au joueur visé");
        mess.put("help6", "&6balance/money take <joueur> <montant>  -  Prend un montant défini au joueur visé");
        mess.put("help7", "&6balance/money set <joueur> <montant>  -  Défini le montant du Solde au joueur visé");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  give <all/online player> <amount>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  take <amount> from <all/online player>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }

    public static void turkish(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aBakiye: %balance%");
        mess.put("balance_other", "&a%player% adlı oyuncunun bakiyesi: %balance%");
        mess.put("top_title", "&e========= EN IYI 10 <Page %page%> =========");
        mess.put("sum_text", "&fServer Total - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 refreshed every 5 minutes");
        mess.put("top_nodata", "&cEn iyi ilk 10 listesine ait veri bulunamadı");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&c%player% adlı oyuncuya %amount% gönderildi");
        mess.put("pay_receive", "&a%player% adlı oyuncudan %amount% aldın");
        mess.put("pay_fail", "&cBakiyen %amount%''dan az");
        mess.put("pay_self", "&cKendine para gönderemezsin");
        mess.put("no_account", "&cBelirtilen hesap mevcut değil");
        mess.put("invalid_amount", "&cGeçersiz miktar");
        mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
        mess.put("money_give", "&c%player% adlı oyuncuya %amount% verildi");
        mess.put("money_give_receive", "&aBir yönetici sana %amount% verdi");
        mess.put("money_take", "&c%player% adlı oyuncunun bakiyesinden %amount% alındı");
        mess.put("money_take_fail", "&c%player% adlı oyuncunun bakiyesi %amount%''dan az");
        mess.put("money_take_receive", "&cBir yönetici senden %amount% aldı");
        mess.put("money_set", "&c%player% adlı oyuncunun bakiyesi %amount% olarak ayarlandı");
        mess.put("money_set_receive", "&cBir yönetici bakiyeni %amount% olarak ayarladı");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cBu komutu kullanabilmek için yetkin yok.");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] YARDIM <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Bakiyeni gösterir");
        mess.put("help2", "&6balance/money <player>  -  Belirtilen oyuncunun bakiyesini gösterir");
        mess.put("help3", "&6pay <player> <amount>  -  Belirtilen oyuncuya belirtilen miktarı gönderir");
        mess.put("help4", "&6balancetop  -  En İyi 10''u gösterir");
        mess.put("help5", "&6balance/money give <player> <amount>  -  Belirtilen oyuncuya belirtilen miktarda para verir");
        mess.put("help6", "&6balance/money take <player> <amount>  -  Belirtilen oyuncudan belirtilen miktarda para alır");
        mess.put("help7", "&6balance/money set <player> <amount>  -  Belirtilen oyuncunun bakiyesini belirtilen miktar olarak ayarlar");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  give <all/online player> <amount>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  take <amount> from <all/online player>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }

    public static void japanese(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aあなたの残高： %balance%");
        mess.put("balance_other", "&a%player% の残高： %balance%");
        mess.put("top_title", "&e========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&f全サーバーの残高 - %balance%");
        mess.put("top_text", "&e%index%： %player% - %balance%");
        mess.put("top_subtitle", "&7ランキングTOP10の同期時間は5分です");
        mess.put("top_nodata", "&cプレイヤーの経済データはないです。");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% is hidden");
        mess.put("top_displayed", "&a%player% is displayed");
        mess.put("pay", "&cあなたは %player% さんに %amount% を送金しました。");
        mess.put("pay_receive", "&aあなたは %player% さんから %amount% 受け取りました。");
        mess.put("pay_fail", "&cあなたの残高は不足です %amount%。");
        mess.put("pay_self", "&cあなたは自分に送金できません。");
        mess.put("no_account", "&c目標アカウントは存在しない。");
        mess.put("invalid_amount", "&c入力したの量は無効です。");
        mess.put("over_maxnumber", "&cThe target account amount over the maximum amount");
        mess.put("money_give", "&cあなたは %player% さんに %amount% を与えた。");
        mess.put("money_give_receive", "&aGMさんはあなたに %amount% を与えた。");
        mess.put("money_take", "&cあなたは %player% さんから %amount% を受け取りました。");
        mess.put("money_take_fail", "&c%player% さんの残高は不足です %amount%。");
        mess.put("money_take_receive", "&cGMさんは %amount% を差し引きました。");
        mess.put("money_set", "&cあなたは %player% さんの残高を %amount% に設定されました。");
        mess.put("money_set_receive", "&cGMさんはあなたの残高を %amount% に設定されました。");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cあなたはこの命令を実行する権限はない。");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy]幫助 <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  残高照会");
        mess.put("help2", "&6balance/money <プレイヤー>  -  <プレイヤー>の残高をチェックされました。");
        mess.put("help3", "&6pay <プレイヤー> <金額>  -  は<プレイヤー>に<金額>を送金されました。");
        mess.put("help4", "&6balancetop  -  残高ランキングをチェックする。");
        mess.put("help5", "&6balance/money give <プレイヤー> <金額>  -  は<プレイヤー>に<金額>を与えた。");
        mess.put("help6", "&6balance/money take <プレイヤー> <金額>  -  は<プレイヤー>から<金額>を差し引きました。");
        mess.put("help7", "&6balance/money set <プレイヤー> <金額>  -  は<プレイヤー>さんの残高を<金額>に設定されました。");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  は<all/online player>に<金額>を与えた。");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  は<all/online player>から<金額>を差し引きました。");
        mess.put("help10", "&6balancetop hide/display <player>  -  Hide or display a <player>'s data from TOP10");
    }


    public static void german(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aKontostand: %balance%");
        mess.put("balance_other", "&a%player%''s Kontostand: %balance%");
        mess.put("top_title", "&e========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&fServer Gesamtgeld - %balance%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7Die TOP10 aktualisieren sich alle 5 Minuten");
        mess.put("top_nodata", "&cKeine TOP10 Daten vorhanden");
        mess.put("top_out", "&cOut of the TOP10");
        mess.put("top_hidden", "&a%player% ist versteckt");
        mess.put("top_displayed", "&a%player% ist sichtbar");
        mess.put("pay", "&cDu hast %amount% an %player% gezahlt");
        mess.put("pay_receive", "&aDu hast %amount% von %player% bekommen");
        mess.put("pay_fail", "&cDein Kontostand ist weniger als %amount%");
        mess.put("pay_self", "&cDu kannst dir selbst nichts zahlen");
        mess.put("no_account", "&cDer Spieler existiert nicht");
        mess.put("invalid_amount", "&cFalsche Zahleneingabe");
        mess.put("over_maxnumber", "&cDer Zielkontobetrag liegt über dem Maximalbetrag");
        mess.put("money_give", "&cDu hast %player% %amount% gegeben");
        mess.put("money_give_receive", "&aAdministrator hat dir %amount% gegeben");
        mess.put("money_take", "&cDu hast %amount% von %player% abgezogen");
        mess.put("money_take_fail", "&c%player%''s Kontostand ist weniger als %amount%");
        mess.put("money_take_receive", "&cAdministrator hat dir %amount% abgenommen");
        mess.put("money_set", "&cDu hast den Kontostand von %player% auf %amount% gesetzt");
        mess.put("money_set_receive", "&cAdministrator hat deinen Kontostand auf %amount% gesetzt");
        mess.put("no_receive_permission", "&cThe target user can't receive your payment!");
        mess.put("no_permission", "&cDu hast keine Berechtigung für diesen Befehl");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] HILFE <Page %page%> ===============");
        mess.put("help1", "&6balance/money  -  Zeigt deinen Kontostand");
        mess.put("help2", "&6balance/money <Spieler>  -  Zeigt den Kontostand von <Spieler>");
        mess.put("help3", "&6pay <Spieler> <Betrag>  -  Zahlt dem <Spieler> <Betrag>");
        mess.put("help4", "&6balancetop  -  Zeigt die TOP10");
        mess.put("help5", "&6balance/money give <Spieler> <Betrag>  -  gibt <Spieler> <Betrag>");
        mess.put("help6", "&6balance/money take <Spieler> <Betrag>  -  nimmt <Betrag> von <Spieler>");
        mess.put("help7", "&6balance/money set <Spieler> <Betrag>  -  setze den Kontostand von <Spieler> auf <Betrag>");
        mess.put("help8", "&6balance/money give * <all/online> <Betrag> <Grund>  -  gebe <Alle/Online Spieler> <Betrag>");
        mess.put("help9", "&6balance/money take * <all/online> <Betrag> <Grund>  -  nimmt <Betrag> von <Alle/Online Spieler>");
        mess.put("help10", "&6balancetop hide/display <Spieler>  -  Verstecke oder Zeige den Kontostand von <Spieler> in den TOP10");
    }

    public static void indonesia(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aDompet anda: %balance%");
        mess.put("balance_other", "&aDompet %player%: %balance%");
        mess.put("top_title", "&3========= TOP10 <Page %page%> =========");
        mess.put("sum_text", "&fTotal Server - %balance%");
        mess.put("top_text", "&3%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 diperbarui setiap 5 menit");
        mess.put("top_nodata", "&cTidak ada data TOP10");
        mess.put("top_out", "&cKeluar dari TOP10");
        mess.put("top_hidden", "&a%player% tersembunyi");
        mess.put("top_displayed", "&a%player% ditampilkan");
        mess.put("pay", "&cAnda membayar %player% sebanyak %amount%");
        mess.put("pay_receive", "&aAnda menerima %amount% dari %player%");
        mess.put("pay_fail", "&cSaldo Anda kurang dari %amount%");
        mess.put("pay_self", "&cAnda tidak dapat membayar diri Anda sendiri");
        mess.put("no_account", "&cAkun target tidak ada");
        mess.put("invalid_amount", "&cJumlah tidak valid");
        mess.put("over_maxnumber", "&cJumlah akun target melebihi jumlah maksimum");
        mess.put("money_give", "&cAnda memberikan %player% %amount%");
        mess.put("money_give_receive", "&aAdministrator memberi Anda %amount%");
        mess.put("money_take", "&cAnda mengambil %amount% dari %player%");
        mess.put("money_take_fail", "&cDompet %player% kurang dari %amount%");
        mess.put("money_take_receive", "&cAdministrator mengambil %amount% dari dompet Anda");
        mess.put("money_set", "&cAnda mengatur Dompet %player% sejumlah %amount%");
        mess.put("money_set_receive", "&cAdministrator menyetel saldo Anda menjadi %amount%");
        mess.put("no_receive_permission", "&cPengguna target tidak dapat menerima pembayaran Anda!");
        mess.put("no_permission", "&cAnda tidak memiliki izin untuk menggunakan perintah ini");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&b=============== BANTUAN [XConomy] <Halaman %page%> ===============");
        mess.put("help1", "&6balance/money  -  Menampilkan saldo Anda");
        mess.put("help2", "&6balance/money <pemain>  -  Menampilkan saldo <pemain>");
        mess.put("help3", "&6pay <pemain> <jumlah>  -  Membayar <pemain> <jumlah>");
        mess.put("help4", "&6balancetop  -  Menampilkan TOP10");
        mess.put("help5", "&6balance/money give <pemain> <jumlah>  -  memberikan <pemain> <jumlah>");
        mess.put("help6", "&6balance/money take <pemain> <jumlah>  -  mengambil <jumlah> dari <pemain>");
        mess.put("help7", "&6balance/money set <pemain> <jumlah>  -  setel <pemain> dompet menjadi <jumlah>");
        mess.put("help8", "&6balance/money give * <all/online> <jumlah> <alasan>  -  memberikan <all/online player> <jumlah>");
        mess.put("help9", "&6balance/money take * <all/online> <jumlah> <alasan>  -  mengambil <jumlah> dari <all/online player>");
        mess.put("help10", "&6balancetop hide/display <pemain>  -  Sembunyikan atau tampilkan data <pemain> dari TOP10");
    }

    public static void portuguese(HashMap<String, String> mess) {
        mess.put("prefix", "&6[XConomy]");
        mess.put("balance", "&aSaldo: %balance%");
        mess.put("balance_other", "&aSaldo de %player%: %balance%");
        mess.put("top_title", "&e========= TOP10 <Página %page%> =========");
        mess.put("sum_text", "&fTotal do servidor - %balance%%");
        mess.put("top_text", "&e%index%: %player% - %balance%");
        mess.put("top_subtitle", "&7TOP10 recarregado á cada 5 minutos");
        mess.put("top_nodata", "&cSem dados do TOP10");
        mess.put("top_out", "&cFora do TOP10");
        mess.put("top_hidden", "&a%player% está escondido");
        mess.put("top_displayed", "&a%player% está exibido");
        mess.put("pay", "&cVocê pagou para %player% %amount%");
        mess.put("pay_receive", "&aVocê recebeu %amount% de %player%");
        mess.put("pay_fail", "&cSeu saldo é menor que %amount%");
        mess.put("pay_self", "&cVocê não pode pagar para você mesmo");
        mess.put("no_account", "&cA conta de destino não existe");
        mess.put("invalid_amount", "&cQuantidade inválida");
        mess.put("over_maxnumber", "&cO saldo da conta de destino está acima do saldo máxim");
        mess.put("money_give", "&cVocê deu para %player% %amount%");
        mess.put("money_give_receive", "&aO administrador te deu %amount%");
        mess.put("money_take", "&cVocê pegou %amount% de %player%");
        mess.put("money_take_fail", "&cO saldo de %player% é menor que %amount%");
        mess.put("money_take_receive", "&cO administrador pegou %amount% de sua conta");
        mess.put("money_set", "&cVocê setou o saldo de %player% para %amount%");
        mess.put("money_set_receive", "&cO administrador setou o seu saldo para %amount%");
        mess.put("no_receive_permission", "&cO usuário não pode receber o seu pagamento!");
        mess.put("no_permission", "&cVocê não tem permissão para executar esse comando!");
        mess.put("global_permissions_change", "&aThe global %permission% permissions has been set to %value%");
        mess.put("personal_permissions_change", "&aThe %permission% permission of %player% has been set to %value%");
        mess.put("help_title_full", "&6=============== [XConomy] Ajuda <Página %page%> ===============");
        mess.put("help1", "&6balance/money  -  Mostra o seu saldo");
        mess.put("help2", "&6balance/money <player>  -  Mostra o saldo de <player>");
        mess.put("help3", "&6pay <player> <amount>  -  Paga para <player> <amount>");
        mess.put("help4", "&6balancetop  -  Mostra o TOP10");
        mess.put("help5", "&6balance/money give <player> <amount>  -  dê para <player> <amount>");
        mess.put("help6", "&6balance/money take <player> <amount>  -  tire <amount> de <player>");
        mess.put("help7", "&6balance/money set <player> <amount>  -  define o saldo de <player> para <amount>");
        mess.put("help8", "&6balance/money give * <all/online> <amount> <reason>  -  dê para <all/online player> <amount>");
        mess.put("help9", "&6balance/money take * <all/online> <amount> <reason>  -  tire <amount> de <all/online player>");
        mess.put("help10", "&6balancetop hide/display <player>  -  Esconder ou mostrar a data de <player> do TOP10");
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

    public static void translatorName(String lang, File file) {
        if (lang.equalsIgnoreCase("French")) {
            translateFile("#============================== Translator - Xx_Fluoxe_xX ==============================", file);
        } else if (lang.equalsIgnoreCase("Spanish")) {
            translateFile("#============================== Translator - gabyfig ==============================", file);
        } else if (lang.equalsIgnoreCase("Russian")) {
            translateFile("#============================== Translator1 - Trimitor ==============================\n", file);
            translateFile("#============================== Translator2 - Dbarkovski ==============================", file);
        } else if (lang.equalsIgnoreCase("Turkish")) {
            translateFile("#============================== Translator - erkutay007 ==============================", file);
        } else if (lang.equalsIgnoreCase("Japanese")) {
            translateFile("#============================== Translator - シロカミ ==============================", file);
        } else if (lang.equalsIgnoreCase("German")) {
            translateFile("#============================== Translator - Thund3rst0rm89 ==============================", file);
        } else if (lang.equalsIgnoreCase("Indonesia")) {
            translateFile("#============================== Translator - 72_TUI ==============================", file);
        }
    }
}
