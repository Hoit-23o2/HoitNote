package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.databinding.Bindable;

import com.example.hoitnote.BR;
import com.example.hoitnote.customviews.charts.HoitNoteClView;
import com.example.hoitnote.utils.enums.ChartClTimeSegmentType;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;

public class AnalysisViewModel extends BaseViewModel {
    ChartAnalysisManager chartAnalysisManager;
    private String incomes;
    private String outcomes;
    private String remains;

    public AnalysisViewModel(Context context, ChartAnalysisManager chartAnalysisManager) {
        super(context);
        this.chartAnalysisManager = chartAnalysisManager;
    }

    @Bindable
    public String getIncomes() {
        return incomes;
    }

    public void setIncomes(String incomes) {
        this.incomes = incomes;
        notifyPropertyChanged(BR.incomes);
    }

    @Bindable
    public String getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
        notifyPropertyChanged(BR.outcomes);
    }

    @Bindable
    public String getRemains() {
        return remains;
    }

    public void setRemains(String remains) {
        this.remains = remains;
        notifyPropertyChanged(BR.remains);
    }
}
