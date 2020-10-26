package com.example.hoitnote.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.SplashActivity;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.Theme;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
/*
* 该类用于处理主题相关的工作
* */
public class ThemeHelper {
    private final static Random mRandom = new Random(System.currentTimeMillis());

    /*该方法可以获取随机颜色*/
    public static int generateColor(Context context){
        final int baseColor = Color.WHITE;
        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.argb(127,red, green, blue);
    }

    public static Theme getCurrentTheme(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.theme, MODE_PRIVATE);
        int current_theme = preferences.getInt(Constants.currentTheme, 0);
        return Theme.values()[current_theme];
    }

    public static void setCurrentTheme(Context context, Theme theme){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.theme, MODE_PRIVATE).edit();
        editor.putInt(Constants.currentTheme, theme.ordinal());
        editor.apply();
    }

    /*在每个Activity开头调用*/
    public static void notifyThemeChanged(BaseActivity activity){
        /*Log.d(activity.getClass().toString(), "Flag:"+ activity.isThemeChangeFlag());
        ToastHelper.showToast(activity, "Flag:"+ activity.isThemeChangeFlag(), Toast.LENGTH_SHORT);*/
        if(activity.isThemeChangeFlag()){
            activity.restartActivity();
            activity.clearThemeChangedFlag();
        }
    }

    public static int getPrimaryLightColor(Context context){
        int colorPrimary;
        if(getCurrentTheme(context) == Theme.SWEET)
            colorPrimary = Color.parseColor(Constants.sweetColorPrimaryLight);
        else
            colorPrimary = Color.parseColor(Constants.defaultColorPrimaryLight);
        return colorPrimary;
    }

    public static int getPrimaryColor(Context context){
        int colorPrimary;
        if(getCurrentTheme(context) == Theme.SWEET)
            colorPrimary = Color.parseColor(Constants.sweetColorPrimary);
        else
            colorPrimary = Color.parseColor(Constants.defaultColorPrimary);
        return colorPrimary;
    }


    public static int getAccentColor(Context context){
        int colorAccent;
        if(getCurrentTheme(context) == Theme.SWEET)
            colorAccent = Color.parseColor(Constants.sweetColorAccent);
        else
            colorAccent = Color.parseColor(Constants.defaultColorAccent);
        return colorAccent;
    }

    /*请使用Contants中的Color*/
    public static void changeColorOfNavigationBar(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
    }
    public static void initUI(BaseActivity activity, Theme theme){
        View decorView = activity.getWindow().getDecorView();

        int layoutFlag = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            /*if(theme == Theme.DEFAULT){
                activity.getWindow().setStatusBarColor(Color.parseColor(Constants.defaultColorPrimary));
            }
            else {
                activity.getWindow().setStatusBarColor(Color.parseColor(Constants.sweetColorPrimary));
            }*/
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutFlag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        decorView.setSystemUiVisibility(layoutFlag);

        Toolbar actionBarToolbar = activity.findViewById(R.id.action_bar);
        if (actionBarToolbar != null){
            actionBarToolbar.setTitleTextColor(Color.BLACK);
        }

        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            /*if(theme == Theme.DEFAULT){
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.defaultColorPrimary)));

            }
            else {
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.sweetColorPrimary)));
            }
            actionBar.setElevation(0);*/
        }

        if(theme == Theme.SWEET){
            changeColorOfNavigationBar(activity, Color.parseColor(Constants.sweetColorPrimary));
        }
        else if(theme == Theme.DEFAULT){
            changeColorOfNavigationBar(activity, Color.parseColor(Constants.defaultColorPrimary));
        }

    }

    public static void initUI(SplashActivity activity){
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }
}
