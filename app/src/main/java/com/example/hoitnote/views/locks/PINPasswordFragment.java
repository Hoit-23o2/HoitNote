package com.example.hoitnote.views.locks;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentPinPasswordBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.PasswordStatueHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.LockViewModel;

import java.util.List;

public class PINPasswordFragment extends BasePasswordFragment {
    FragmentPinPasswordBinding binding;

    public PINPasswordFragment(){
        super();
    }

    public PINPasswordFragment( LockViewModel lockViewModel,Context context, Config config){
        super(lockViewModel, context, config);
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
        binding.setLockViewModel(lockViewModel);
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
                if(!password.equals(config.getPassword())){
                    wrongCount++;

                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            binding.patternLockView.clearPattern();
                        }
                    }, Constants.delayDuration);

                    int remainWrongTimes = Constants.totalWrongCount - wrongCount;
                    if(remainWrongTimes < 0){
                        wrongCount = 0;
                        PasswordStatueHelper.setPasswordSatueTime(context, Constants.notPassTime);
                        Intent intent = new Intent(getActivity(), LockCountDownActivity.class);
                        intent.putExtra(Constants.currentPasswordStatue, Constants.notPassTime);
                        startActivity(intent);
                        if(getActivity() != null){
                            getActivity().finish();
                        }
                    }
                    else{
                        ToastHelper.showToast(context,Constants.passwordWrong, Toast.LENGTH_SHORT);
                        ToastHelper.showToast(context,Constants.passwordWrongTips+
                                        remainWrongTimes,
                                Toast.LENGTH_SHORT);
                    }
                }
                else{
                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    if(getActivity() != null){
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onCleared() {
                Log.d(getClass().getName(), "Pattern has been cleared");
            }
        });
        return  binding.getRoot();
    }
}
