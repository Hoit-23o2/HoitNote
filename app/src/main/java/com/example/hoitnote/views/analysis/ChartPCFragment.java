package com.example.hoitnote.views.analysis;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.R;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.databinding.FragmentChartPcBinding;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;
import com.example.hoitnote.viewmodels.AnalysisViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartPCFragment extends Fragment {
    FragmentChartPcBinding binding;
    AnalysisViewModel analysisViewModel;
    ChartAnalysisManager chartAnalysisManager;
    ArrayList<ContentValues> contentValues;
    public ChartPCFragment(AnalysisViewModel analysisViewModel,
                           ChartAnalysisManager chartAnalysisManager,
                           ArrayList<ContentValues> contentValues){
        this.analysisViewModel = analysisViewModel;
        this.chartAnalysisManager = chartAnalysisManager;
        this.contentValues = contentValues;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_chart_pc,
                container,
                false
        );
        chartAnalysisManager.setHoitNotePCView(binding.circleChart);
        chartAnalysisManager.setListViewPC(binding.legendsList);
        chartAnalysisManager.actImageViewPC();

        /*初始化PC*/
        ArrayList<TallyAnalysisPC> tallyAnalysisPCs = ChartAnalysisManager.analyseTalliesPC(contentValues, new ArrayList<String>(
                Arrays.asList(Constants.tallyTableColumn_c1, Constants.tallyTableColumn_c2)
        ));
        chartAnalysisManager.setTallyAnalysisPCListPC(tallyAnalysisPCs);

        return binding.getRoot();
    }

}
