package com.example.hoitnote.utils.helpers;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.PopupwindowDialogNormalBinding;

public class DialogHelper {
    public static AlertDialog buildDialog(Context context, ViewDataBinding binding){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialog = builder.setView(binding.getRoot()).create();
        if(alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }
}
