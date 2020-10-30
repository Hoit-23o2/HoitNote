package com.example.hoitnote.utils.helpers;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.PopupwindowDialogNormalBinding;
import com.example.hoitnote.databinding.PopupwindowLoaderBinding;

public class DialogHelper {

    private static AlertDialog loadingDialog = null;
    public static AlertDialog buildDialog(Context context, ViewDataBinding binding){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialog = builder.setView(binding.getRoot()).create();
        if(alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }

    public static void showLoading(Context context){
        PopupwindowLoaderBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.popupwindow_loader,
                null,
                false
        );
        loadingDialog = buildDialog(context, binding);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        Window window = loadingDialog.getWindow();
        if(window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();
    }

    public static void hideLoading(){
        if(loadingDialog != null)
            loadingDialog.dismiss();
    }
}
