package com.example.hoitnote.utils.constants;

public class Constants {
    /*
    * Theme Related: currentTheme标识当前主题，theme标识主题
    * */
    public static String currentTheme = "current_theme";
    public static String theme = "theme";

    /*
    * Splash Related:
    * */
    public static int delayDuration = 1000;

    public static final int PERMISSION_REQUEST_CODE = 200;

    /*
    * LockActivity Related:
    * */
    public static int totalWrongCount = 5;
    public static String passwordNotEnough = "密码位数不足";
    public static String passwordWrong = "密码错误";
    public static String passwordWrongTips = "您还剩下错误的机会:";
    public static String passwordPass = "解锁成功";

    public static String passwordStatue = "password_statue";
    public static String currentPasswordStatue = "current_password_statue";
    public static int notPassTime = 10; //十分钟
    public static int passTime = 0;     //0分钟
}
