package com.example.hoitnote.utils;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;

import java.util.ArrayList;

/*
* 全局静态变量
* */
public class App {
    public static SQLiteDatabase sqLiteDatabase;
    @SuppressLint("StaticFieldLeak")
    public static DataBaseHelper dataBaseHelper;
    @SuppressLint("StaticFieldLeak")
    public static BlueToothHelper blueToothHelper;
    public static FileHelper fileHelper;
    public static ArrayList<Config> configs;    //保存了除了指纹外的所有密码
}
