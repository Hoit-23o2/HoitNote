package com.example.hoitnote.utils.constants;

public class Constants {
    /*
    * Theme Related: currentTheme标识当前主题，theme标识主题
    * */
    public static String currentTheme = "current_theme";
    public static String theme = "theme";

    public static String defaultColorPrimary = "#FFFFFF";
    public static String defaultColorAccent = "#FFD100";

    public static String sweetColorPrimary = "#FFFFFF";
    public static String sweetColorAccent = "#EFC7C2";

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
    public static int notPassTime = 10; //十分钟
    public static int passTime = 0;     //0分钟

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
    * Setting Activity Related
    * */
    public static int animDelayDuration = 200;

    /*
    * Data base related
    * */
    public static int databaseVersion = 1;
    public static String databaseFileName = "hoit.db";  //数据库文件名

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

    /*
     * classification table related
     * */
    public static String classificationTableName = "ClassificationTable";
    public static String createClassificationTable = "CREATE TABLE IF NOT EXISTS " + classificationTableName + " (id integer primary key autoincrement, classification1 text, classification2 text)";
    public static String classificationColumn_c1 = "classification1";
    public static String classificationColumn_c2 = "classification2";

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
}
