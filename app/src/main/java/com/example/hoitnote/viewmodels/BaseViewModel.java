package com.example.hoitnote.viewmodels;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.hoitnote.BR;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;

import java.io.Serializable;
import java.util.ArrayList;

/*
* 请有ViewModel继承该BaseViewModel
* */
public class BaseViewModel extends BaseObservable  {
    public Context context;

    public BaseViewModel(Context context){
        this.context = context;
    }


}
