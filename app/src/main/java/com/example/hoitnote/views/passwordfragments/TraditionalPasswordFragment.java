package com.example.hoitnote.views.passwordfragments;

import android.content.Context;
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

import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentTraditionalPasswordBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.helpers.KeyboardHelper;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;
import com.example.hoitnote.views.settings.PasswordSettingActivity;

public class TraditionalPasswordFragment extends BasePasswordFragment {

    private FragmentTraditionalPasswordBinding binding;


    public TraditionalPasswordFragment(){
        super();
    }

    public TraditionalPasswordFragment(BaseLockViewModel baseLockViewModel, Context context, Config config){
        super(baseLockViewModel, context, config);
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
        initFrame();
        return binding.getRoot();
    }

    @Override
    public void initFrame() {
        binding.setTraditionalPasswordFragmentViewModel(baseLockViewModel);
        binding.setTraditionalPasswordFragment(this);

        binding.realCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int beforeLength, int currentLength) {
                switch (charSequence.toString().length()){
                    case 0:
                        binding.hoitPasswordFrame1.hidePassword();
                        binding.hoitPasswordFrame2.hidePassword();
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 1:
                        binding.hoitPasswordFrame1.showPassword(charSequence.charAt(0));
                        binding.hoitPasswordFrame2.hidePassword();
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 2:
                        binding.hoitPasswordFrame1.showPassword(charSequence.charAt(0));
                        binding.hoitPasswordFrame2.showPassword(charSequence.charAt(1));
                        binding.hoitPasswordFrame3.hidePassword();
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 3:
                        binding.hoitPasswordFrame1.showPassword(charSequence.charAt(0));
                        binding.hoitPasswordFrame2.showPassword(charSequence.charAt(1));
                        binding.hoitPasswordFrame3.showPassword(charSequence.charAt(2));
                        binding.hoitPasswordFrame4.hidePassword();
                        break;
                    case 4:
                        binding.hoitPasswordFrame1.showPassword(charSequence.charAt(0));
                        binding.hoitPasswordFrame2.showPassword(charSequence.charAt(1));
                        binding.hoitPasswordFrame3.showPassword(charSequence.charAt(2));
                        binding.hoitPasswordFrame4.showPassword(charSequence.charAt(3));
                        break;
                    default:
                        if(getActivity() != null){
                            KeyboardHelper.closeKeyboard(context, getActivity());
                        }
                        break;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getFocus(View view) {
        binding.realCodeInput.requestFocus();
        KeyboardHelper.showKeyboard(context);
    }

    private boolean isComeFromSetting = false;
    public void btnClick(View view) {

        String password = baseLockViewModel.getPassword();
        //ToastHelper.showToast(context,password,Toast.LENGTH_LONG);
        if(password.length() < 4){
            ToastHelper.showToast(context, Constants.passwordNotEnough, Toast.LENGTH_SHORT);
        }
        else {
            password = password.substring(0,4);
            /*登录*/
            if(baseLockViewModel.getLockViewType() == LockViewType.LOGIN){
                if(!password.equals(config.getPassword())){
                    wrongCount++;
                    int remainWrongTimes = Constants.totalWrongCount - wrongCount;
                    triggerWrongTips(remainWrongTimes);
                }
                else{
                    NavigationHelper.navigationClosedCurrentActivity(getActivity(),
                            MainActivity.class);
                }
            }
            /*注册*/
            else if(baseLockViewModel.getLockViewType() == LockViewType.REGISTRATION){
                Config newConfig = new Config(ThemeHelper.getCurrentTheme(context),
                        password, PasswordStyle.TRADITIONAL);
                boolean isSaved = App.dataBaseHelper.saveConfig(newConfig);
                /*不是来自Setting的注册*/
                if(!isComeFromSetting){
                    if(isSaved){
                        ToastHelper.showToast(context,Constants.registrationSuccess,Toast.LENGTH_SHORT);
                        NavigationHelper.navigationClosedCurrentActivity(context, MainActivity.class);
                    }
                    else{
                        ToastHelper.showToast(context,Constants.registrationFalse,Toast.LENGTH_LONG);
                    }
                }
                /*是来自Setting的注册*/
                else{
                    if(isSaved){
                        ToastHelper.showToast(context,Constants.settingSuccess,Toast.LENGTH_SHORT);
                        NavigationHelper.navigationClosedCurrentActivity(context, PasswordSettingActivity.class);
                    }
                    else{
                        ToastHelper.showToast(context,Constants.settingFalse,Toast.LENGTH_SHORT);
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
                    baseLockViewModel.setTitle(Constants.traditionalSettingTip2);
                    baseLockViewModel.setBtnText(Constants.settingBtnText);
                    baseLockViewModel.setPassword("");
                    baseLockViewModel.setLockViewType(LockViewType.REGISTRATION);
                    binding.setTraditionalPasswordFragmentViewModel(baseLockViewModel);
                    isComeFromSetting = true;
                }
            }
        }

    }

}
