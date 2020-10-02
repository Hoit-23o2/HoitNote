package com.example.hoitnote.views.locks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentTraditionalPasswordBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.KeyboardHelper;
import com.example.hoitnote.utils.helpers.PasswordStatueHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.LockViewModel;

public class TraditionalPasswordFragment extends BasePasswordFragment {

    private FragmentTraditionalPasswordBinding binding;


    public TraditionalPasswordFragment(){
        super();
    }

    public TraditionalPasswordFragment(LockViewModel lockViewModel, Context context, Config config){
        super(lockViewModel, context, config);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_traditional_password,
                container,
                false
        );
        binding.setLockViewModel(lockViewModel);
        binding.setTraditionalPasswordFragment(this);
        binding.realCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int beforeLength, int currentLength) {
                switch (currentLength){
                    case 0:
                        binding.hoitPasswordFrame1.hidePassword();
                        binding.hoitPasswordFrame2.hidePassword();
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 1:
                        binding.hoitPasswordFrame1.showPassword();
                        binding.hoitPasswordFrame2.hidePassword();
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 2:
                        binding.hoitPasswordFrame1.showPassword();
                        binding.hoitPasswordFrame2.showPassword();
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 3:
                        binding.hoitPasswordFrame1.showPassword();
                        binding.hoitPasswordFrame2.showPassword();
                        binding.hoitPasswordFrame3.showPassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 4:
                        binding.hoitPasswordFrame1.showPassword();
                        binding.hoitPasswordFrame2.showPassword();
                        binding.hoitPasswordFrame3.showPassword();
                        binding.hoitPasswordFrame4.showPassword();
                        break;
                    default:
                        KeyboardHelper.closeKeyboard(context);
                        break;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return binding.getRoot();
    }

    public void getFocus(View view) {
        binding.realCodeInput.requestFocus();
        KeyboardHelper.showKeyboard(context);
    }

    public void login(View view) {
        wrongCount++;
        String password = lockViewModel.getPassword();
        //ToastHelper.showToast(context,password,Toast.LENGTH_LONG);
        if(password.length() < 4){
            ToastHelper.showToast(context, Constants.passwordNotEnough, Toast.LENGTH_SHORT);
        }
        else {
            password = password.substring(0,4);
            if(!password.equals(config.getPassword())){
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                if(getActivity() != null){
                    getActivity().finish();
                }

            }

        }
    }

}
