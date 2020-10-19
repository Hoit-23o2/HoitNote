package com.example.hoitnote.customviews.passwordfragments;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.PasswordStatueHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;
import com.example.hoitnote.views.locks.LockCountDownActivity;

public class BasePasswordFragment extends Fragment {
    public BaseLockViewModel baseLockViewModel;
    public Context context;
    public Config config;
    public int wrongCount = 0;

    public BasePasswordFragment(){

    }

    public BasePasswordFragment(BaseLockViewModel baseLockViewModel,Context context, Config config){
        this.context = context;
        this.config = config;
        this.baseLockViewModel = baseLockViewModel;
    }

    /*
    * 当错误次数小于0时，进入
    * */
    public void triggerWrongTips(int remainWrongTimes){
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

    public void initFrame(){

    }
}
