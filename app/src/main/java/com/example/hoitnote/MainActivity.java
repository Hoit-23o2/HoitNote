package com.example.hoitnote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hoitnote.adapters.main.AccountCardAdapter;
import com.example.hoitnote.adapters.main.TallyRecentAdapter;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.customviews.popups.AddAccountPopupView;
import com.example.hoitnote.databinding.ActivityMainBinding;
import com.example.hoitnote.databinding.PopupwindowAddaccountBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.GroupType;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.viewmodels.MainViewModel;
import com.example.hoitnote.viewmodels.TallyViewModel;
import com.example.hoitnote.views.flow.HistoryActivity;
import com.example.hoitnote.views.settings.SettingsActivity;
import com.example.hoitnote.views.tallyadd.BookingActivity;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {
    MainViewModel mainViewModel;
    ActivityMainBinding binding;
    AccountCardAdapter accountCardAdapter;
    ArrayList<AccountCardFragment> accountCardFragments = new ArrayList<>();
    AccountCardFragment currentAccountCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel();
        accountCardFragments = mainViewModel.getAccountCardFragments();
        Lifecycle lifecycle = getLifecycle();
        accountCardAdapter = new AccountCardAdapter(getSupportFragmentManager(),lifecycle,
                accountCardFragments);
        initActivity();
    }

    private void initActivity() {
        binding.setMainViewModel(mainViewModel);
        binding.accountCardBanner.setAdapter(accountCardAdapter);
        binding.accountCardBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentAccountCardFragment = accountCardAdapter.getFragment(position);
                if(currentAccountCardFragment.getBinding() != null){
                    AccountCardViewModel currentAccountCardViewModel = currentAccountCardFragment.getBinding().getAccountCardViewModel();
                    ArrayList<TallyViewModel> tallyViewModels = mainViewModel.getRecentTallyViewModels(currentAccountCardFragment);
                    /*分组*/
                    HashMap<String, ArrayList<TallyViewModel>> tallyViewModelWithGroups = mainViewModel.
                            groupTallyViewModel(tallyViewModels, GroupType.DATE);
                    TallyRecentAdapter adapter = new TallyRecentAdapter(context, tallyViewModelWithGroups);
                    binding.recentTalliesContainer.setAdapter(adapter);
                    /*展开所有分组*/
                    adapter.expandAllGroup(binding.recentTalliesContainer);
                    if(currentAccountCardViewModel.getAccount()!=null){
                        ToastHelper.showToast(context,"现在是:"+position+","+currentAccountCardViewModel.getAccount().getAccountName(), Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    public void addAccount(View view) {
        /*Fake card*/
        final AddAccountPopupView popupView = new AddAccountPopupView(view);
        final PopupWindow popupWindow = popupView.showPopupWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupwindowAddaccountBinding binding = popupView.getBinding();
                ToastHelper.showToast(context, binding.accountCodeField.getText().toString(), Toast.LENGTH_SHORT);
                ToastHelper.showToast(context, binding.accountNameField.getText().toString(), Toast.LENGTH_SHORT);
            }
        });
        //App.dataBaseHelper.addAccount(new Account("中国中央银行卡","1000000000100000111"));
        //accountCardAdapter.addAccountCard(binding.accountCardBanner, accountCardFragment);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void checkFlow(View view) {
        NavigationHelper.navigationNormally(context, HistoryActivity.class);
    }

    public void addTally(View view) {
        NavigationHelper.navigationNormally(context, BookingActivity.class);
    }
}