package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.example.hoitnote.customviews.charts.HoitNoteClView;
import com.example.hoitnote.utils.enums.ChartClTimeSegmentType;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;

public class AnalysisViewModel extends BaseViewModel {
    public ChartAnalysisManager chartAnalysisManager;

    public AnalysisViewModel(Context context, ChartAnalysisManager chartAnalysisManager) {
        super(context);
        this.chartAnalysisManager = chartAnalysisManager;
    }



}
