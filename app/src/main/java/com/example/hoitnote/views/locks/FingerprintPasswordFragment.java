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
import com.example.hoitnote.databinding.FragmentFingerprintPasswordBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.viewmodels.LockViewModel;

public class FingerprintPasswordFragment extends BasePasswordFragment {
    FragmentFingerprintPasswordBinding binding;

    public FingerprintPasswordFragment(){
        super();
    }

    public FingerprintPasswordFragment(LockViewModel lockViewModel,Context context, Config config){
        super(lockViewModel, context, config);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_fingerprint_password,
                container,
                false
        ) ;
        binding.setLockViewModel(lockViewModel);
        binding.setFingerprintPasswordFragment(this);
        return  binding.getRoot();
    }
}
