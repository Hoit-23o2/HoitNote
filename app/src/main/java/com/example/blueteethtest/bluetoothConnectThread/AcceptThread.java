package com.example.blueteethtest.bluetoothConnectThread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.blueteethtest.BluetoothActivity;
import com.example.blueteethtest.BluetoothActivity.MyHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Handler;

import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_LISTEN_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_FAILURE;

//蓝牙连接线程
public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private BluetoothSocket socket;

    private static String TAG = "Bluetooth Device";
    private static String Name = "My debug server";

    private ReceiveMessageThread receiveMessageThread;
    private static MyHandler mHandler;
    private TextView messageText;//显示消息的文本栏
    public AcceptThread(BluetoothAdapter bluetoothAdapter, UUID deviceUUID,MyHandler mHandler, TextView messageText){
        this.mHandler = mHandler;
        this.messageText = messageText;
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
            mHandler.obtainMessage(MSG_LISTEN_FAILURE).sendToTarget();
        }
        mmServerSocket = tmp;

    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (!BluetoothActivity.isSendFinished()){
            try{
                socket = mmServerSocket.accept();//接收连接
                if(socket!=null){
                    BluetoothActivity.setSocket(socket);
                    //开启新线程接受数据
                    receiveMessageThread = new ReceiveMessageThread(socket,this.mHandler);
                    receiveMessageThread.start();
                }
                //mmServerSocket.close();

            }catch (IOException e){
                Log.e(TAG, "Socket's accept() method failed", e);
                mHandler.obtainMessage(MSG_RECEIVE_FAILURE).sendToTarget();
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

    public ReceiveMessageThread getReceiveMessageThread() {
        return receiveMessageThread;
    }

}
