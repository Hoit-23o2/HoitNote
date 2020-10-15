package com.example.blueteethtest.bluetoothConnectThread;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blueteethtest.BluetoothActivity;
import com.example.blueteethtest.BluetoothActivity.MyHandler;
import com.example.blueteethtest.ObejctToSend.Account;
import com.example.blueteethtest.ObejctToSend.Tally;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.blueteethtest.BluetoothActivity.toObject;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_SUCCESS;

public class ReceiveMessageThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private InputStream is;

    private TextView messageText;
    private static MyHandler mHandler;
    public ReceiveMessageThread(BluetoothSocket socket,MyHandler mHandler,TextView messageText) {
        bluetoothSocket = socket;
        this.messageText = messageText;
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

    public void run(){
        while (!BluetoothActivity.sendFinished){
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
                        ArrayList<Tally> tallies = (ArrayList<Tally>) toObject(buf);
                        messageText.setText(tallies.get(BluetoothActivity.testArrayIndex).getAccount().getAccountName());
                        this.mHandler.obtainMessage(MSG_RECEIVE_SUCCESS).sendToTarget();
                        BluetoothActivity.sendFinished = true;
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.mHandler.obtainMessage(MSG_RECEIVE_FAILURE).sendToTarget();
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
