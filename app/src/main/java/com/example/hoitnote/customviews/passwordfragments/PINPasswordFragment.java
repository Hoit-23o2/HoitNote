package com.example.hoitnote.customviews.passwordfragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentPinPasswordBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;
import com.example.hoitnote.views.settings.PasswordSettingActivity;

import java.util.List;

public class PINPasswordFragment extends BasePasswordFragment {
    FragmentPinPasswordBinding binding;

    public PINPasswordFragment(){
        super();
    }

    public PINPasswordFragment(BaseLockViewModel baseLockViewModel, Context context, Config config){
        super(baseLockViewModel, context, config);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pin_password,
                container,
                false
        ) ;
        initFrame();
        return  binding.getRoot();
    }

    private boolean isComeFromSetting = false;
    @Override
    public void initFrame(){
        binding.setPinPasswordFragmentViewModel(baseLockViewModel);
        binding.setPinPasswordFragment(this);
        binding.patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.d(getClass().getName(), "Pattern drawing started");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                Log.d(getClass().getName(), "Pattern progress: " +
                        PatternLockUtils.patternToString(binding.patternLockView, progressPattern));
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String password = PatternLockUtils.patternToString(binding.patternLockView, pattern);
                Log.d(getClass().getName(), "Pattern complete: " + password);
                if(password.length() < 4){
                    ToastHelper.showToast(context, Constants.passwordNotEnough, Toast.LENGTH_SHORT);
                }
                else {
                    /*登录逻辑*/
                    if(baseLockViewModel.getLockViewType() == LockViewType.LOGIN){
                        if(!password.equals(config.getPassword())){
                            wrongCount++;
                            binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                            clearPattern();
                            int remainWrongTimes = Constants.totalWrongCount - wrongCount;
                            triggerWrongTips(remainWrongTimes);
                        }
                        else{
                            binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                            NavigationHelper.navigationClosedCurrentActivity(getActivity(),
                                    MainActivity.class);
                        }
                    }
                    /*注册逻辑*/
                    else if(baseLockViewModel.getLockViewType() == LockViewType.REGISTRATION){
                        Config newConfig = new Config(ThemeHelper.getCurrentTheme(context),
                                password, PasswordStyle.PIN);
                        boolean isSaved = App.dataBaseHelper.saveConfig(newConfig, null);
                        /*不是来自Setting的注册*/
                        if(!isComeFromSetting){
                            if(isSaved){
                                ToastHelper.showToast(context,Constants.registrationSuccess,Toast.LENGTH_SHORT);
                                NavigationHelper.closeCurrentActivity(context);
                            }
                            else{
                                ToastHelper.showToast(context,Constants.registrationFalse,Toast.LENGTH_LONG);
                                clearPattern();
                            }
                        }
                        /*是来自Setting的注册*/
                        else{
                            if(isSaved){
                                ToastHelper.showToast(context,Constants.settingSuccess,Toast.LENGTH_SHORT);
                                NavigationHelper.closeCurrentActivity(context);
                            }
                            else{
                                ToastHelper.showToast(context,Constants.settingFalse,Toast.LENGTH_SHORT);
                                clearPattern();
                            }
                        }
                    }

                    /*设置-重设*/
                    else if(baseLockViewModel.getLockViewType() == LockViewType.SETTING){
                        /*第一次输入密码错误*/
                        if(!password.equals(config.getPassword())){
                            ToastHelper.showToast(context, Constants.passwordWrong, Toast.LENGTH_SHORT);
                        }
                        /*输入密码正确*/
                        else{
                            baseLockViewModel.setTitle(Constants.patternSettingTip2);
                            baseLockViewModel.setBtnText(Constants.settingBtnText);
                            baseLockViewModel.setPassword("");
                            baseLockViewModel.setLockViewType(LockViewType.REGISTRATION);
                            binding.setPinPasswordFragmentViewModel(baseLockViewModel);
                            clearPattern();
                            isComeFromSetting = true;
                        }
                    }
                }


            }

            @Override
            public void onCleared() {
                Log.d(getClass().getName(), "Pattern has been cleared");
            }
        });


    }
    private void clearPattern(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                binding.patternLockView.clearPattern();
            }
        }, Constants.delayDuration / 2);
    }
}
