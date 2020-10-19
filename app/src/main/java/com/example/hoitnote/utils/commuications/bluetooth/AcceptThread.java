package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private static String TAG = "Bluetooth Device";
    private static String Name = "My debug server";
    private ReceiveMessageThread receiveMessageThread;
    private BlueToothHelper.BlueToothHandler mHandler;

    public AcceptThread(BluetoothAdapter bluetoothAdapter, UUID deviceUUID,
                        BlueToothHelper.BlueToothHandler mHandler){
        this.mHandler = mHandler;

        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        //创建服务器
        //接收客户端的连接请求
        BluetoothServerSocket tmp = null;
        try{
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(Name,deviceUUID);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e( TAG,"Socket's listen() method failed", e);
            mHandler.obtainMessage(Constants.MSG_LISTEN_FAILURE).sendToTarget();
        }
        mmServerSocket = tmp;

    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (!BlueToothHelper.sendFinished){
            try{
                socket = mmServerSocket.accept();//接收连接
                if(socket!=null){
                    //开启新线程接受数据
                    receiveMessageThread = new ReceiveMessageThread(socket,this.mHandler);
                    receiveMessageThread.start();
                }
                mmServerSocket.close();

            }catch (IOException e){
                Log.e(TAG, "Socket's accept() method failed", e);
                mHandler.obtainMessage(Constants.MSG_RECEIVE_FAILURE).sendToTarget();
                break;
            }

        }
    }

    public void cancel(){
        try{
            mmServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
