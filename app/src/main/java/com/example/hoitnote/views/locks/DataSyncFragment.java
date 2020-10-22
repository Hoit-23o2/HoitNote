package com.example.hoitnote.views.locks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentDatasyncBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.commuications.bluetooth.ReceiveInfo;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataSyncFragment extends Fragment {
    FragmentDatasyncBinding binding;
    Context context;


    public DataSyncFragment(Context context){
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_datasync,
                container,
                false
        );
        binding.setDataSyncFragment(this);
        binding.pairedDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                BluetoothDevice device = App.blueToothHelper.getPairedDeviceList().get(pos);
                DataPackage dataPackage = new DataPackage();
                dataPackage.setConfigs(App.dataBaseHelper.getConfigs());
                SendDataPackage(device, dataPackage);
            }
        });
        return  binding.getRoot();
    }

    /*获取已匹配设备事件*/
    public void getPairedList(View v){
        ArrayList<BluetoothDevice> pairedList = App.blueToothHelper.getPairedDeviceList();
        ArrayList<String> list = new ArrayList<>();
        for (BluetoothDevice device:
             pairedList) {
            list.add(device.getName());
        }
        binding.pairedDeviceListView.setAdapter(new ArrayAdapter<>(context, R.layout.item_bluetooth, list));
    }

    public void getScanningList(View v){
        ArrayList<BluetoothDevice> pairedList = App.blueToothHelper.getFoundDeviceList();

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检测设备是否支持蓝牙
        App.blueToothHelper.checkBluetoothSupport(context);
        //Im注册广播接收信号
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(App.blueToothHelper.getReceiver(), filter);
        /*开启蓝牙*/
        App.blueToothHelper.openBluetooth(context);

    }


    /*发送*/
    public void SendDataPackage(BluetoothDevice device, DataPackage dataPackage){
        /*连接设备*/
        App.blueToothHelper.connectDevice(device);
        /*发送信息*/
        App.blueToothHelper.sendDataPackage(dataPackage);
    }

    /*接受方*/
    public void ReceiveDataPackage(View v){
        /*开启服务器*/
        App.blueToothHelper.startServer(context);
        /*接受回调*/
        App.blueToothHelper.mHandler.setBlueToothHandlerListener(new BlueToothHelper.BlueToothHandler.BlueToothHandlerListener() {
            @Override
            public void onDataReceived(DataPackage dataPackage) {
                ToastHelper.showToast(context, dataPackage.toString(), Toast.LENGTH_SHORT);
                Log.d("蓝牙：",dataPackage.getConfigs().get(0).getPassword());
            }

            @Override
            public void onDataSendSuccessful(ReceiveInfo receiveInfo) {
                ToastHelper.showToast(context, receiveInfo.getMessage(),Toast.LENGTH_SHORT);
                Log.d("蓝牙：",receiveInfo.getMessage());
            }
        });
    }


}
