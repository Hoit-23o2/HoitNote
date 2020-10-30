package com.example.hoitnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.PasswordStatueHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.views.locks.LockActivity;
import com.example.hoitnote.views.locks.LockCountDownActivity;
import com.example.hoitnote.views.settings.SettingsActivity;

import java.sql.Date;
import java.sql.Time;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*初始化数据库*/
        App.dataBaseHelper = new DataBaseHelper(this,Constants.databaseFileName,null,Constants.databaseVersion);
        App.dataBaseHelper.setSqLiteDatabase(App.dataBaseHelper.getReadableDatabase());
        /*初始化备份数据库*/
        App.backupDataBaseHelper = new DataBaseHelper(this, Constants.backupDatabaseFileName, null, Constants.databaseVersion);
        App.backupDataBaseHelper.setSqLiteDatabase(App.backupDataBaseHelper.getReadableDatabase());
        /*初始化BookingStatic*/
        BookingDataHelper.getAccountsForShow();


        App.fileHelper = new FileHelper();

        ThemeHelper.initUI(this);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                int time = PasswordStatueHelper.getPasswordStatueTime(SplashActivity.this);
                /*进入输入密码页面*/
                if(time == 0){
                    NavigationHelper.navigationClosedCurrentActivity(SplashActivity.this,
                            LockActivity.class/*SettingsActivity.class*/);
                }
                /*进入倒计时页面*/
                else{
                    Intent intent = new Intent(SplashActivity.this, LockCountDownActivity.class);
                    intent.putExtra(Constants.currentPasswordStatue, time);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }, Constants.delayDuration);
    }

    private void Test(){
        Account mockAccount = new Account("Hello","11111");
        App.dataBaseHelper.addAccount(mockAccount);
        Tally mockTally = new Tally(1,new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis())," ",mockAccount, ActionType.OUTCOME,
                "食品酒水","无","无","无","无");
        for (int i = 0; i < 1000; i++){
            App.dataBaseHelper.addTally(mockTally);
        }
    }
}