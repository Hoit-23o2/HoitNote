package com.example.hoitnote.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityLockBinding;
import com.example.hoitnote.viewmodels.LockViewModel;

public class LockActivity extends AppCompatActivity {
    ActivityLockBinding binding;
    LockViewModel lockViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*数据绑定固定写法*/
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lock);
        lockViewModel = new LockViewModel();
        binding.setLockViewModel(lockViewModel);

        initUI();

    }

    private void initUI() {
        binding.realCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}