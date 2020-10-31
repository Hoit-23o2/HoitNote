package com.example.hoitnote.views.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivitySettingsBinding;
import com.example.hoitnote.databinding.ActivitySyncSettingBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.viewmodels.DataSyncViewModel;
import com.example.hoitnote.views.bluetooth.DataSyncFragment;

public class SyncSettingActivity extends BaseActivity {
    ActivitySyncSettingBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync_setting);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        DataSyncViewModel dataSyncViewModel = new DataSyncViewModel(context, LockViewType.SETTING);
        DataSyncFragment dataSyncFragment = new DataSyncFragment(context, dataSyncViewModel);

        fragmentTransaction.add(binding.mainContainer.getId(), dataSyncFragment).commit();

        initActivity();
    }

    private void initActivity() {
        showBackButton();
        ThemeHelper.changeColorOfNavigationBar(this,
                ThemeHelper.getPrimaryLightColor(context));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.blueToothHelper.cancel();
    }
}