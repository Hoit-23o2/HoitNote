package com.example.hoitnote.views.analysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityAnalysisBinding;

public class AnalysisActivity extends AppCompatActivity {
    ActivityAnalysisBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_analysis);

    }
}