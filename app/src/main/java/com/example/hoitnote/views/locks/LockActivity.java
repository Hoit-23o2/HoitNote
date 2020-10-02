package com.example.hoitnote.views.locks;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityLockBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.viewmodels.LockViewModel;

public class LockActivity extends BaseActivity {
    ActivityLockBinding binding;
    LockViewModel lockViewModel;
    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*数据绑定固定写法*/
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lock);
        lockViewModel = new LockViewModel();
        binding.setLockViewModel(lockViewModel);
        context = LockActivity.this;

        initUI();

    }


    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Config config = App.dataBaseHelper.getConfig();

        if(config == null){
            config = new Config();
            config.setPassword("aaaa");
            config.setPasswordStyle(PasswordStyle.TRADITIONAL);
        }

        PasswordStyle passwordStyle = config.getPasswordStyle();
        if(passwordStyle == PasswordStyle.TRADITIONAL){
            TraditionalPasswordFragment fragment = new TraditionalPasswordFragment(
                    lockViewModel,context,config);
            fragmentTransaction.add(R.id.mainFragmentContainer, fragment).commit();
        }
        else if(passwordStyle == PasswordStyle.FINGERPRINT){
            PINPasswordFragment fragment = new PINPasswordFragment(
                    lockViewModel,context,config);
            fragmentTransaction.add(R.id.mainFragmentContainer,fragment).commit();
        }
        else if(passwordStyle == PasswordStyle.PIN){
            PINPasswordFragment fragment = new PINPasswordFragment(
                    lockViewModel,context,config);
            fragmentTransaction.add(R.id.mainFragmentContainer,fragment).commit();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }
    }


}