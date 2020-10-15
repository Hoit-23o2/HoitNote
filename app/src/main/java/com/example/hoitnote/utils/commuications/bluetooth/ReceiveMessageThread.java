package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.ConvertHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveMessageThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream is;
    private BlueToothHelper.BlueToothHandler mHandler;
    public ReceiveMessageThread(BluetoothSocket socket, BlueToothHelper.BlueToothHandler mHandler) {
        bluetoothSocket = socket;
        this.mHandler = mHandler;
        InputStream im = null;
        //尝试获取InputStream
        try{
            im = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        is = im;
    }


    @Override
    public void run(){
        while (!BlueToothHelper.sendFinished){
            int count=0;
            while (count==0){
                try{
                    if (is != null) {
                        count = is.available();
                    }
                    byte[] buf = new byte[1024];
                    if(is !=null){
                        is.read(buf,0,buf.length);//变量分别是缓冲区，读取起始位置，缓冲长度
                        DataPackage dataPackage = (DataPackage) ConvertHelper.toObject(buf);
                        this.mHandler.obtainMessage(Constants.MSG_RECEIVE_SUCCESS).sendToTarget();
                        BlueToothHelper.sendFinished = true;
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.mHandler.obtainMessage(Constants.MSG_RECEIVE_FAILURE).sendToTarget();
                    this.cancel();
                    break;
                }
            }
        }
    }

    public void cancel(){
        try{
            bluetoothSocket.close();
        } catch (IOException e) {

        }

    }

    @Override
    public void destroy() {
        cancel();
    }
}
