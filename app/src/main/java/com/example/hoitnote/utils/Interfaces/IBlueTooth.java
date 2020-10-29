package com.example.hoitnote.utils.Interfaces;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.hoitnote.utils.commuications.DataPackage;

import java.util.ArrayList;

public interface IBlueTooth {

    /*
    *
    * */
    void checkBluetoothSupport(Context context);

    void scanDevice(Context context);

    void openBluetooth(Context context);

    void startServer(Context context);

    ArrayList<BluetoothDevice> getPairedDeviceList();

    void closeBluetooth();

    void exposeDevice();

    void connectDevice(BluetoothDevice device);

    void applyPermission();

    void sendDataPackage(final DataPackage dataPackage);
}
