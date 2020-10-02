package com.example.hoitnote.utils.helpers;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    public static void showToast(Context context, String tips, int length){
        Toast.makeText(context, tips, length).show();
    }
}
