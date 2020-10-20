package com.example.hoitnote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
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
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.AccountJudgeType;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.enums.GroupType;
import com.example.hoitnote.utils.enums.Theme;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
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
        mainViewModel = new MainViewModel(context);
        /*从viewModel处拿取账户数据，并转换为Fragments*/
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
                    ArrayList<TallyViewModel> tallyViewModels = mainViewModel.getRecentTallyViewModelsByCardFragment(currentAccountCardFragment);
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
        int color = ThemeHelper.getPrimaryLightColor(context);
        String colorStr = "#" + Integer.toHexString(color).substring(2);
        ThemeHelper.changeColorOfNavigationBar(this,
                colorStr);

    }

    /*点击添加加号后添加一个账户*/
    public void addAccount(View view) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final PopupwindowAddaccountBinding popupViewBinding;
        popupViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.popupwindow_addaccount,
                null,
                false
        );

        //dialog.setContentView(popupViewBinding.getRoot());
        alertDialog = builder.setView(popupViewBinding.getRoot()).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String accountName = popupViewBinding.accountNameField.getText().toString();
                String accountCode = popupViewBinding.accountCodeField.getText().toString();
                ToastHelper.showToast(context, accountName, Toast.LENGTH_SHORT);
                ToastHelper.showToast(context, accountCode, Toast.LENGTH_SHORT);
                Account newAccount = new Account(accountName,accountCode);
                AccountJudgeType accountJudge = newAccount.checkIfAccountValid();
                if(accountJudge == AccountJudgeType.SUCCESSFUL){
                    App.dataBaseHelper.addAccount(newAccount);
                    AccountCardFragment newCardFragment = new AccountCardFragment(
                            new AccountCardViewModel(
                                    newAccount,
                                    "",
                                    "0",
                                    "0",
                                    "0",
                                    true,
                                    context,
                                    ClickType.TAP
                            )
                    );
                    accountCardAdapter.addAccountCard(binding.accountCardBanner, newCardFragment);
                }
                else if(accountJudge == AccountJudgeType.CODE_SAME){
                    ToastHelper.showToast(context, Constants.accountCodeSameHint
                            , Toast.LENGTH_SHORT);
                }
                else if(accountJudge == AccountJudgeType.CODE_NOT_ENOUGH){
                    ToastHelper.showToast(context, Constants.accountCodeNotEnoughHint
                            , Toast.LENGTH_SHORT);
                }
            }
        });

        popupViewBinding.addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
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
        NavigationHelper.navigationWithParameter(Constants.mainParamTag,
                currentAccountCardFragment.getBinding().getAccountCardViewModel().getAccount(),
                context,BookingActivity.class, false);
    }
}