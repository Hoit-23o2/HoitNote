package com.example.hoitnote.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.os.Bundle;
import android.view.View;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivitySampleBinding;
import com.example.hoitnote.models.Sample;
import com.example.hoitnote.viewmodels.SampleViewModel;

public class SampleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivitySampleBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_sample);
        final SampleViewModel vm = new SampleViewModel(context);
        binding.setVm(vm);
        final int[] i = {0};
        binding.sample1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i[0]++;
                vm.setSample(new Sample("Hello"+ i[0]));
            }
        });

        binding.sample2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i[0]++;
                vm.sample2.set(new Sample("Hello"+ i[0]));
            }
        });
    }
}