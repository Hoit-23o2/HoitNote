package com.example.hoitnote.utils.helpers;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hoitnote.utils.constants.Constants;

import java.util.concurrent.Executor;

public class FingerPrintHelper {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSupportFingerPrint(Context context){
        KeyguardManager keyguardManager = context.getSystemService(KeyguardManager.class);
        BiometricManager biometricManager = BiometricManager.from(context);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            ToastHelper.showToast(context, Constants.fingerPrintNotSupportHardWare, Toast.LENGTH_SHORT);
            return false;
        }
        else if(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED){
            ToastHelper.showToast(context, Constants.fingerPrintNotEnrolled, Toast.LENGTH_SHORT);
            return false;
        }
        else if (keyguardManager != null && !keyguardManager.isKeyguardSecure()) {
            ToastHelper.showToast(context, Constants.fingerPrintNotSupport, Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}
