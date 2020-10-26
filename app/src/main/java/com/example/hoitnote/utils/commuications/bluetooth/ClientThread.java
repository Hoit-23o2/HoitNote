package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ClientThread extends Thread{
    private static final String TAG = "蓝牙";
    private OutputStream os=null;
    private InputStream is=null;
    private BluetoothSocket socket;
    String message;
    public BluetoothDevice device = null;
    public BlueToothHelper.BlueToothHandler mHandler;
    public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private BlueToothHelper blueToothHelper;
    private ReceiveMessageThread receiveMessageThread;
    public ClientThread(BluetoothDevice device,
                        BlueToothHelper.BlueToothHandler mHandler,
                        BlueToothHelper blueToothHelper) {
        this.blueToothHelper = blueToothHelper;
        this.device = device;
        this.mHandler = mHandler;
    }

    @Override
    public void run(){
        Log.d("蓝牙初始状态",String.valueOf(BlueToothHelper.isIsConnected())+"========================");
        Log.d("蓝牙初始状态",String.valueOf(BlueToothHelper.isRecieveFinished())+"========================");
        if (!BlueToothHelper.isIsConnected()){
            try{
                //援引一个接口
            /*
            final BluetoothSocket socket = (BluetoothSocket)device.getClass().
                    getDeclaredMethod("createRfcommSocket",new Class[]{int.class}).invoke(device,1);*/
                socket = device.createRfcommSocketToServiceRecord(UUID.
                        fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"));
                adapter.cancelDiscovery();//取消扫描
                while (true)
                {
                    socket.connect();//连接
                    Thread.sleep(1000);
                    if (socket.isConnected())
                    {
                        is = socket.getInputStream();//输入到本机设备的数据流
                        os = socket.getOutputStream();//输出到远端设备的数据流
                        receiveMessageThread = new ReceiveMessageThread(is,os,mHandler,blueToothHelper);
                        receiveMessageThread.start();
                        break;
                    }
                    else
                    {
                        socket.close();
                    }
                }
                this.mHandler.obtainMessage(Constants.MSG_CONNECT_SUCCESS).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                this.mHandler.obtainMessage(Constants.MSG_CONNECT_FAILURE).sendToTarget();
            } catch (NullPointerException E) {
                E.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            mHandler.obtainMessage(Constants.MSG_Is_Connected).sendToTarget();
        }
        Log.d("蓝牙","============== ClientThread is close ====================");
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
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not close the connect socket", e);
        }
        mHandler.obtainMessage(Constants.MSG_CANCEL).sendToTarget();
    }

    public OutputStream getOs() {
        return os;
    }

    public InputStream getIs() {
        return is;
    }

    public ReceiveMessageThread getReceiveMessageThread() {
        return receiveMessageThread;
    }
}