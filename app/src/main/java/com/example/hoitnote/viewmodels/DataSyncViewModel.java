package com.example.hoitnote.viewmodels;

import android.content.Context;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.hoitnote.utils.enums.BluetoothStatue;
import com.example.hoitnote.utils.enums.LockViewType;

public class DataSyncViewModel extends BaseViewModel {

    /*表明当前是什么页面：Lock、Settings*/
    private LockViewType lockViewType;
    @Bindable
    public LockViewType getLockViewType() {
        return lockViewType;
    }

    public void setLockViewType(LockViewType lockViewType) {
        this.lockViewType = lockViewType;
        notifyPropertyChanged(BR.lockViewType);
    }

    /*考察当前蓝牙状态*/
    private BluetoothStatue currentStatue;

    @Bindable
    public BluetoothStatue getCurrentStatue() {
        return currentStatue;
    }

    public void setCurrentStatue(BluetoothStatue currentStatue) {
        this.currentStatue = currentStatue;
        notifyPropertyChanged(BR.currentStatue);
    }

    public DataSyncViewModel(Context context, LockViewType lockViewType) {
        super(context);
        this.lockViewType = lockViewType;
    }


}
