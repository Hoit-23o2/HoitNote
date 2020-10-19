package com.example.hoitnote.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.utils.Interfaces.IBlueTooth;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.commuications.bluetooth.AcceptThread;
import com.example.hoitnote.utils.commuications.bluetooth.ClientThread;
import com.example.hoitnote.utils.constants.Constants;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlueToothHelper implements IBlueTooth {
    private static final int REQUEST_ENABLE_BT = 2;//自行定义的局部变量，检查返回call的
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static boolean isConnected = false;
    public static int testArrayIndex=0;
    public static boolean sendFinished = false;

    private BluetoothAdapter bluetoothAdapter;//Im蓝牙适配器

    ClientThread clientThread;//客户端线程——发送数据
    AcceptThread acceptThread;//服务器端线程——接受数据
    Context context;
    BaseActivity activity;
    public final BlueToothHandler mHandler;
    Set<BluetoothDevice> foundDeviceSet = new HashSet<>();
    ArrayList<BluetoothDevice> foundDeviceList = new ArrayList<>();

    public BlueToothHelper(BaseActivity activity){
        this.context = activity.context;
        this.activity = activity;
        mHandler = new BlueToothHandler(activity);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int before_add_size = foundDeviceSet.size();
            foundDeviceSet.add(device);
            if (action != null) {
                switch (action){
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.d("receiver", "开始扫描...");
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.d("receiver", "结束扫描...");
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        Log.d("receiver", "发现设备...");
                        Log.d("======================",device.getName()+"   "+device.getAddress());

                        //集合数量变化，表示有新的设备加入
                        if(before_add_size != foundDeviceSet.size()){
                            /*if(device.getName()!=null){
                                device.getType()
                                //foundDeviceList.add(device.getName());
                            }else{
                                //foundDeviceList.add("未知设备"+"\n"+device.getAddress());
                            }*/
                            foundDeviceList.add(device);

                            //foundAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }

    };

    public BroadcastReceiver getReceiver() {
        return receiver;
    }


    /*BlueToothHandler*/
    public static class BlueToothHandler extends Handler {
        private final WeakReference<BaseActivity> mActivity;
        Context mContext;

        public BlueToothHandler(BaseActivity activity){
            mActivity = new WeakReference<>(activity);
            mContext = activity.context;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mActivity.get()==null){
                Log.d("BlueToothHandler","========== mActivity.get()==null ============");
            }
            BaseActivity activity = mActivity.get();
            switch (msg.what){
                case Constants.MSG_SEND_SUCCESS:
                    ToastHelper.showToast(mContext,"发送成功",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_SEND_FAILURE:
                    ToastHelper.showToast(mContext,"发送失败",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_RECEIVE_SUCCESS:
                    ToastHelper.showToast(mContext,"服务端接受成功",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_RECEIVE_FAILURE:
                    ToastHelper.showToast(mContext,"服务端接受失败",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_CONNECT_SUCCESS:
                    ToastHelper.showToast(mContext,"客户端连接成功",Toast.LENGTH_SHORT);
                    isConnected = true;
                    break;
                case Constants.MSG_LISTEN_FAILURE:
                    ToastHelper.showToast(mContext,"服务端监听线程开启失败",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_Is_Connected:
                    ToastHelper.showToast(mContext,"应用已经连接成功",Toast.LENGTH_SHORT);
                    break;
            }
        }
    }
    @Override
    public void checkBluetoothSupport(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            ToastHelper.showToast(context, Constants.deviceNotSupport, Toast.LENGTH_SHORT);
        }else{
            Log.d("BluetoothCheck","This device has bluetooth");
        }
    }

    @Override
    public void scanDevice(Context context) {
        //判断蓝牙适配器是否开启
        if(!bluetoothAdapter.isEnabled()){
            //开启蓝牙适配器
            bluetoothAdapter.enable();
            //开始扫描
            doDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        ToastHelper.showToast(context,"正在搜索设备...",Toast.LENGTH_SHORT);
    }

    private void doDiscovery(){
        if (bluetoothAdapter.isDiscovering()) {
            //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
            bluetoothAdapter.cancelDiscovery();
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void openBluetooth(Context context) {
        //判断是否有蓝牙
        if (bluetoothAdapter != null) {
            //判断蓝牙是否开启
            if (bluetoothAdapter.isEnabled()) {
                //提示语：蓝牙已开启
                ToastHelper.showToast(context, "蓝牙已开启", Toast.LENGTH_SHORT);
            } else {
                //开启蓝牙
                bluetoothAdapter.enable();
                //提示语：蓝牙开启成功
                //toast = ToastHelper.showToastcontextgetApplicationContext(), "蓝牙开启成功", Toast.LENGTH_SHORT);
                //toast;
            }
        } else {
            //提示语：设备没有蓝牙
            ToastHelper.showToast(context, "设备没有蓝牙", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void startServer(Context context) {
        sendFinished = false;
        if(bluetoothAdapter.isEnabled()){
            //开启蓝牙服务器端线程
            acceptThread = new AcceptThread(bluetoothAdapter,
                    UUID.fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"),
                    mHandler);

            acceptThread.start();
            ToastHelper.showToast(context, "服务器已开启", Toast.LENGTH_SHORT);
        }else{
            ToastHelper.showToast(context, "请先开启蓝牙", Toast.LENGTH_SHORT);
        }
    }




    @Override
    public ArrayList<BluetoothDevice> getPairedDeviceList() {
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
        Set<BluetoothDevice> set;
        set = bluetoothAdapter.getBondedDevices();
        if(set.size() > 0){
            //这一段需要在交接的时候进行协商
            bluetoothDevices.addAll(set);
        }
        return bluetoothDevices;
    }

    @Override
    public void closeBluetooth() {
        if(bluetoothAdapter.isEnabled()){
            //关闭蓝牙
            bluetoothAdapter.disable();
        }else{
            ToastHelper.showToast(context,"蓝牙已关闭",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void exposeDevice() {
        //蓝牙是否处于可被发现和可被连接模式
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            //设置300可被扫描
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        }
        ToastHelper.showToast(context, "300秒内可被检测", Toast.LENGTH_SHORT);
    }

    public void connectDevice(BluetoothDevice device) {
        clientThread = new ClientThread(device, mHandler);
        clientThread.start();
    }

    @Override
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {
            //检查是否已经给了权限
            int checkpermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkpermission != PackageManager.PERMISSION_GRANTED) {//没有给权限
                Log.e("permission", "动态申请");
                //参数分别是当前活动，权限字符串数组，requestcode
                ToastHelper.showToast(context,"搜索回调权限未开启",Toast.LENGTH_SHORT);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ToastHelper.showToast(context,"搜索回调权限已开启",Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void sendDataPackage(DataPackage dataPackage) {
        sendObject(dataPackage);
    }

    private void sendObject(final Object object){
        sendFinished = false;
        new Thread(new Runnable() {
            private BlueToothHandler mHandler;
            private byte[] bytes = new byte[]{};
            @Override
            public void run() {
                bytes = ConvertHelper.toByteArray(object);
                writeByte(bytes);
            }
        }).start();
    }

    /**
     * Im向OutputStream传输一段比特流
     * @param bytes 字符串内容
     * 调用前必须先开启ClientThread，并且连接到远端设备的服务器
     */
    public void writeByte(byte[] bytes){
        if(ClientThread.os!=null){
            try {
                byte [] datatset= bytes;
                ClientThread.os.write(datatset);
                ClientThread.os.flush();
                mHandler.obtainMessage(Constants.MSG_SEND_SUCCESS).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.obtainMessage(Constants.MSG_SEND_FAILURE).sendToTarget();
            }
        }
    }
}
