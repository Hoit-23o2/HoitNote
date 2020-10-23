package com.example.hoitnote.viewmodels;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.databinding.Bindable;

import com.example.hoitnote.BR;

public class BlueToothViewModel extends BaseViewModel {
    private BluetoothDevice bluetoothDevice;

    @Bindable
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        notifyPropertyChanged(BR.bluetoothDevice);
    }

    private boolean hasPaired;

    @Bindable
    public boolean isHasPaired() {
        return hasPaired;
    }

    public void setHasPaired(boolean hasPaired) {
        this.hasPaired = hasPaired;
        notifyPropertyChanged(BR.hasPaired);
    }
    public BlueToothViewModel(Context context, BluetoothDevice bluetoothDevice, boolean hasPaired) {
        super(context);
        this.bluetoothDevice = bluetoothDevice;
        this.hasPaired = hasPaired;
    }



}
