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

    public BlueToothViewModel(Context context, BluetoothDevice bluetoothDevice) {
        super(context);
        this.bluetoothDevice = bluetoothDevice;
    }



}
