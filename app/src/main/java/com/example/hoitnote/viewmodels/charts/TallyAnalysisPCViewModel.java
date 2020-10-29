package com.example.hoitnote.viewmodels.charts;

import android.content.Context;

import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.viewmodels.BaseViewModel;

public class TallyAnalysisPCViewModel extends BaseViewModel {
    private TallyAnalysisPC tallyAnalysisPC;


    public TallyAnalysisPCViewModel(Context context) {
        super(context);
    }


    public TallyAnalysisPC getTallyAnalysisPC() {
        return tallyAnalysisPC;
    }

    public void setTallyAnalysisPC(TallyAnalysisPC tallyAnalysisPC) {
        this.tallyAnalysisPC = tallyAnalysisPC;
    }
}
