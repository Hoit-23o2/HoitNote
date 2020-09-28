package com.example.hoitnote.viewmodels;

import androidx.databinding.BaseObservable;

import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;

/*
* 请有ViewModel继承该BaseViewModel
* */
public class BaseViewModel extends BaseObservable {

    public DataBaseHelper dataBaseHelper = new DataBaseHelper();
    public BlueToothHelper blueToothHelper = new BlueToothHelper();
    public FileHelper fileHelper = new FileHelper();

    public BaseViewModel(){

    }
}
