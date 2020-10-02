package com.example.hoitnote.views.locks;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.viewmodels.LockViewModel;

public class BasePasswordFragment extends Fragment {
    public LockViewModel lockViewModel;
    public Context context;
    public Config config;
    public int wrongCount = 0;

    public BasePasswordFragment(){

    }

    public BasePasswordFragment(LockViewModel lockViewModel,Context context, Config config){
        this.context = context;
        this.config = config;
        this.lockViewModel = lockViewModel;
    }

}
