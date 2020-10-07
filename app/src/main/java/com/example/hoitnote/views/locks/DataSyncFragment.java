package com.example.hoitnote.views.locks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentDatasyncBinding;

public class DataSyncFragment extends Fragment {
    FragmentDatasyncBinding binding;

    public DataSyncFragment(){

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
}
