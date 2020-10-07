package com.example.hoitnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.enums.Theme;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.configs = App.dataBaseHelper.getConfigs();
        App.configs = new ArrayList<>(Arrays.asList(
                new Config(Theme.DEFAULT, "1234",PasswordStyle.TRADITIONAL)/*,
                new Config(Theme.DEFAULT, "01234",PasswordStyle.PIN)*/
        ));
        /*
        * 初始化相关配置
        * */
        Theme currentTheme;
        if(App.configs == null){
            currentTheme = ThemeHelper.getCurrentTheme(this);
        }
        else{
            currentTheme = App.configs.get(0).getCurrentTheme();
        }
        switch (currentTheme){
            case DEFAULT:
                setTheme(R.style.HoitNote_DefaultTheme);
                break;
            case SWEET:
                setTheme(R.style.HoitNote_SweetTheme);
                break;
        }
        ThemeHelper.initUI(this);
        if(!checkPermission(this)){
            requestPermission(this, BaseActivity.this);
        }


        super.onCreate(savedInstanceState);
    }




    /*
    * 工具方法
    * */
    private boolean checkPermission(Context context){

        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermission(Context context, Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(context,"已经获得存储读写权限",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            //Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();
        }
    }

    public int checkPasswordtyleInConfigs(ArrayList<Config> configs, PasswordStyle passwordStyle){
        int index = -1;
        for (Config config:
             configs) {
            if(config.getPasswordStyle() == passwordStyle){
                return configs.indexOf(config);
            }
        }
        return index;
    }


}