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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentDatasyncBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.helpers.BlueToothHelper;

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
        ) ;
        return  binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检测设备是否支持蓝牙
        App.blueToothHelper.checkBluetoothSupport(context);
        //Im注册广播接收信号
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(App.blueToothHelper.getReceiver(), filter);
        App.blueToothHelper.applyPermission();


        /* 发送方
        * App.blueToothHelper.exposeDevice();
        * App.blueToothHelper.scanDevice(context);
        * App.blueToothHelper.connectDevice(new BluetoothDevice);
        * App.blueToothHelper.sendDataPackage(DataPackage da);
        * */


        /* 接受方
        * App.blueToothHelper.exposeDevice();
        * App.blueToothHelper.startServer(context);
        * */
    }


}
