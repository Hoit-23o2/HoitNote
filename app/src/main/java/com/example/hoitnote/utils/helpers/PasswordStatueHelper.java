package com.example.hoitnote.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hoitnote.utils.constants.Constants;

import static android.content.Context.MODE_PRIVATE;

public class PasswordStatueHelper {
    public static int getPasswordStatueTime(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.passwordStatue, MODE_PRIVATE);
        return preferences.getInt(Constants.currentPasswordStatue, Constants.passTime);
    }

    public static void setPasswordSatueTime(Context context,int time){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.passwordStatue, MODE_PRIVATE).edit();
        editor.putInt(Constants.currentPasswordStatue, time);
        editor.apply();
    }
}
