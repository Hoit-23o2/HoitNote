package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.example.hoitnote.utils.helpers.BlueToothHelper;

import java.io.Serializable;

public class SendInfo implements Serializable {
    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    private BluetoothDevice device;

    public String getBluetoothDeviceName() {
        return bluetoothDeviceName;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        this.bluetoothDeviceName = bluetoothDeviceName;
    }

    private String bluetoothDeviceName;
}
