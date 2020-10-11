package com.example.hoitnote.customviews.passwordfragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.hoitnote.MainActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentFingerprintPasswordBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.LockViewType;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.BaseLockViewModel;

import java.util.concurrent.Executor;

public class FingerprintPasswordFragment extends BasePasswordFragment {
    FragmentFingerprintPasswordBinding binding;
    BiometricPrompt.PromptInfo promptInfo;
    BiometricPrompt biometricPrompt;

    public FingerprintPasswordFragment(){
        super();
    }

    public FingerprintPasswordFragment(BaseLockViewModel baseLockViewModel, Context context, Config config){
        super(baseLockViewModel, context, config);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_fingerprint_password,
                container,
                false
        ) ;
        initFrame();
        return  binding.getRoot();
    }

    @Override
    public void initFrame() {
        binding.setFingerprintPasswordFragmentViewModel(baseLockViewModel);
        binding.setFingerprintPasswordFragment(this);
        binding.fingerprintAnim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                biometricPrompt.authenticate(promptInfo);
                binding.fingerprintAnim.setProgress(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Executor executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                ToastHelper.showToast(context,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT);
                wrongCount++;

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        biometricPrompt.cancelAuthentication();
                    }
                }, Constants.delayDuration);

                int remainWrongTimes = Constants.totalWrongCount - wrongCount;
                triggerWrongTips(remainWrongTimes);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                ToastHelper.showToast(context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT);

                NavigationHelper.navigationClosedCurrentActivity(getActivity(),
                        MainActivity.class);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                ToastHelper.showToast(context, "Authentication failed",
                        Toast.LENGTH_SHORT);
                biometricPrompt.cancelAuthentication();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
    }

    public void btnClick(View v){
        /*登录*/
        if(baseLockViewModel.getLockViewType() == LockViewType.LOGIN){
            binding.fingerprintAnim.playAnimation();
        }
        /*注册*/
        else if(baseLockViewModel.getLockViewType() == LockViewType.REGISTRATION){

        }

    }
}
