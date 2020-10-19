package com.example.blueteethtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blueteethtest.ObejctToSend.Account;
import com.example.blueteethtest.ObejctToSend.ReceiveInfo;
import com.example.blueteethtest.ObejctToSend.SendInfo;
import com.example.blueteethtest.ObejctToSend.Tally;
import com.example.blueteethtest.bluetoothConnectThread.AcceptThread;
import com.example.blueteethtest.bluetoothConnectThread.ClientThread;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_CONNECT_SUCCESS;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_Get_RECEIVEINFO;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_Get_SENDINFO;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_Is_Connected;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_LISTEN_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_RECEIVE_SUCCESS;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_SEND_FAILURE;
import static com.example.blueteethtest.bluetoothInterface.MessageConstants.MSG_SEND_SUCCESS;
import static java.lang.Thread.activeCount;

public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 2;//自行定义的局部变量，检查返回call的
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1;

    public static void setConnectState(boolean connectState) {
        BluetoothActivity.connectState = connectState;
    }

    public static boolean isConnectState() {
        return connectState;
    }

    private static boolean connectState = false;

    public static boolean isSendFinished() {
        return sendFinished;
    }
    public static void setSendFinished(boolean sendFinished) {
        BluetoothActivity.sendFinished = sendFinished;
    }
    private static boolean sendFinished = false;

    public static BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    private static BluetoothAdapter bluetoothAdapter;//Im蓝牙适配器

    private ClientThread clientThread;//客户端线程——发送数据
    private AcceptThread acceptThread;//服务器端线程——接受数据

    //已配对设备列表
    public ArrayAdapter pairedAdapter;
    public ArrayList<String> pairedDeviceNameList= new ArrayList<>();
    public ArrayList<BluetoothDevice> pairedDeviceList= new ArrayList<>();
    public Set<BluetoothDevice> pairedDeviceSet = new HashSet<>();;
    //发现的设备列表
    public ArrayAdapter foundAdapter;
    public ArrayList<String> foundDeviceNameList= new ArrayList<>();
    public ArrayList<BluetoothDevice> foundDeviceList= new ArrayList<>();
    public Set<BluetoothDevice> foundDeviceSet = new HashSet<>();

    //接受的设备
    private ArrayList<Tally> tallies;

    //SOCKET
    private static BluetoothSocket socket = null;

    public static BluetoothSocket getSocket() {
        return socket;
    }

    public static void setSocket(BluetoothSocket socket) {
        BluetoothActivity.socket = socket;
    }

    //交互消息
    private static SendInfo sendInfo;
    public SendInfo getSendInfo() {
        return sendInfo;
    }
    public void setSendInfo(SendInfo sendInfo) {
        this.sendInfo = sendInfo;
    }

    private static ReceiveInfo receiveInfo;
    public ReceiveInfo getReceiveInfo() {
        return receiveInfo;
    }
    public void setReceiveInfo(ReceiveInfo receiveInfo) {
        this.receiveInfo = receiveInfo;
    }


    //页面控件变量
    private Button sendn;
    private Button scann;
    private Button openn;
    private Button servern;
    private Button closen;
    private Button beChecked;
    private Toast toast;
    private static TextView messageText;



    //Im子线程传回信息的Handler
    //用于发送各种Toast
    public final  MyHandler mHandler = new MyHandler(this, tallies);
    public static class MyHandler extends Handler{
        private final WeakReference<BluetoothActivity> mActivity;
        private ArrayList<Tally> tallies = new ArrayList<>();
        public MyHandler(BluetoothActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        public MyHandler(BluetoothActivity activity, ArrayList<Tally> tallies){
            mActivity = new WeakReference<>(activity);
            this.tallies = tallies;
        }

        public ArrayList<Tally> getTallies(){
            return tallies;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mActivity.get()==null){
                Log.d("MyHandler","========== mActivity.get()==null ============");
            }
            BluetoothActivity activity = mActivity.get();
            switch (msg.what){
                case MSG_SEND_SUCCESS:
                    Toast.makeText(activity,"发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SEND_FAILURE:
                    Toast.makeText(activity,"发送失败",Toast.LENGTH_SHORT).show();
                    break;
                case MSG_RECEIVE_SUCCESS:
                    Toast.makeText(activity,"服务端接受成功",Toast.LENGTH_SHORT).show();
                    //修改状态
                    setSendFinished(true);
                    //生成回复信息
                    ReceiveInfo receiveInfo = new ReceiveInfo("接受成功");
                    activity.setReceiveInfo(receiveInfo);
                    //发送回复信息
                    activity.sendReply();
                    //展示收到的信息
                    this.tallies = activity.acceptThread.getReceiveMessageThread().getTallies();
                    activity.messageText.setText(tallies.get(1).getAccount().getAccountName());
                    break;
                case MSG_RECEIVE_FAILURE:
                    Toast.makeText(activity,"服务端接受失败",Toast.LENGTH_SHORT).show();

                    break;
                case MSG_CONNECT_SUCCESS:
                    Toast.makeText(activity,"客户端连接成功",Toast.LENGTH_SHORT).show();
                    setConnectState(true);
                    break;
                case MSG_LISTEN_FAILURE:
                    Toast.makeText(activity,"服务端监听线程开启失败",Toast.LENGTH_SHORT).show();
                    break;
                case MSG_Is_Connected:
                    Toast.makeText(activity,"应用已经连接成功",Toast.LENGTH_SHORT).show();
                    break;
                case MSG_Get_SENDINFO:
                    activity.setSendInfo(activity.acceptThread.getReceiveMessageThread().getSendInfo());
                    break;
                case MSG_Get_RECEIVEINFO:
                    Toast.makeText(activity,"对方已接受到消息",Toast.LENGTH_SHORT).show();
                    activity.setReceiveInfo(activity.clientThread.getReceiveMessageThread().getReceiveInfo());
                    activity.messageText.setText(activity.getReceiveInfo().getMessage());
                    break;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_teeth);

        //初始化状态
        setConnectState(false);
        setSendFinished(false);

        sendn = (Button) findViewById(R.id.send);
        scann = (Button) findViewById(R.id.scan);
        openn = (Button) findViewById(R.id.open);
        servern = (Button) findViewById(R.id.server);
        closen = (Button) findViewById(R.id.close);
        beChecked = (Button) findViewById(R.id.cannotScan);
        messageText = (TextView)findViewById(R.id.message);

        //检测设备是否支持蓝牙
        checkBluetoothSupport();
        //Im注册广播接收信号
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);

        //定义扫描按钮的事件
        scann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDevice();
            }
        });

        //定义开启open控件的事件
        openn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBluetooth();
            }
        });

        //定义server控件的事件
        servern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });

        pairedDeviceList = getPairedDeviceList();

        ListView pairedListView = (ListView)findViewById(R.id.paired_device_list);
        ListView foundListView = (ListView)findViewById(R.id.found_device_list);
        //显示已配对的设备
        //ArrayAdepter()中间那个参数是item布局的源文件
        pairedAdapter = new ArrayAdapter(this,R.layout.list_item,pairedDeviceNameList);
        foundAdapter = new ArrayAdapter(this,R.layout.list_item,foundDeviceNameList);
        pairedListView.setAdapter(pairedAdapter);
        foundListView.setAdapter(foundAdapter);


        //定义控件close事件
        closen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(bluetoothAdapter.isEnabled()){
                    //关闭蓝牙
                    bluetoothAdapter.disable();
                    toast = Toast.makeText(getApplicationContext(),"蓝牙关闭成功",Toast.LENGTH_SHORT);
                    toast.show();
                    //清楚设别列表
                    pairedAdapter.clear();
                    pairedAdapter.notifyDataSetChanged();
                    foundAdapter.clear();
                    foundAdapter.notifyDataSetChanged();
                }else{
                    toast = Toast.makeText(getApplicationContext(),"蓝牙已关闭",Toast.LENGTH_SHORT);
                    toast.show();
                }*/
                closeBluetooth();
            }
        });

        //定义cannotScan控件的事件
        beChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //蓝牙是否处于可被发现和可被连接模式
                if(bluetoothAdapter.getScanMode()!=bluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    //设置300可被扫描
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                }
                toast = Toast.makeText(getApplicationContext(), "300秒内可被检测", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //定义列表Item的点击事件
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = pairedDeviceList.get(position);
                connectDevice(device);
            }
        });

        foundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = foundDeviceList.get(position);
                connectDevice(device);
            }
        });

        //定义send控件的点击事件
        sendn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //debug中传输的信息
                /*
                String message = "Hello World!";
                //writeMsg(message);
                sendMessage(message);
                */
                //传输一个对象
                /*
                Account account = new Account("测试用户","2302");
                sendObject(account);
                */
                //传输包含对象的对象
                /*
                Account account = new Account("测试用户1","2302");
                Tally tally = new Tally();
                tally.setAccount(account);
                sendObject(tally);
                 */
                //传输对象列表
                ArrayList<Tally> tallies = new ArrayList<>();
                Tally tally1 = new Tally();
                Tally tally2 = new Tally();
                Tally tally3 = new Tally();

                Account account1 = new Account("测试用户1","2301");
                Account account2 = new Account("测试用户2","2302");
                Account account3 = new Account("测试用户3","2303");

                tally1.setAccount(account1);
                tally2.setAccount(account2);
                tally3.setAccount(account3);

                tallies.add(tally1);
                tallies.add(tally2);
                tallies.add(tally3);
                sendObject(tallies);

            }
        });
        applypermission();
    }

    //Im搜索可用但未配对蓝牙设备，官方文件是放在onCreate外面
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int before_add_size = foundDeviceSet.size();
            foundDeviceSet.add(device);
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
                    if(before_add_size!=foundDeviceSet.size()){
                        if(device.getName()!=null){
                            foundDeviceNameList.add(device.getName());
                        }else{
                            foundDeviceNameList.add("未知设备"+"\n"+device.getAddress());
                        }
                        foundDeviceList.add(device);
                        foundAdapter.notifyDataSetChanged();
                    }
                    break;
            }


        }

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();//解除注册
        unregisterReceiver(receiver);
        releaseAllThread();
    }

    /**
     *
     */
    private static void releaseAllThread(){
        BluetoothActivity.setSendFinished(true);
        BluetoothActivity.setConnectState(false);

    }

    /**
     * Im检测设备是否支持蓝牙
     * 在放在BluetoothActivity.onCreate()的一开始，在定义MyHandler内部静态类之后
     */
    public void checkBluetoothSupport(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            Toast toast = Toast.makeText(BluetoothActivity.this,"设备不支持蓝牙",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            Log.d("BluetoothCheck","This device has bluetooth");
        }
    }

    /**
     * Im开启蓝牙扫描
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     * 可以与“搜索设备”Button绑定
     */
    public void scanDevice(){
        //判断蓝牙适配器是否开启
        if(!bluetoothAdapter.isEnabled()){
            //开启蓝牙适配器
            bluetoothAdapter.enable();
            //开始扫描
            doDiscovry();
        }
        bluetoothAdapter.startDiscovery();
        Toast.makeText(BluetoothActivity.this,"正在搜索设备...",Toast.LENGTH_SHORT).show();
    }

    /**
     * Im开启蓝牙扫描子函数
     */
    public void doDiscovry() {
        if (bluetoothAdapter.isDiscovering()) {
            //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
            bluetoothAdapter.cancelDiscovery();
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    /**
     * Im打开蓝牙
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     * 可以与“打开蓝牙”Button绑定
     */
    public void openBluetooth(){
        //判断是否有蓝牙
        if (bluetoothAdapter != null) {
            //判断蓝牙是否开启
            if (bluetoothAdapter.isEnabled()) {
                //提示语：蓝牙已开启
                toast = Toast.makeText(getApplicationContext(), "蓝牙已开启", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                //开启蓝牙
                bluetoothAdapter.enable();
                //提示语：蓝牙开启成功
                //toast = Toast.makeText(getApplicationContext(), "蓝牙开启成功", Toast.LENGTH_SHORT);
                //toast.show();
            }
        } else {
            //提示语：设备没有蓝牙
            toast = Toast.makeText(getApplicationContext(), "设备没有蓝牙", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Im开启服务器，启动接受数据线程
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     * 可以与“开启服务器”Button绑定
     */
    public void startServer(){
        sendFinished = false;
        if(bluetoothAdapter.isEnabled()){
            //开启蓝牙服务器端线程
            acceptThread = new AcceptThread(bluetoothAdapter,
                    UUID.fromString("1a79f483-b8ae-4e8d-97f0-a1496439136b"),
                    mHandler,
                    messageText);

            acceptThread.start();
            toast = Toast.makeText(getApplicationContext(), "服务器已开启", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            toast = Toast.makeText(getApplicationContext(), "请先开启蓝牙", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Im获取本机已配对设备列表
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     * 在onCreate()中调用
     */
    public ArrayList<BluetoothDevice>getPairedDeviceList(){
        ArrayList<BluetoothDevice> list = new ArrayList<>();
        Set<BluetoothDevice> set = new HashSet<>();
        set = bluetoothAdapter.getBondedDevices();
        if(set.size()>0){
            for(BluetoothDevice device:set){
                String deviceName = device.getName();
                String deviceMACAddress = device.getAddress();
                pairedDeviceNameList.add(deviceName+"\n"+deviceMACAddress);//这一段需要在交接的时候进行协商
                list.add(device);
            }
        }
        return list;
    }

    /**
     * Im关闭蓝牙
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     */
    public void closeBluetooth(){
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();

            pairedAdapter.clear();
            pairedAdapter.notifyDataSetChanged();
            foundAdapter.clear();
            foundAdapter.notifyDataSetChanged();
        }else{
            toast = Toast.makeText(BluetoothActivity.this,"蓝牙已关闭",Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * Im开启被扫描模式
     * 调用之前一定要先初始化bluetoothAdapter 或调用过checkBluetoothSupport()
     * 可以与“开放检测”Button绑定
     * 搜索失败时使用，但目前还没用过
     */
    public void exposeDevice(){
        //蓝牙是否处于可被发现和可被连接模式
        if(bluetoothAdapter.getScanMode()!=bluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            //设置300可被扫描
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        }
        toast = Toast.makeText(getApplicationContext(), "300秒内可被检测", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Im若设备未与远端设备连接，连接选中的蓝牙设备，开启客户端线程准备发送信息。若当前设备已经
     * 连接上其他设备，则显示已经连接消息，不再开启客户端线程。
     * @param device
     */
    public void connectDevice(BluetoothDevice device){

//        if(!isConnectState()){
//            clientThread = new ClientThread(device, mHandler);
//            clientThread.start();
//        }else{
//            mHandler.obtainMessage(MSG_Is_Connected).sendToTarget();
//        }
        clientThread = new ClientThread(device, mHandler);
        clientThread.start();
    }


    /**
     *Im动态申请权限
     * 放在BluetoothActivity.onCreate()的最后
     */
    public void applypermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //检查是否已经给了权限
            int checkpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkpermission != PackageManager.PERMISSION_GRANTED) {//没有给权限
                Log.e("permission", "动态申请");
                //参数分别是当前活动，权限字符串数组，requestcode
                Toast.makeText(BluetoothActivity.this,"搜索回调权限未开启",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(BluetoothActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                Toast.makeText(BluetoothActivity.this,"搜索回调权限已开启",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /***************************            这部分用于传输               *********************************/

    /**
     * 向服务器端发送字符串
     * @param msg 发送的信息
     */
    public void sendMessage(String msg){
        final String message = msg;
        sendFinished = false;
        //新开一个数据输入线程
        new Thread(new Runnable() {
            private MyHandler mHandler;
            @Override
            public void run() {
                writeMsg(message);
            }
        }).start();
    }

    /**
     * Im向服务器端发送一个数据对象
     * @param object 要传的参数
     */
    public void sendObject(final Object object){
        sendFinished = false;
        new Thread(new Runnable() {
            private MyHandler mHandler;
            private byte[] bytes = new byte[]{};
            @Override
            public void run() {
                bytes = toByteArray(object);
                writeByte(bytes);
            }
        }).start();
    }

    /**
     * 发送数据  发送方 -> 反馈方
     * @param data
     */
    public void sendDataPackage(final Object data){
        sendObject(sendInfo);//先发送 数据信息
        sendObject(data); //后发送 数据
    }


    /**
     * Im传输结束后，反馈方 ->发送方
     */
    public void sendReply(){
        sendObject(receiveInfo);
    }


    /***************************            这部分用于转换               *********************************/

    /**
     * Im可序列化object转比特流
     * sendObejct中会用到
     * @param obj
     * @return
     */
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
            //oos.flush();
        }
        catch(Exception e)
        {
            Log.e("Bluetooth", "Cast exception at sending end ...");
        }

        return bytes;
    }

    /**
     * Im可序列化object列表转比特流
     * sendArrayList中会用到
     * @param arrayList
     * @return
     */
    public static byte[] ToByteArray(ArrayList<Object> arrayList){
        byte[] bytes = null;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(b);
            oos.writeObject(arrayList);
            bytes = b.toByteArray();
            //oos.flush();
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
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }

    /**
     * Im向OutputStream传输一段字符串
     * @param msg 字符串内容
     * 调用前必须先开启ClientThread，并且连接到远端设备的服务器
     */
    public  void writeMsg(String msg){
        OutputStream os = clientThread.getOs();
        if(os!=null){
            try {
                byte [] datatset= msg.getBytes("utf-8");
                os.write(datatset);
                os.flush();
                mHandler.obtainMessage(MSG_SEND_SUCCESS).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.obtainMessage(MSG_SEND_FAILURE).sendToTarget();
            }
        }
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
        }else{
            os = acceptThread.getReceiveMessageThread().getOs();
        }

        if(os!=null){
            try {
                byte [] datatset= bytes;
                os.write(datatset);
                os.flush();
                mHandler.obtainMessage(MSG_SEND_SUCCESS).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.obtainMessage(MSG_SEND_FAILURE).sendToTarget();
            }
        }
    }


    /***  这部分用于parcel  ***/


}