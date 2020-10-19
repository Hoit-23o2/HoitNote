package com.example.hoitnote.customviews.popups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.PopupwindowAddaccountBinding;

import java.util.Observable;

public class AddAccountPopupView extends BaseObservable {
    //PopupWindow display method

    private String accountName;


    @Bindable
    public String getAccountName() {
        return accountName;
    }


    public void setAccountName(String accountName) {
        this.accountName = accountName;
        notifyPropertyChanged(BR.accountName);
    }


    private String accountCode;
    @Bindable
    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
        notifyPropertyChanged(BR.accountCode);
    }

    PopupwindowAddaccountBinding binding;
    View view;

    public AddAccountPopupView(View view){
        this.view = view;
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.popupwindow_addaccount,
                null,
                false
        );
        binding.setPopupView(this);
    }

    public PopupWindow showPopupWindow() {
        //Create a View object yourself through inflater
        /*LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow_addaccount, null);*/

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(binding.getRoot(), width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //Handler for clicking on the inactive zone of the window
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                //popupWindow.dismiss();

                return true;
            }
        });
        return popupWindow;
    }

    public PopupwindowAddaccountBinding getBinding(){
        return this.binding;
    }


}
