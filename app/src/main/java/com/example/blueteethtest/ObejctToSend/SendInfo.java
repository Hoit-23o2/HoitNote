package com.example.blueteethtest.ObejctToSend;

import android.bluetooth.BluetoothDevice;

public class SendInfo {
    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    private BluetoothDevice device;
}
