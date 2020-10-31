package com.example.hoitnote.utils.constants;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    /*
    * Color Related
    * */

    public static String lightRed = "#FE7F7F";
    public static String lightGreen = "#C4F9AC";
    /*
    * Theme Related: currentTheme标识当前主题，theme标识主题
    * */
    public static String currentTheme = "current_theme";
    public static String theme = "theme";

    public static String defaultColorPrimary = "#F6F6F6";
    public static String defaultColorPrimaryDark= "#757575";
    public static String defaultColorAccent = "#FFD100";
    public static String defaultColorPrimaryLight = "#FFFFFF";

    public static String sweetColorPrimary = "#F6F6F6";
    public static String sweetColorPrimaryDark = "#757575";
    public static String sweetColorAccent = "#EFC7C2";
    public static String sweetColorPrimaryLight = "#FFFFFF";

    /*
    * Splash Related:
    * */
    public static int delayDuration = 1000;

    public static final int PERMISSION_REQUEST_CODE = 200;

    /*
    * LockActivity Related:
    * */
    public static int totalWrongCount = 3;
    public static String passwordNotEnough = "密码位数不足";
    public static String passwordWrong = "密码错误";
    public static String passwordWrongTips = "您还剩下错误的机会:";


    public static String passwordStatue = "password_statue";
    public static String currentPasswordStatue = "current_password_statue";
    public static int notPassTime = 10; //10s
    public static float notPassTimeFraction = 0.0001f; //10s
    public static int passTime = 0;     //0s

    public static String fingerPrintNotSupportHardWare = "您的系统版本过低，不支持指纹功能";
    public static String fingerPrintNotEnrolled = "至少录入一个指纹";
    public static String fingerPrintNotSupport = "您的手机不支持指纹功能";

    public static String patternLock = "手势解锁";
    public static String traditionalLock = "密码解锁";
    public static String fingerprintLock = "指纹解锁";
    public static String patternRegistration = "注册手势";
    public static String traditionalRegistration = "密码注册";
    public static String fingerprintRegistration = "指纹功能无法开启";
    public static String patternSettingTip1 = "请绘制上一次的手势";
    public static String patternSettingTip2 = "设置新的手势";
    public static String traditionalSettingTip1 = "请输入上一次的密码";
    public static String traditionalSettingTip2 = "设置新的密码";
    public static String fingerprintSetting = "请在安卓系统级别修改指纹";

    public static String registrationSuccess = "注册成功";
    public static String loginSuccess = "解锁成功";
    public static String registrationFalse = "注册失败，密码重复";
    public static String loginFalse = "解锁失败";
    public static String settingSuccess = "设置成功";
    public static String settingFalse = "设置失败，密码重复";

    public static String registrationBtnText = "注  册";
    public static String settingBtnText = "修  改";
    public static String confirmBtnText = "确  定";
    public static String loginBtnText = "登  录";

    /*
    * Main Activity Related
    * */
    public static String noneAccountCode = "xxxxxxxx";
    public static String noneAccountName = "Unkonw";

    public static String accountCodeSameHint = "已有账号";
    public static String accountCodeNotEnoughHint = "账号位数不足";

    public static String mainParamTag = "current_account";

    /*
    * Analysis Activity Related
    * */

    public static String analysisParamTag = "current_account";


    /*
    * Setting Activity Related
    * */
    public static int animDelayDuration = 200;

    /*
    * Data base related
    * */
    public static int databaseVersion = 1;

    public static String databaseFileName = "hoit.db";  //数据库文件名
    public static String backupDatabaseFileName = "backup_hoit.db";  //数据库文件名

    /*
     * config table related
     * */
    public static String configTableName = "ConfigTable";
    public static String createConfigTable = "CREATE TABLE IF NOT EXISTS "+ configTableName +" (id integer primary key autoincrement, currentTheme integer, password text, passwordStyle integer)";
    public static String configTableColumn_ct = "currentTheme";
    public static String configTableColumn_pw = "password";
    public static String configTableColumn_pws = "passwordStyle";

    /*
     * tally table related
     * */
    public static String tallyTableName = "TallyTable";
    public static String createTallyTable = "CREATE TABLE IF NOT EXISTS "+ tallyTableName + " (id integer primary key autoincrement, money double, mTime time, mDate date, remark text, account text, actionType integer, classification1 text, classification2 text, member text, project text, vendor text)";
    public static String tallyTableColumn_id = "id";
    public static String tallyTableColumn_money = "money";
    public static String tallyTableColumn_date = "mDate";
    public static String tallyTableColumn_remark = "remark";
    public static String tallyTableColumn_account="account";
    public static String tallyTableColumn_actionType = "actionType";
    public static String tallyTableColumn_c1 = "classification1";
    public static String tallyTableColumn_c2 = "classification2";
    public static String tallyTableColumn_member = "member";
    public static String tallyTableColumn_project = "project";
    public static String tallyTableColumn_vendor = "vendor";
    public static String tallyTableColumn_time = "mTime";
    public static String tallyTableC1C2Spliter = "&";

    /*
     * classification table related
     * */
    public static String classificationTableName = "ClassificationTable";
    public static String createClassificationTable = "CREATE TABLE IF NOT EXISTS " + classificationTableName + " (id integer primary key autoincrement, classification1 text, classification2 text, actionType integer)";
    public static String classificationColumn_c1 = "classification1";
    public static String classificationColumn_c2 = "classification2";
    public static String classificationColumn_actionType = "actionType";

    /*
     * ThirdParty table related
     * */
    public static String ThirdPartyTableName = "ThirdPartyTable";
    public static String createThirdPartyTable = "CREATE TABLE IF NOT EXISTS " + ThirdPartyTableName + " (id integer primary key autoincrement, ThirdPartyType integer, content text)";
    public static String ThirdPartyColumn_tp = "ThirdPartyType";
    public static String ThirdPartyColumn_ct = "content";

    /*
     * Account table related
     * */
    public static String AccountTableName = "AccountTable";
    public static String createAccountTable = "CREATE TABLE IF NOT EXISTS " + AccountTableName + " (id integer primary key autoincrement, accountName text, accountCode text)";
    public static String AccountColumn_an = "accountName";
    public static String AccountColumn_ac = "accountCode";

    /*
     * Symbol table related
     * */
    public static String SymbolTableName = "SymbolTable";
    public static String createSymbolTable = "CREATE TABLE IF NOT EXISTS " + SymbolTableName + " (id integer primary key autoincrement, symbolName text)";
    public static String SymbolColumn_sn = "symbolName";

    public static String symbol_ifSaveDataPackage = "ifSaveDataPackage";

    /*
    * BlueTooth Related
    * */

    public static final int MSG_SEND_SUCCESS = 0;
    public static final int MSG_SEND_FAILURE = 1;
    public static final int MSG_RECEIVE_SUCCESS = 2;
    public static final int MSG_RECEIVE_FAILURE = 3;
    public static final int MSG_CONNECT_SUCCESS = 4;
    public static final int MSG_CONNECT_FAILURE = 5;
    public static final int MSG_LISTEN_FAILURE = 6;
    public static final int MSG_Is_Connected = 7;
    public static final int MSG_Get_SENDINFO = 8;
    public static final int MSG_Get_RECEIVEINFO = 9;
    public static final int MSG_CANCEL = 10;
    public static final int MSG_DEVICE_FOUND = 11;

    public static String deviceNotSupport = "设备不支持蓝牙";


    /*BookingActivity related
     * */
    public static String BookingActivityClassification1 = "classification1";
    public static String BookingActivityClassification2 = "classification2";
    public static String BookingActivityMoney = "money";
    public static String BookingActivityAccount = "account";
    public static String BookingActivityRemark= "remark";
    public static String BookingActivityPerson = "person";
    public static String BookingActivityStore = "store";
    public static String BookingActivityProject = "project";
    public static String BookingActivityHasTemp = "has_temp";
    public static String BookingActivityIncomeAccount = "income_account";
    public static String BookingActivityOutcomeAccount = "outcome_account";
    /*IconActivity related
     * */
    public static List<String> IconFonts;
    static {
        IconFonts = new ArrayList<>();
        IconFonts.add("\uf5d7");
        IconFonts.add("\uf206");
        IconFonts.add("\uf55e");
        IconFonts.add("\uf030");
        IconFonts.add("\uf5de");
        IconFonts.add("\uf217");
        IconFonts.add("\uf1fd");
        IconFonts.add("\uf77d");
        IconFonts.add("\uf5d1");
        IconFonts.add("\uf46b");
        IconFonts.add("\uf51b");
        IconFonts.add("\uf522");
        IconFonts.add("\uf06b");
        IconFonts.add("\uf79f");
        IconFonts.add("\uf66f");
        IconFonts.add("\uf3d1");
        IconFonts.add("\uf481");
        IconFonts.add("\uf305");
        IconFonts.add("\uf5aa");
        IconFonts.add("\uf53f");
        IconFonts.add("\uf1b0");
        IconFonts.add("\uf5b0");
        IconFonts.add("\uf549");
        IconFonts.add("\uf2cc");
        IconFonts.add("\uf6fc");
        IconFonts.add("\uf7b5");
        IconFonts.add("\uf5a2");
        IconFonts.add("\uf51e");
    }
    public static final String IconAdd = "\uf067";
    public static final String IconUpArrow = "\uf106";
    public static final String IconDownArrow = "\uf107";
    public static final String IconPhone = "\uf3cd";
    public static final String IconComputer = "\uf108";
    public static final String IconEarphone = "\uf58f";
    public static final String IconArrowLeft = "\uf053";
    public static final String IconArrowRight = "\uf054";
    public static final String IconCircleArrowDown = "\uf13a";
    public static final String IconCircleArrowUp = "\uf139";
    public static final String IconNormal = "\uf005";
    public static final String IconIncome = "\uf060";
    public static final String IconOutcome = "\uf061";
    public static final String SeasonSpring = "\uf4d8 春";
    public static final String SeasonSummer = "\uf185 夏";
    public static final String SeasonAutumn = "\uf1bb 秋";
    public static final String SeasonWinter = "\uf2dc 冬";
    public static final String HzsNullString = "无";
    public static final String TypeStringProject = "项目";
    public static final String TypeStringMember = "成员";
    public static final String TypeStringVendor = "商家";
    public static int ChooseIconResultCode = 0530;
    public static int AddOptionResultCode = 0531;


    /*
     *  IconInformation table related
     * */
    public static String IconInformationTableName = "IconInformationTable";
    public static String createIconInformationTable = "CREATE TABLE IF NOT EXISTS " + IconInformationTableName + " (id integer primary key autoincrement, iconName text, iconType integer, iconCode text)";
    public static String IconInformationColumn_in = "iconName";
    public static String IconInformationColumn_it = "iconType";
    public static String IconInformationColumn_ic = "iconCode";


    /*
    * Icon Related
    * */
    public static String PingAnKeyWord = "平安";
    public static String JianSheKeyWord = "建设";
    public static String ZhongGuoKeyWord = "中国银行";
    public static String ZhongXinKeyWord = "中信";
    public static String HuaXiaKeyWord = "华夏";
}
