package com.example.hoitnote.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import com.example.hoitnote.utils.commuications.bluetooth.ReceiveInfo;
import com.example.hoitnote.utils.commuications.bluetooth.SendInfo;
import com.example.hoitnote.utils.constants.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BlueToothHelper implements IBlueTooth {
    private static final int REQUEST_ENABLE_BT = 2;//自行定义的局部变量，检查返回call的
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;

    public static void setIsConnected(boolean isConnected) {
        BlueToothHelper.isConnected = isConnected;
    }


    public static boolean isIsConnected() {
        return isConnected;
    }

    private static boolean isConnected = false;
    private static boolean recieveFinished = false;

    private BluetoothAdapter bluetoothAdapter;//Im蓝牙适配器

    ClientThread clientThread = null;//客户端线程——发送数据
    AcceptThread acceptThread = null;//服务器端线程——接受数据
    Context context;
    BaseActivity activity;
    public final BlueToothHandler mHandler;
    Set<BluetoothDevice> foundDeviceSet = new HashSet<>();

    public ArrayList<BluetoothDevice> getFoundDeviceList() {
        return foundDeviceList;
    }

    ArrayList<BluetoothDevice> foundDeviceList = new ArrayList<>();

    public static boolean isRecieveFinished() {
        return recieveFinished;
    }

    public static void setRecieveFinished(boolean recieveFinished) {
        BlueToothHelper.recieveFinished = recieveFinished;
    }

    public BlueToothHelper(BaseActivity activity){
        this.context = activity.context;
        this.activity = activity;
        mHandler = new BlueToothHandler(activity, this);
    }

    private static BluetoothSocket socket = null;

    public static BluetoothSocket getSocket() {
        return socket;
    }

    public static void setSocket(BluetoothSocket socket) {
        BlueToothHelper.socket = socket;
    }

    //交互消息
    private SendInfo sendInfo;
    public SendInfo getSendInfo() {
        return sendInfo;
    }
    public void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
    }

    private ReceiveInfo receiveInfo;

    public ReceiveInfo getReceiveInfo() {
        return receiveInfo;
    }

    public void setReceiveInfo(ReceiveInfo receiveInfo) {
        this.receiveInfo = receiveInfo;
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
                            foundDeviceList.add(device);
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
        BlueToothHelper blueToothHelper;

        public interface BluetoothAcceptListener {
            void onDataReceived(DataPackage dataPackage);
            void onConnectSuccess();
        }

        private BluetoothAcceptListener bluetoothAcceptListener = null;


        public void setBlueToothAcceptListener(BluetoothAcceptListener bluetoothAcceptListener) {
            this.bluetoothAcceptListener = bluetoothAcceptListener;
        }

        public interface BluetoothClientListener{
            void onConnectSuccess();
            void onDataSendSuccessful(ReceiveInfo receiveInfo);
        }

        private BluetoothClientListener bluetoothClientListener = null;

        public  void setBluetoothClientListener(BluetoothClientListener bluetoothClientListener){
            this.bluetoothClientListener = bluetoothClientListener;
        }


        public BlueToothHandler(BaseActivity activity, BlueToothHelper blueToothHelper){
            mActivity = new WeakReference<>(activity);
            mContext = activity.context;
            this.blueToothHelper = blueToothHelper;
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mActivity.get()==null){
                Log.d("BlueToothHandler","========== mActivity.get()==null ============");
            }
            BaseActivity activity = mActivity.get();
            /*variable*/
            ReceiveInfo receiveInfo = null;
            switch (msg.what){
                /********************          Client           ***********************/
                case Constants.MSG_SEND_SUCCESS:
                    ToastHelper.showToast(mContext,"发送成功",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_SEND_FAILURE:
                    ToastHelper.showToast(mContext,"发送失败",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_Is_Connected:
                    ToastHelper.showToast(mContext,"应用已经连接",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_Get_RECEIVEINFO:
                    ToastHelper.showToast(mContext,"对方已接受到消息",Toast.LENGTH_SHORT);
                    setRecieveFinished(true);
                    receiveInfo = blueToothHelper.clientThread.getReceiveMessageThread().getReceiveInfo();
                    bluetoothClientListener.onDataSendSuccessful(receiveInfo);
                    break;

                /********************          Accept           ***********************/
                case Constants.MSG_RECEIVE_SUCCESS:
                    ToastHelper.showToast(mContext,"服务端接受成功",Toast.LENGTH_SHORT);
                    setRecieveFinished(true);
                    //生成回复信息
                    receiveInfo = new ReceiveInfo("对方接受成功");
                    blueToothHelper.setReceiveInfo(receiveInfo);
                    Log.d("ReceiveMessage:",String.valueOf(blueToothHelper.acceptThread.getReceiveMessageThread() == null));
                    //发送回复信息
                    blueToothHelper.sendReply();
                    DataPackage dataPackage =  blueToothHelper.acceptThread.getReceiveMessageThread().getDataPackage();
                    bluetoothAcceptListener.onDataReceived(dataPackage);
                    break;
                case Constants.MSG_RECEIVE_FAILURE:
                    /*ToastHelper.showToast(mContext,"服务端接受失败",Toast.LENGTH_SHORT);*/
                    setRecieveFinished(true);
                    setIsConnected(false);
                    break;
                case Constants.MSG_LISTEN_FAILURE:
                    ToastHelper.showToast(mContext,"服务端监听线程开启失败",Toast.LENGTH_SHORT);
                    break;
                case Constants.MSG_Get_SENDINFO:
                    ToastHelper.showToast(mContext,"有信息即将输入",Toast.LENGTH_SHORT);
                    blueToothHelper.setSendInfo(blueToothHelper.acceptThread.getReceiveMessageThread().getSendInfo());
                    Log.d("蓝牙","=============接受起始信息============");
                    break;

                /***************               generate                       *******************/
                case Constants.MSG_CANCEL:
                    setRecieveFinished(false);
                    setIsConnected(false);
                    break;
                case Constants.MSG_CONNECT_SUCCESS:
                    ToastHelper.showToast(mContext,"建立连接成功",Toast.LENGTH_SHORT);
                    setIsConnected(true);
                    if(bluetoothClientListener!=null){
                        bluetoothClientListener.onConnectSuccess();
                    }
                    if (bluetoothAcceptListener!=null){
                        bluetoothAcceptListener.onConnectSuccess();
                    }
                    break;
            }
        }
    }

    @Override
    public void checkBluetoothSupport(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
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
        setRecieveFinished(false);
        if(bluetoothAdapter.isEnabled()){
            //开启蓝牙服务器端线程
            acceptThread = new AcceptThread(bluetoothAdapter,
                    UUID.fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"),
                    mHandler,
                    this);

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
        clientThread = new ClientThread(device, mHandler,this);
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
        byte[] bytes = BlueToothHelper.toByteArray(dataPackage);

        setSendInfo(new SendInfo());
        sendInfo.setBluetoothDeviceName(bluetoothAdapter.getName());
        sendInfo.setByteSize(bytes.length);
        sendObject(sendInfo);//先发送 数据信息

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendObject(dataPackage);
    }

    private void sendObject(final Object object){
        setRecieveFinished(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = BlueToothHelper.toByteArray(object);
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
        OutputStream os = null;
        if(clientThread!=null){
            os = clientThread.getOs();
        }else if(acceptThread!=null){
            os = acceptThread.getReceiveMessageThread().getOs();
        }else {
            Log.d("蓝牙","===================没有线程===============");
        }
        Log.d("蓝牙","===================写比特流===============");
        if(os!=null){
            try {
                os.write(bytes);
                os.flush();
                mHandler.obtainMessage(Constants.MSG_SEND_SUCCESS).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.obtainMessage(Constants.MSG_SEND_FAILURE).sendToTarget();
            }
        }else {
            Log.d("蓝牙","========os is null");
        }
    }

    /*
     * 发送数据  发送方 -> 反馈方
     * @param data

    public void sendDataPackage(final Object data){
        sendObject(sendInfo);//先发送 数据信息
        sendObject(data); //后发送 数据
    }*/


    /**
     * Im传输结束后，反馈方 ->发送方
     */
    public void sendReply(){
        sendObject(receiveInfo);
    }

    public static byte[] toByteArray(Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(b);
            oos.writeObject(obj);
            bytes = b.toByteArray();
            Log.d("对象转比特",bytes.length + "=========");
            //s.flush();
        }
        catch(Exception e)
        {
            Log.e("Bluetooth", "Cast exception at sending end ...");
        }

        return bytes;
    }

    /**
     * Im比特流转可序列化对象
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes)
    {
        Object obj = null;
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }
        catch(Exception ex)
        {
            Log.e("Bluetooth", "Cast exception at receiving end ...");
        }
        return obj;
    }


    /*
     * 二进制转整型的String
     * 暂时没用到
     * */
    public static String bytesToIntString(byte[] bytes) {
        String result = "";
        for (byte aByte : bytes) {
            String hexString = Integer.toString(aByte & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }

    public void cancel(){
        BlueToothHelper.setIsConnected(false);
        BlueToothHelper.setRecieveFinished(false);
        if(this.clientThread!=null){
            clientThread.cancel();
        }

        if(acceptThread!=null){
            acceptThread.cancel();
        }
    }
}
