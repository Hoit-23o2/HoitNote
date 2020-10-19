package com.example.hoitnote.views.analysis;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ActivityAnalysisBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.viewmodels.AnalysisViewModel;

import java.util.ArrayList;

public class AnalysisActivity extends BaseActivity {
    ActivityAnalysisBinding binding;
    Account account;
    AnalysisViewModel analysisViewModel;
    AnalysisFragment analysisFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ChartAnalysisManager chartAnalysisManager;
    ArrayList<ContentValues> contentValuesArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_analysis);
        account = (Account)NavigationHelper.getNavigationParameter(this, Constants.analysisParamTag);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        chartAnalysisManager = new ChartAnalysisManager(context);
        analysisViewModel = new AnalysisViewModel(context, chartAnalysisManager);

        ArrayList<Fragment> fragments = initFragments();

        analysisFragment = new AnalysisFragment(fragmentManager,getLifecycle(),
                analysisViewModel,context, fragments);

        fragmentTransaction.add(binding.mainContainer.getId(), analysisFragment).commit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /*调用统计图*/
            /*
            chartAnalysisManager.setListViewPC(binding.legends);

            chartAnalysisManager.setHoitNotePCView(binding.circleChart);
            chartAnalysisManager.actImageViewPC();

            chartAnalysisManager.setHoitNoteCLView(binding.linearChart);
            chartAnalysisManager.actImageViewCl();


            ArrayList<ContentValues> contentValuesArrayList = ChartAnalysisManager.getContentValuesTallies(
                    App.dataBaseHelper.getTallies(null)
            );

            final ArrayList<TallyAnalysisPC> tallyAnalysisPCs = ChartAnalysisManager.analyseTalliesPC(contentValuesArrayList, new ArrayList<String>(
                    Arrays.asList(Constants.tallyTableColumn_c1, Constants.tallyTableColumn_c2)
            ));
            final ArrayList<TallyAnalysisCl> tallyAnalysisCls = ChartAnalysisManager.analyseTalliesCl(contentValuesArrayList);

            chartAnalysisManager.setTallyAnalysisPCListPC(tallyAnalysisPCs);
            chartAnalysisManager.setTallyAnalysisClListCl(tallyAnalysisCls);*/

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Fragment> initFragments(){
        ArrayList<ContentValues> contentValuesArrayList = ChartAnalysisManager.getContentValuesTallies(
                App.dataBaseHelper.getTallies(new DataBaseFilter(
                        null,
                        null,
                        DataBaseFilter.IDInvalid,
                        null,
                        null,
                        null
                ))
        );
        ArrayList<Fragment> fragments = new ArrayList<>();
        AccountCardViewModel accountCardViewModel = account.parseToAccountCardViewModel(context, ClickType.LONG_TAP);
        InformationFragment informationFragment = new InformationFragment(accountCardViewModel
                ,fragmentManager, fragmentTransaction);
        ChartPCFragment chartPCFragment = new ChartPCFragment(analysisViewModel,
                chartAnalysisManager,contentValuesArrayList);
        ChartClFragment chartClFragment = new ChartClFragment(analysisViewModel,
                chartAnalysisManager,contentValuesArrayList);
        fragments.add(informationFragment);
        fragments.add(chartPCFragment);
        fragments.add(chartClFragment);
        return fragments;
    }



}