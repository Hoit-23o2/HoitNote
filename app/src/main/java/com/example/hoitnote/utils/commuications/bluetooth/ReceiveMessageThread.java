package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
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
    private BlueToothHelper blueToothHelper;
    private BlueToothHelper.BlueToothHandler mHandler;
    public ReceiveMessageThread(InputStream is,OutputStream os,
                                BlueToothHelper.BlueToothHandler mHandler,
                                BlueToothHelper blueToothHelper) {
        this.blueToothHelper = blueToothHelper;
        this.mHandler = mHandler;
        this.is = is;
        this.os = os;
    }


    @Override
    public void run(){
        while (App.ReceiveThreadFlag){
                try{
                    if(is != null){
                        int a = 0;
                        int a1 = 0;
                        /*接受sendInfo*/
                        if(blueToothHelper.getSendInfo()==null){
                            a = is.available();
                        }
                        else {
                            a = blueToothHelper.getSendInfo().getByteSize();
                        }
                        Log.d("size of A:",a+"---------------------------------");
                        byte [] buf = new byte[a];
                        while (a1!=a){
                            a1 += is.read(buf,a1,a-a1);
                        }
                        if(a != 0){
                            Object object = new Object();
                            object = BlueToothHelper.toObject(buf);
                            if(object instanceof ReceiveInfo){
                                receiveInfo = (ReceiveInfo)object;
                                this.mHandler.obtainMessage(Constants.MSG_Get_RECEIVEINFO).sendToTarget();
                                App.ReceiveThreadFlag = false;
                            }else if(object instanceof SendInfo){
                                sendInfo = (SendInfo)object;
                                this.mHandler.obtainMessage(Constants.MSG_Get_SENDINFO).sendToTarget();
                            }else if(object instanceof DataPackage){
                                dataPackage = (DataPackage)object;
                                this.mHandler.obtainMessage(Constants.MSG_RECEIVE_SUCCESS).sendToTarget();
                                App.ReceiveThreadFlag = false;
                            }
                            else {
                                Log.d("null object","+++++++++++++++++++++++++++++++++++++");
                            }
                        }
                        else{
                            Log.d("end ","+++++++++++++++++++++++++++++++++++++");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //this.mHandler.obtainMessage(Constants.MSG_RECEIVE_FAILURE).sendToTarget();
                    try {
                        Log.d(" ", "available" + is.available());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    App.ReceiveThreadFlag = false;
                    this.mHandler.obtainMessage(Constants.MSG_Get_RECEIVEINFO).sendToTarget();
                    break;
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