package com.example.hoitnote.views.analysis;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.RelativeGuide;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentChartPcBinding;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
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
        chartAnalysisManager.setPcIcon(binding.circleChartBackBtn);
        chartAnalysisManager.setPcNoDataHint(binding.circleChartNotHaveDataTextView);
        chartAnalysisManager.setRecyclerViewShowScreen(binding.filterTabs);
        chartAnalysisManager.actImageViewPC();

        ArrayList<String> markScreenList = new ArrayList<>(
                Arrays.asList(Constants.tallyTableColumn_c1, Constants.tallyTableColumn_c2));
        /*初始化PC*/
        ArrayList<TallyAnalysisPC> tallyAnalysisPCs = ChartAnalysisManager.analyseTalliesPC(contentValues, markScreenList);
        chartAnalysisManager.setTallyAnalysisPCListPC(tallyAnalysisPCs, markScreenList);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if(chartAnalysisManager.isIfHaveData()){
            App.newbieGuideBuilder
                    .setLabel("guide_pc")
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.circleChart, new RelativeGuide(R.layout.guide_cam_chart_pc_chart,
                                    Gravity.BOTTOM, 20)))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.legendsList, new RelativeGuide(R.layout.guide_cam_chart_pc_legend,
                                    Gravity.TOP, 20)))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.filterTabs, new RelativeGuide(R.layout.guide_cam_chart_pc_now_screen,
                                    Gravity.BOTTOM, 20)))
                    .show();
        }
    }

    public FragmentChartPcBinding getBinding() {
        return binding;
    }
}
