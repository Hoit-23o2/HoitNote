package com.example.hoitnote.views.analysis;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
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
import com.example.hoitnote.databinding.FragmentChartClBinding;
import com.example.hoitnote.databinding.FragmentChartPcBinding;
import com.example.hoitnote.models.charts.TallyAnalysisCl;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
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
}
