package com.example.blueteethtest.bluetoothConnectThread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.example.blueteethtest.BluetoothActivity.MyHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_CONNECT_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_CONNECT_SUCCESS;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_SEND_FAILURE;

public class ClientThread extends Thread {
    public  static OutputStream os;
    private InputStream is;
    String message;
    public BluetoothDevice device = null;
    public static MyHandler mHandler;
    public BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    public ClientThread(BluetoothDevice device, MyHandler mHandler) {
        this.device = device;
        this.mHandler = mHandler;
    }

    public void run(){
        try{
            //援引一个接口
            /*
            final BluetoothSocket socket = (BluetoothSocket)device.getClass().
                    getDeclaredMethod("createRfcommSocket",new Class[]{int.class}).invoke(device,1);*/
            final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"));
            adapter.cancelDiscovery();//取消扫描
            while (true)
            {
                socket.connect();//连接
                Thread.sleep(1000);
                if (socket.isConnected())
                {
                    // call a function here
                    // my function blocks for the application lifetime
                    break;
                }
                else
                {
                    socket.close();
                }
            }

            ///Toast.makeText(context,"连接成功",Toast.LENGTH_SHORT).show();
            //连接成功，向BluetoothActivity中的MyHandler传消息
            this.mHandler.obtainMessage(MSG_CONNECT_SUCCESS).sendToTarget();
            is = socket.getInputStream();//输入到本机设备的数据流
            os = socket.getOutputStream();//输出到远端设备的数据流

            //新开一个线程来发送数据，因为读出写入数据过程为阻塞过程
            /*
            new Thread(new Runnable( ) {
                private Handler mHandler = ClientThread.mHandler;
                @Override
                public void run() {
                    //发送测试消息
                    write("这是另一台手机发送过来的数据");

                }
            }).start();*/

        } catch (IOException e) {
            e.printStackTrace();
            this.mHandler.obtainMessage(MSG_CONNECT_FAILURE).sendToTarget();
        } catch (NullPointerException E) {
            E.printStackTrace();
        /*} catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();*/
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
            mHandler.obtainMessage(MSG_SEND_FAILURE).sendToTarget();
        }
    }
}
