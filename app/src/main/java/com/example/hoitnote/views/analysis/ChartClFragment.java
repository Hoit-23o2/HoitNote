package com.example.hoitnote.views.analysis;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
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
import com.example.hoitnote.databinding.FragmentChartClBinding;
import com.example.hoitnote.databinding.FragmentChartPcBinding;
import com.example.hoitnote.models.charts.TallyAnalysisCl;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ChartClTimeSegmentType;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;
import com.example.hoitnote.viewmodels.AnalysisViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ChartClFragment extends Fragment {
    FragmentChartClBinding binding;
    AnalysisViewModel analysisViewModel;
    ChartAnalysisManager chartAnalysisManager;
    ArrayList<ContentValues> contentValues;
    ArrayList<View> filterTabs = new ArrayList<>();

    public ChartClFragment(AnalysisViewModel analysisViewModel,
                           ChartAnalysisManager chartAnalysisManager,
                           ArrayList<ContentValues> contentValues){
        this.analysisViewModel = analysisViewModel;
        this.chartAnalysisManager = chartAnalysisManager;
        this.contentValues = contentValues;
    }

    public FragmentChartClBinding getBinding() {
        return binding;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_chart_cl,
                container,
                false
        );
        binding.setAnalysisViewModel(analysisViewModel);
        binding.setChartClFragment(this);

        chartAnalysisManager.setHoitNoteCLView(binding.linearChart);
        chartAnalysisManager.setListViewCl(null, binding.legendsList);
        chartAnalysisManager.actImageViewCl();

        filterTabs.add(binding.yearFilter);
        filterTabs.add(binding.monthFilter);
        filterTabs.add(binding.weekFilter);
        filterTabs.add(binding.dayFilter);

        /*初始化CL*/
        ArrayList<TallyAnalysisCl> tallyAnalysisCls = ChartAnalysisManager.analyseTalliesCl(contentValues);
        chartAnalysisManager.setTallyAnalysisClListCl(tallyAnalysisCls);

        renderTabs(filterTabs.get(3));

        return binding.getRoot();
    }



    /*修改线性统计图的时间段查看*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeClTimeSegment(View v, ChartClTimeSegmentType chartClTimeSegmentType){
        renderTabs(v);
        String newTimeDivision = "";
        switch (chartClTimeSegmentType){
            case DAY:
                newTimeDivision = "日";
                break;
            case WEEK:
                newTimeDivision = "周";
                break;
            case YEAR:
                newTimeDivision = "年";
                break;
            case MONTH:
                newTimeDivision = "月";
                break;
        }
        chartAnalysisManager.notifyTimeDivision(newTimeDivision);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void renderTabs(View targetTab){
        targetTab.setBackgroundColor(ThemeHelper.getAccentColor(analysisViewModel.context));
        for (View filterTab:
                filterTabs) {
            if(filterTab != targetTab){
                filterTab.setBackgroundColor(ThemeHelper.getPrimaryColor(analysisViewModel.context));
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if(chartAnalysisManager.isIfHaveData()){
            App.newbieGuideBuilder
                    .setLabel("guide_cl")
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.linearChart, new RelativeGuide(R.layout.guide_cam_chart_cl_chart,
                                    Gravity.BOTTOM, 20)))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.legendsList, new RelativeGuide(R.layout.guide_cam_chart_cl_legend,
                                    Gravity.LEFT, 20)))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(binding.filterTabs, new RelativeGuide(R.layout.guide_cam_chart_cl_time_selector,
                                    Gravity.TOP, 20)))
                    .alwaysShow(true)
                    .show();
        }
    }

}
