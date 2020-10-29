package com.example.hoitnote.views.analysis;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.RelativeGuide;
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
        chartAnalysisManager.setNowAccount(account);
        analysisViewModel = new AnalysisViewModel(context, chartAnalysisManager);

        ArrayList<Fragment> fragments = initFragments();

        analysisFragment = new AnalysisFragment(fragmentManager,getLifecycle(),
                analysisViewModel,context, fragments);

        fragmentTransaction.add(binding.mainContainer.getId(), analysisFragment).commit();

        initActivity();
    }

    public void initActivity(){
        showBackButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Fragment> initFragments(){
        ArrayList<ContentValues> contentValuesArrayList = ChartAnalysisManager.getContentValuesTallies(
                App.dataBaseHelper.getTallies(new DataBaseFilter(
                        null,
                        null,
                        DataBaseFilter.IDInvalid,
                        null,
                        account,
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

    MenuItem analysisSelectorItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.analysis_selector_menu, menu);
        analysisSelectorItem = menu.findItem(R.id.analysis_selector);
        analysisSelectorItem.setActionView(R.layout.menu_analysis);
        analysisSelectorItem.getActionView().findViewById(R.id.selectorBtn).
                setOnClickListener(
                        new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                chartAnalysisManager.showDialog();
            }
        });
        hideSelectorItem(false);
        return super.onCreateOptionsMenu(menu);
    }

    public void hideSelectorItem(boolean isAnim){
        if(isAnim)
            analysisSelectorItem.getActionView().animate().alpha(0).start();
        else
            analysisSelectorItem.getActionView().setAlpha(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void displaySelectorItem(){
        analysisSelectorItem.getActionView().animate().alpha(1).start();

        App.newbieGuideBuilder = NewbieGuide.with(AnalysisActivity.this)
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(ChartAnalysisManager.getViewRectF(analysisSelectorItem.getActionView())
                                ,new RelativeGuide(R.layout.guide_cam_chart_filter,
                                Gravity.LEFT,20)));
    }
}