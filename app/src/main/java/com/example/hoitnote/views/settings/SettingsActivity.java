package com.example.hoitnote.views.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivitySettingsBinding;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.wuyr.rippleanimation.RippleAnimation;

public class SettingsActivity extends BaseActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);
        context = SettingsActivity.this;
        initActivity();
    }

    private void initActivity() {
        showBackButton();
    }

    public void goThemeSettingActivity(View view) {
        NavigationHelper.navigationWithTransitionAnim(context,binding.themeIcon
                ,"themeIcon",ThemeSettingActivity.class);
    }

    public void goPasswordSettingActivity(View view) {
        NavigationHelper.navigationWithTransitionAnim(context,binding.securityIcon
                ,"securityIcon",PasswordSettingActivity.class);
    }

    public void goSyncActivity(View view) {
        NavigationHelper.navigationNormally(context, SyncSettingActivity.class);
    }
}