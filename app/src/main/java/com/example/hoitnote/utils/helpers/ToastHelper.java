package com.example.hoitnote.utils.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.customviews.FontAwesome;

public class ToastHelper {
    public static void showToast(Context context, String tips, int length){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        @SuppressLint("InflateParams")
        View customToastRoot = inflater.inflate(R.layout.toast_layout, null);
        TextView tipsView = customToastRoot.findViewById(R.id.toastTips);
        tipsView.setText(tips);
        Toast customToast = new Toast(context);
        customToast.setView(customToastRoot);
        customToast.setDuration(length);
        customToast.show();
    }

    public static void showToast(Context context, String fontIcon, String tips, int length){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        @SuppressLint("InflateParams")
        View customToastRoot = inflater.inflate(R.layout.toast_layout, null);
        TextView tipsView = customToastRoot.findViewById(R.id.toastTips);
        FontAwesome fontAwesomeView = customToastRoot.findViewById(R.id.toastIcon);
        tipsView.setText(tips);
        fontAwesomeView.setText(fontIcon);
        Toast customToast = new Toast(context);
        customToast.setView(customToastRoot);
        customToast.setDuration(length);
        customToast.show();
    }
}
