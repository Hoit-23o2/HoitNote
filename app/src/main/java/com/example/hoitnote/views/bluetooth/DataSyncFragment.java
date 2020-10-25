package com.example.hoitnote.views.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.bluetooth.BlueToothDeviceAdapter;
import com.example.hoitnote.databinding.FragmentDatasyncBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.commuications.bluetooth.ReceiveInfo;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.BluetoothStatue;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DeviceHelper;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BlueToothViewModel;
import com.example.hoitnote.viewmodels.DataSyncViewModel;

import java.util.ArrayList;

public class DataSyncFragment extends Fragment {
    FragmentDatasyncBinding binding;
    DataSyncViewModel dataSyncViewModel;
    Context context;


    public DataSyncFragment(Context context, DataSyncViewModel dataSyncViewModel){
        this.context = context;
        this.dataSyncViewModel = dataSyncViewModel;
        dataSyncViewModel.setCurrentStatue(BluetoothStatue.NORMAL);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                dataSyncViewModel.setCurrentStatue(BluetoothStatue.CONNECTING);
                binding.senderBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_connecting_hint));
                BluetoothDevice device = App.blueToothHelper.getPairedDeviceList().get(pos);
                DataPackage dataPackage = new DataPackage();
                dataPackage.setConfigs(App.dataBaseHelper.getConfigs());
                dataPackage.setAllTallies(App.dataBaseHelper.getTallies(null));
                sendDataPackage(device, dataPackage);
            }
        });
        binding.setDataSyncViewModel(dataSyncViewModel);
        binding.mainContainer.getLayoutTransition().setAnimateParentHierarchy(false);
        /*点击关闭按钮*/
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pairedDeviceListViewContainer.setVisibility(View.GONE);
            }
        });
        return  binding.getRoot();
    }

    /*点击搜索设备按钮，获取已匹配设备事件*/
    public void getPairedList(View v){
        ArrayList<BluetoothDevice> pairedList = App.blueToothHelper.getPairedDeviceList();
        pairedList.addAll(getScanningList(v));
        ArrayList<BlueToothViewModel> blueToothViewModels = new ArrayList<>();
        for (BluetoothDevice device:
             pairedList) {
            blueToothViewModels.add(new BlueToothViewModel(context, device));
        }
        BlueToothDeviceAdapter blueToothDeviceAdapter = new BlueToothDeviceAdapter(context, blueToothViewModels);
        binding.pairedDeviceListView.setAdapter(blueToothDeviceAdapter);
        binding.pairedDeviceListViewContainer.setVisibility(View.VISIBLE);
    }

    /*扫描设备事件*/
    public ArrayList<BluetoothDevice> getScanningList(View v){
        return App.blueToothHelper.getFoundDeviceList();
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
        App.blueToothHelper.applyPermission();
        App.blueToothHelper.scanDevice(context);
    }


    /*发送*/
    public void sendDataPackage(BluetoothDevice device, final DataPackage dataPackage){
        /*连接设备*/
        App.blueToothHelper.connectDevice(device);
        /*发送信息*/
        App.blueToothHelper.mHandler.setBluetoothClientListener(new BlueToothHelper.BlueToothHandler.BluetoothClientListener() {
            @Override
            public void onConnectSuccess() {
                dataSyncViewModel.setCurrentStatue(BluetoothStatue.SENDING);
                binding.senderBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_sending_hint));
                App.blueToothHelper.sendDataPackage(dataPackage);
                ToastHelper.showToast(context, "连接成功",Toast.LENGTH_SHORT);
            }

            @Override
            public void onDataSendSuccessful(ReceiveInfo receiveInfo) {
                dataSyncViewModel.setCurrentStatue(BluetoothStatue.SENDED);
                binding.senderBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_sended_hint));
                ToastHelper.showToast(context, receiveInfo.getMessage(),Toast.LENGTH_SHORT);
                Log.d("蓝牙：",receiveInfo.getMessage());
            }
        });

    }

    /*接受方*/
    public void ReceiveDataPackage(View v){
        /*修改为正在连接*/
        binding.receiverBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_connecting_hint));
        /*开启服务器*/
        App.blueToothHelper.startServer(context);
        /*接受回调*/
        App.blueToothHelper.mHandler.setBlueToothAcceptListener(new BlueToothHelper.BlueToothHandler.BluetoothAcceptListener() {
            @Override
            public void onConnectSuccess() {
                dataSyncViewModel.setCurrentStatue(BluetoothStatue.RECEIVING);
                binding.receiverBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_receiving_hint));
            }

            @Override
            public void onDataReceived(DataPackage dataPackage) {
                dataSyncViewModel.setCurrentStatue(BluetoothStatue.RECEIVED);
                binding.receiverBtnText.setText(dataSyncViewModel.context.getString(R.string.datasync_received_hint));
                ToastHelper.showToast(context, dataPackage.toString(), Toast.LENGTH_SHORT);
                App.dataBaseHelper.saveDataPackage(dataPackage);
                /*重启App*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DeviceHelper.restartApp(context);
                    }
                }, Constants.delayDuration);
            }
        });
    }


}
