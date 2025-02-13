package com.example.hoitnote.views.settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityThemeSettingBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.Theme;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.wuyr.rippleanimation.RippleAnimation;

public class ThemeSettingActivity extends BaseActivity {
    ActivityThemeSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme_setting);
        initActivity();
    }

    private void initActivity() {
        showBackButton();
    }


    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeTheme(View view) {
        RippleAnimation.create(view).setDuration(Constants.animDelayDuration).start();
        int colorPrimary = 0;
        int colorAccent = 0;
        switch (view.getId()){
            case R.id.defualtThemeBtn:
                // Parse MyCustomStyle, using Context.obtainStyledAttributes()
                colorPrimary = Color.parseColor(Constants.defaultColorPrimary);
                colorAccent = Color.parseColor(Constants.defaultColorAccent);
                ThemeHelper.changeColorOfNavigationBar(this, Color.parseColor(Constants.defaultColorPrimary));
                ThemeHelper.setCurrentTheme(context, Theme.DEFAULT);
                break;
            case R.id.sweetThemeBtn:
                colorPrimary = Color.parseColor(Constants.sweetColorPrimary);
                colorAccent = Color.parseColor(Constants.sweetColorAccent);
                ThemeHelper.changeColorOfNavigationBar(this, Color.parseColor(Constants.sweetColorPrimary));
                ThemeHelper.setCurrentTheme(context, Theme.SWEET);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

        binding.upperContainer.setBackgroundColor(colorAccent);
        binding.displayContainer.setBackgroundColor(colorPrimary);
        boolean isSaved =  App.dataBaseHelper.saveConfig(null,
                ThemeHelper.getCurrentTheme(context));
        ToastHelper.showToast(context,String.valueOf(isSaved), Toast.LENGTH_SHORT);
        /*除本页外全部添加Flag*/
        for(int i = 0; i < NavigationHelper.navigationStack.size() - 1; i++){
            NavigationHelper.navigationStack.get(i).addThemeChangedFlag();
        }

    }
}