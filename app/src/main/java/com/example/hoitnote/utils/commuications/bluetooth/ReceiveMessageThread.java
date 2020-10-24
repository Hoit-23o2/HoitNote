package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.ConvertHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ReceiveMessageThread extends Thread {

    private InputStream is;
    private OutputStream os;
    ArrayList<Tally> tallies;
    private DataPackage dataPackage;
    public DataPackage getDataPackage() {
        return dataPackage;
    }
    private SendInfo sendInfo;
    private ReceiveInfo receiveInfo;

    private BlueToothHelper.BlueToothHandler mHandler;
    public ReceiveMessageThread(InputStream is,OutputStream os, BlueToothHelper.BlueToothHandler mHandler) {

        this.mHandler = mHandler;
        this.is = is;
        this.os = os;
    }


    @Override
    public void run(){
        while (!BlueToothHelper.isRecieveFinished()){
            int count=0;
            while (count==0){
                if(BlueToothHelper.isRecieveFinished()){
                    break;
                }
                try{
                    if (is != null) {
                        count = is.available();
                    }
                    byte[] buf = new byte[1024];
                    if(is !=null){
                        is.read(buf,0,buf.length);//变量分别是缓冲区，读取起始位置，缓冲长度
                        Object object = new Object();
                        object = BlueToothHelper.toObject(buf);
                        if(object instanceof ReceiveInfo){
                            receiveInfo = (ReceiveInfo)object;
                            this.mHandler.obtainMessage(Constants.MSG_Get_RECEIVEINFO).sendToTarget();
                        }else if(object instanceof SendInfo){
                            sendInfo = (SendInfo)object;
                            this.mHandler.obtainMessage(Constants.MSG_Get_SENDINFO).sendToTarget();
                        }else if(object instanceof DataPackage){
                            dataPackage = (DataPackage)object;
                            this.mHandler.obtainMessage(Constants.MSG_RECEIVE_SUCCESS).sendToTarget();
                        }
                        else {
                            Log.d("null object","+++++++++++++++++++++++++++++++++++++");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.mHandler.obtainMessage(Constants.MSG_RECEIVE_FAILURE).sendToTarget();
                    break;
                }
            }
        }
        Log.d("蓝牙","============== receiveThread is close ====================");
    }

    public ReceiveInfo getReceiveInfo() {
        return receiveInfo;
    }

    public SendInfo getSendInfo() {
        return sendInfo;
    }

    public void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
    }

    public OutputStream getOs() {
        return os;
    }

    public InputStream getIs() {
        return is;
    }


}