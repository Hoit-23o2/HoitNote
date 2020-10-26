package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class AcceptThread extends Thread {
    public BluetoothServerSocket getMmServerSocket() {
        return mmServerSocket;
    }
    private boolean exit;
    private BluetoothServerSocket mmServerSocket = null;
    private static String TAG = "Bluetooth Device";
    private static String Name = "My debug server";
    private ReceiveMessageThread receiveMessageThread;
    private BlueToothHelper.BlueToothHandler mHandler;
    private InputStream is = null;
    private OutputStream os = null;

    public BluetoothSocket getSocket() {
        return socket;
    }

    private BluetoothSocket socket = null;


    public AcceptThread(BluetoothAdapter bluetoothAdapter, UUID deviceUUID,
                        BlueToothHelper.BlueToothHandler mHandler){
        this.mHandler = mHandler;
        this.exit=false;
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
        // Keep listening until exception occurs or a socket is returned.
        if(!BlueToothHelper.isIsConnected()){
            while (true){
                try{
                    socket = mmServerSocket.accept();//接收连接
                    if(socket!=null){
                        os = socket.getOutputStream();
                        is = socket.getInputStream();
                        //开启新线程接受数据
                        receiveMessageThread = new ReceiveMessageThread(is,os,this.mHandler);
                        receiveMessageThread.start();
                        break;
                    }
                    //mmServerSocket.close();
                }catch (IOException e){
                    Log.e(TAG, "Socket's accept() method failed", e);
                    mHandler.obtainMessage(Constants.MSG_RECEIVE_FAILURE).sendToTarget();
                    break;
                }
            }
            this.mHandler.obtainMessage(Constants.MSG_CONNECT_SUCCESS).sendToTarget();
        }else{
            mHandler.obtainMessage(Constants.MSG_Is_Connected).sendToTarget();
        }
        Log.d("蓝牙","============== AcceptThread is close ====================");
    }

    public void cancel(){
        try{
            if(this.getReceiveMessageThread()!=null){
                if (this.getReceiveMessageThread().getOs()!=null){
                    this.getReceiveMessageThread().getOs().close();
                }
                if(this.getReceiveMessageThread().getIs()!=null){
                    this.getReceiveMessageThread().getIs().close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not close the I/O", e);
        }

        try {
            if(socket!=null){
                socket.close();
            }
            if(mmServerSocket!=null){
                mmServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not close the connect socket", e);
        }
        mHandler.obtainMessage(Constants.MSG_CANCEL).sendToTarget();

    }

    public ReceiveMessageThread getReceiveMessageThread() {
        return receiveMessageThread;
    }

    public InputStream getIs() {
        return is;
    }

    public OutputStream getOs() {
        return os;
    }

}