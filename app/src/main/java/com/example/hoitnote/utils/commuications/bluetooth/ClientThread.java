package com.example.hoitnote.utils.commuications.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.BlueToothHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ClientThread extends Thread{
    public  static OutputStream os;
    private InputStream is;
    String message;
    public BluetoothDevice device = null;
    public BlueToothHelper.BlueToothHandler mHandler;
    public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    private ReceiveMessageThread receiveMessageThread;
    public ClientThread(BluetoothDevice device, BlueToothHelper.BlueToothHandler mHandler) {
        this.device = device;
        this.mHandler = mHandler;
    }

    @Override
    public void run(){
        try{
            //援引一个接口
            /*
            final BluetoothSocket socket = (BluetoothSocket)device.getClass().
                    getDeclaredMethod("createRfcommSocket",new Class[]{int.class}).invoke(device,1);*/
            final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.
                    fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"));
            adapter.cancelDiscovery();//取消扫描
            while (true)
            {
                socket.connect();//连接
                Thread.sleep(1000);
                if (socket.isConnected())
                {
                    // call a function here
                    // my function blocks for the application lifetime
                    receiveMessageThread = new ReceiveMessageThread(socket,mHandler);
                    receiveMessageThread.start();
                    break;
                }
                else
                {
                    socket.close();
                }
            }

            ///Toast.makeText(context,"连接成功",Toast.LENGTH_SHORT).show();
            //连接成功，向BluetoothActivity中的BlueToothHandler传消息
            this.mHandler.obtainMessage(Constants.MSG_CONNECT_SUCCESS).sendToTarget();
            is = socket.getInputStream();//输入到本机设备的数据流
            os = socket.getOutputStream();//输出到远端设备的数据流

        } catch (IOException e) {
            e.printStackTrace();
            this.mHandler.obtainMessage(Constants.MSG_CONNECT_FAILURE).sendToTarget();
        } catch (NullPointerException E) {
            E.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //写入
    public void write(String msg){

        try{
            os.write(msg.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.obtainMessage(Constants.MSG_SEND_FAILURE).sendToTarget();
        }
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