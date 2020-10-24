package com.example.hoitnote.adapters.analysis;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.hoitnote.R;
import com.example.hoitnote.viewmodels.AnalysisViewModel;
import com.example.hoitnote.views.analysis.ChartClFragment;
import com.example.hoitnote.views.analysis.ChartPCFragment;
import com.example.hoitnote.views.analysis.InformationFragment;

import java.util.ArrayList;

public class AnalysisFragmentAdapter extends FragmentStateAdapter {

    AnalysisViewModel analysisViewModel;
    ArrayList<Fragment> fragments = new ArrayList<>();

    public AnalysisFragmentAdapter(@NonNull Fragment fragment,
    ArrayList<Fragment> fragments) {
        super(fragment);
        this.fragments = fragments;

    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
