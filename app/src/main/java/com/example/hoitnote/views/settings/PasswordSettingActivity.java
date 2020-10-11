package com.example.hoitnote.views.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.SplashActivity;
import com.example.hoitnote.databinding.ActivityPasswordSettingBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.views.locks.LockCountDownActivity;

public class PasswordSettingActivity extends BaseActivity {
    ActivityPasswordSettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_password_setting);
        initActivity();
    }

    private void initActivity() {
        showBackButton();
        binding.passwordStylePanel.setVisibility(View.GONE);
        binding.passwordStylePanel.setTranslationY(1000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                binding.passwordStylePanel.setVisibility(View.VISIBLE);
                binding.passwordStylePanel.animate().translationY(0).start();
            }
        }, Constants.delayDuration / 2);
    }

    public void setFingerPrint(View view) {
        ToastHelper.showToast(context, Constants.fingerprintSetting, Toast.LENGTH_SHORT);
    }

    public void setPattern(View view) {
        Intent intent = new Intent(context, PasswordSettingDetailActivity.class);
        intent.putExtra(Constants.passwordStatue, PasswordStyle.PIN);
        context.startActivity(intent);
    }

    public void setTraditional(View view) {
        Intent intent = new Intent(context, PasswordSettingDetailActivity.class);
        intent.putExtra(Constants.passwordStatue, PasswordStyle.TRADITIONAL);
        context.startActivity(intent);
    }



}