package com.example.hoitnote.views.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityPasswordSettingBinding;
import com.example.hoitnote.databinding.ActivityPasswordSettingDetailBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;
import com.example.hoitnote.views.passwordfragments.PINPasswordFragment;
import com.example.hoitnote.views.passwordfragments.TraditionalPasswordFragment;

public class PasswordSettingDetailActivity extends BaseActivity {
    ActivityPasswordSettingDetailBinding binding;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Config fingerprintConfig;
    Config pinConfig;
    Config traditionalConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_password_setting_detail);
        initActivity();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        BaseLockViewModel baseLockViewModel = new BaseLockViewModel();
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
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            PasswordStyle passwordStyle = (PasswordStyle)bundle.getSerializable(Constants.passwordStatue);
            if (passwordStyle != null) {
                switch (passwordStyle){
                    case PIN:
                        if(pinConfig == null){
                            baseLockViewModel.setLockViewType(LockViewType.REGISTRATION);
                            baseLockViewModel.setBtnText(Constants.registrationBtnText);
                            baseLockViewModel.setTitle(Constants.patternRegistration);
                        }
                        else{
                            baseLockViewModel.setLockViewType(LockViewType.SETTING);
                            baseLockViewModel.setBtnText(Constants.confirmBtnText);
                            baseLockViewModel.setTitle(Constants.patternSettingTip1);
                        }
                        PINPasswordFragment pinPasswordFragment = new PINPasswordFragment(
                                baseLockViewModel,
                                context,
                                pinConfig
                        );
                        /*开启一项Fragment*/
                        fragmentTransaction.add(binding.container.getId(),
                                pinPasswordFragment).commit();
                        break;
                    case FINGERPRINT:
                        break;
                    case TRADITIONAL:
                        if(traditionalConfig == null){
                            baseLockViewModel.setLockViewType(LockViewType.REGISTRATION);
                            baseLockViewModel.setBtnText(Constants.registrationBtnText);
                            baseLockViewModel.setTitle(Constants.traditionalRegistration);
                        }
                        else{
                            baseLockViewModel.setLockViewType(LockViewType.SETTING);
                            baseLockViewModel.setBtnText(Constants.confirmBtnText);
                            baseLockViewModel.setTitle(Constants.traditionalSettingTip1);
                        }
                        TraditionalPasswordFragment traditionalPasswordFragment = new TraditionalPasswordFragment(
                                baseLockViewModel,
                                context,
                                traditionalConfig
                        );
                        /*开启一项Fragment*/
                        fragmentTransaction.add(binding.container.getId(),
                                traditionalPasswordFragment).commit();
                        break;
                }
            }
        }
    }

    private void initActivity() {
        showBackButton();
    }
}