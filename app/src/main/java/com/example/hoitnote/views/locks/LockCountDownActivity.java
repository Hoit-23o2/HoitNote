package com.example.hoitnote.views.locks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.SplashActivity;
import com.example.hoitnote.databinding.ActivityLockCountDownBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.PasswordStatueHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.LockCountDownViewModel;

public class LockCountDownActivity extends BaseActivity {
    ActivityLockCountDownBinding binding;
    LockCountDownViewModel lockCountDownViewModel;
    Context context;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lock_count_down);
        lockCountDownViewModel = new LockCountDownViewModel();
        binding.setLockCountDownViewModel(lockCountDownViewModel);
        context = LockCountDownActivity.this;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int time = bundle.getInt(Constants.currentPasswordStatue);
            ToastHelper.showToast(context,String.valueOf(time), Toast.LENGTH_SHORT);
            startCountDown(time);
        }

    }

    /*
    * 开始计时
    * */
    private void startCountDown(int startTime){
        countDownTimer = new CountDownTimer(startTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                PasswordStatueHelper.setPasswordSatueTime(context, (int) millisUntilFinished / 1000);
                ToastHelper.showToast(context,millisUntilFinished+"left",Toast.LENGTH_SHORT);
            }

            public void onFinish() {
                ToastHelper.showToast(context,"计时完毕",Toast.LENGTH_SHORT);
                PasswordStatueHelper.setPasswordSatueTime(context, 0);
                /*开启解锁登录Activity*/
                /*Intent intent = new Intent(context, LockActivity.class);
                LockCountDownActivity.this.startActivity(intent);
                LockCountDownActivity.this.finish();*/

                NavigationHelper.navigationClosedCurrentActivity(LockCountDownActivity.this,
                        LockActivity.class);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}