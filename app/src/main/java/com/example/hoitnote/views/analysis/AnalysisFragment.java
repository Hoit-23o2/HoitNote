package com.example.hoitnote.views.analysis;

import android.content.Context;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.example.hoitnote.R;
import com.example.hoitnote.adapters.analysis.AnalysisFragmentAdapter;
import com.example.hoitnote.adapters.locks.LockFragmentAdapter;
import com.example.hoitnote.databinding.FragmentAnalysisBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.viewmodels.AnalysisViewModel;
import com.example.hoitnote.viewmodels.BaseLockViewModel;

import java.util.ArrayList;

public class AnalysisFragment extends Fragment {
    public FragmentManager fragmentManager;
    public Lifecycle lifecycle;
    public AnalysisViewModel analysisViewModel;
    public Context context;
    FragmentAnalysisBinding binding;
    ArrayList<Fragment> fragments;

    public AnalysisFragment(ArrayList<Fragment> fragments){
        this.fragments = fragments;
    }

    public AnalysisFragment(@NonNull FragmentManager fragmentManager,
                        @NonNull Lifecycle lifecycle,
                        AnalysisViewModel analysisViewModel,
                        Context context,ArrayList<Fragment> fragments){
        this.fragmentManager = fragmentManager;
        this.lifecycle = lifecycle;
        this.analysisViewModel = analysisViewModel;
        this.context = context;
        this.fragments = fragments;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_analysis,
                container,
                false
        );
        binding.analysisContainer.setAdapter(new AnalysisFragmentAdapter(this, this.fragments));
        return binding.getRoot();
    }
}
