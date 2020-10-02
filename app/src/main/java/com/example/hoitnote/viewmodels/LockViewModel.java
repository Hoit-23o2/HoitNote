package com.example.hoitnote.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.hoitnote.BR;

public class LockViewModel extends BaseViewModel {
    private String password;


    public LockViewModel(){
        password = "";
    }

    @Bindable
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
