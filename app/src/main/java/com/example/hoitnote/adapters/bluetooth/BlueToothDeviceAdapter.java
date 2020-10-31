package com.example.hoitnote.adapters.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ItemDeviceBinding;
import com.example.hoitnote.viewmodels.BlueToothViewModel;

import java.util.ArrayList;

public class BlueToothDeviceAdapter extends BaseAdapter {
    ItemDeviceBinding binding;
    Context context;
    ArrayList<BlueToothViewModel> bluetoothViewModels;

    public BlueToothDeviceAdapter(Context context, ArrayList<BlueToothViewModel> bluetoothViewModels){
        this.context = context;
        this.bluetoothViewModels = bluetoothViewModels;
    }

    public void addBluetoothCollection(ArrayList<BlueToothViewModel> _blueToothViewModels){
        bluetoothViewModels.addAll(_blueToothViewModels);
        notifyDataSetChanged();
    }

    public ArrayList<BlueToothViewModel> getBluetoothViewModels(){
        return this.bluetoothViewModels;
    }

    @Override
    public int getCount() {
        return bluetoothViewModels.size();
    }

    @Override
    public Object getItem(int i) {
        return bluetoothViewModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device,null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }
        else{
            binding = (ItemDeviceBinding) convertView.getTag();
        }
        binding.setBluetoothViewModel(bluetoothViewModels.get(pos));
        return binding.getRoot();
    }
}
