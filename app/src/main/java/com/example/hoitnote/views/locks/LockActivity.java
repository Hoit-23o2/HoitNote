package com.example.hoitnote.views.locks;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityLockBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.helpers.FingerPrintHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;

import java.util.ArrayList;

public class LockActivity extends BaseActivity {
    ActivityLockBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    LockFragment lockFragment;

    Config traditionalConfig = null;
    Config pinConfig = null;
    Config fingerprintConfig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*数据绑定固定写法*/
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lock);
        context = LockActivity.this;

        initUI();

    }


    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        /*
        * pos:0 fingerprint
        * pos:1 pattern
        * pos:2 trditional
        * */
        ArrayList<BaseLockViewModel> baseLockViewModels = new ArrayList<>();

        for (Config config :
                App.configs) {
            if(config.getPasswordStyle() == PasswordStyle.FINGERPRINT){
                this.fingerprintConfig = config;
            }
            else if(config.getPasswordStyle() == PasswordStyle.PIN){
                this.pinConfig = config;
            }
            else {
                this.traditionalConfig = config;
            }
        }

        //初始化3个页面的ViewModel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(FingerPrintHelper.isSupportFingerPrint(context)){
                baseLockViewModels.add(new BaseLockViewModel(
                        LockViewType.LOGIN, Constants.fingerprintLock
                        ,Constants.loginBtnText));
            }
            else{
                //无指纹通知
                baseLockViewModels.add(
                        new BaseLockViewModel(
                                LockViewType.REGISTRATION,
                                Constants.fingerprintRegistration,Constants.registrationBtnText));
            }
        }

        baseLockViewModels.add(this.pinConfig != null ?
                new BaseLockViewModel(LockViewType.LOGIN,Constants.patternLock
                        ,Constants.loginBtnText)
                : new BaseLockViewModel(LockViewType.REGISTRATION,Constants.patternRegistration,
                Constants.registrationBtnText));
        baseLockViewModels.add(this.traditionalConfig != null ?
                new BaseLockViewModel(LockViewType.LOGIN,Constants.traditionalLock
                        ,Constants.loginBtnText)
                : new BaseLockViewModel(LockViewType.REGISTRATION,Constants.traditionalRegistration,
                Constants.registrationBtnText));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        lockFragment = new LockFragment(
                fragmentManager,
                getLifecycle(),
                baseLockViewModels,
                context,
                pinConfig,
                traditionalConfig,
                fingerprintConfig
        );
        fragmentTransaction.add(binding.mainContainer.getId(), lockFragment).commit();
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