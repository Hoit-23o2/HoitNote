package com.example.blueteethtest.bluetoothConnectThread;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blueteethtest.BluetoothActivity;
import com.example.blueteethtest.BluetoothActivity.MyHandler;
import com.example.blueteethtest.ObejctToSend.Account;
import com.example.blueteethtest.ObejctToSend.ReceiveInfo;
import com.example.blueteethtest.ObejctToSend.SendInfo;
import com.example.blueteethtest.ObejctToSend.Tally;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.blueteethtest.BluetoothActivity.toObject;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_Get_RECEIVEINFO;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_Get_SENDINFO;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_SUCCESS;

public class ReceiveMessageThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream is;
    private OutputStream os;

    private ArrayList<Tally> tallies;

    private SendInfo sendInfo;
    private ReceiveInfo receiveInfo;

    private static MyHandler mHandler;
    public ReceiveMessageThread(BluetoothSocket socket,MyHandler mHandler) {
        bluetoothSocket = socket;

        this.mHandler = mHandler;
        InputStream im = null;
        OutputStream om = null;
        //尝试获取InputStream
        try{
            im = bluetoothSocket.getInputStream();
            om = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        is = im;
        os = om;
    }

    public void run(){
        while (!BluetoothActivity.isSendFinished()){
            int count=0;
            while (count==0){
                try{
                    count = is.available();

                    byte buf[] = new byte[1024];
                    if(is !=null){
                        is.read(buf,0,buf.length);//变量分别是缓冲区，读取起始位置，缓冲长度
                        /*
                        String message = new String(buf);
                        //这里显示UI显示服务端接受的数据
                        messageText.setText(message);
                        */
                        //接受一个简单对象
                        /*
                        Account account = (Account) toObject(buf);
                        messageText.setText(account.getAccountName());
                        */
                        //接受一个包含子对象的对象
                        /*
                        Tally tally = (Tally)toObject(buf);
                        String temp = tally.getAccount().getAccountName();
                         */
                        //接受一个对象列表

                        //tallies = (ArrayList<Tally>) toObject(buf);
                        //接受各种对象
                        Object object = new Object();
                        object = toObject(buf);
                        if(object instanceof  ArrayList){
                            tallies = (ArrayList<Tally>) object;
                            this.mHandler.obtainMessage(MSG_RECEIVE_SUCCESS).sendToTarget();
                        }
                        else if(object instanceof ReceiveInfo){
                            receiveInfo = (ReceiveInfo)object;
                            this.mHandler.obtainMessage(MSG_Get_RECEIVEINFO).sendToTarget();
                        }else if(object instanceof SendInfo){
                            sendInfo = (SendInfo)object;
                            this.mHandler.obtainMessage(MSG_Get_SENDINFO).sendToTarget();
                        }else {
                            Log.d("null object","+++++++++++++++++++++++++++++++++++++");
                        }
                        //is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

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

    public ArrayList<Tally> getTallies() {
        return tallies;
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

}
