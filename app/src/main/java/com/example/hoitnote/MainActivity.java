package com.example.hoitnote;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.databinding.ActivityMainBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.viewmodels.MainViewModel;

public class MainActivity extends BaseActivity {
    MainViewModel mainViewModel;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel();
        binding.setMainViewModel(mainViewModel);

    }
}