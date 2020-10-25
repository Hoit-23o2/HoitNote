package com.example.hoitnote;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.Menu;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.RelativeGuide;
import com.example.hoitnote.adapters.main.AccountCardAdapter;
import com.example.hoitnote.adapters.main.TallyOneExpandableAdapter;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.customviews.DraggableHand;
import com.example.hoitnote.databinding.ActivityMainBinding;
import com.example.hoitnote.databinding.PopupwindowAddaccountBinding;
import com.example.hoitnote.databinding.PopupwindowDialogNormalBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.AccountJudgeType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.enums.GroupType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.utils.helpers.DeviceHelper;
import com.example.hoitnote.utils.helpers.DialogHelper;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.viewmodels.MainViewModel;
import com.example.hoitnote.viewmodels.TallyViewModel;
import com.example.hoitnote.views.flow.HistoryActivity;
import com.example.hoitnote.views.tallyadd.BookingActivity;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {
    MainViewModel mainViewModel;
    ActivityMainBinding binding;
    AccountCardAdapter accountCardAdapter;
    TallyOneExpandableAdapter tallyOneExpandableAdapter;
    ArrayList<AccountCardFragment> accountCardFragments = new ArrayList<>();
    AccountCardFragment currentAccountCardFragment;

    /*拉动相关*/
    private boolean isInitOriginY = true;
    private float originY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel(context);
        BookingDataHelper.getAccounts();
        initActivity();
    }

    private void initActivity() {
        /*从viewModel处拿取账户数据，并转换为Fragments*/
        accountCardFragments = mainViewModel.getAccountCardFragments();
        for (AccountCardFragment accountCardFragment:
                accountCardFragments) {
            accountCardFragment.setLongClickListener(new AccountCardFragment.LongClickListener() {
                @Override
                public void onLongClick(final AccountCardFragment accountCardFragment, View v) {

                   onLongClickImplement(accountCardFragment, v);

                }
            });
        }
        Lifecycle lifecycle = getLifecycle();
        accountCardAdapter = new AccountCardAdapter(getSupportFragmentManager(),lifecycle,
                accountCardFragments);
        /*准备ViewModel*/
        binding.setMainViewModel(mainViewModel);
        binding.accountCardBanner.setAdapter(accountCardAdapter);
        binding.accountCardBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == accountCardAdapter.getItemCount() - 1){
                    binding.recentTalliesExpandableListView.setVisibility(View.GONE);
                    binding.recentHintTextView.setVisibility(View.GONE);
                    binding.floatingButton.setVisibility(View.GONE);
                }
                else{
                    binding.recentTalliesExpandableListView.setVisibility(View.VISIBLE);
                    binding.recentHintTextView.setVisibility(View.VISIBLE);
                    binding.floatingButton.setVisibility(View.VISIBLE);
                    currentAccountCardFragment = accountCardAdapter.getFragment(position);
                    updateCurrentAccountCardListView();
                }

            }
        });
        /*拖动视图*/
        binding.drawer.setDraggableHandListener(new DraggableHand.DraggableHandListener() {
            @Override
            public void onTouchDown(float x, float y) {
                if(isInitOriginY){
                    originY = y;
                    isInitOriginY = false;
                }
            }

            @Override
            public void onTouchUp(float x, float y) {

            }

            @Override
            public void onTouchMove(float x, float y) {
                int upperContainerHeight = DeviceHelper.getActionBarHeight(context) + DeviceHelper.getStatueBarHeight(context);
                if(y < upperContainerHeight || y > originY){
                    return;
                }

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                        binding.recentTalliesContainer.getLayoutParams();
                params.height = DeviceHelper.getDeviceHeight(MainActivity.this) - (int)y;
                binding.recentTalliesContainer.setLayoutParams(params);
            }
        });

        /*修改NavigationKey*/
        int color = ThemeHelper.getPrimaryLightColor(context);
        ThemeHelper.changeColorOfNavigationBar(this,
                color);



        /*调整最近列表的高度*/
        float cardFragmentRatio = 0.6f;
        int deviceHeight = DeviceHelper.getDeviceHeight(context);
        int deviceWidth = DeviceHelper.getDeviceWidth(context);
        int statueBarHeight = DeviceHelper.getStatueBarHeight(context);
        int actionBarHeight = DeviceHelper.getActionBarHeight(context);
        float cardFragmentHeight = deviceWidth * cardFragmentRatio;
        float recentTalliesContainerHeight = deviceHeight - cardFragmentHeight
                - statueBarHeight - actionBarHeight;
        ConstraintLayout.LayoutParams layoutParams =
                (ConstraintLayout.LayoutParams) binding.recentTalliesContainer.getLayoutParams();
        layoutParams.height = (int) recentTalliesContainerHeight;
        binding.recentTalliesContainer.setLayoutParams(layoutParams);
        /*上移动画*/
        binding.recentTalliesContainer.setTranslationY(2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recentTalliesContainer.animate().translationY(0).start();
            }
        }, Constants.delayDuration / 4);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCurrentAccountCardListView();
    }

    /*更新RencentTally*/
    public void updateCurrentAccountCardListView(){
        if(currentAccountCardFragment != null){
            if(currentAccountCardFragment.getBinding() != null){
                ArrayList<TallyViewModel> tallyViewModels = mainViewModel.getRecentTallyViewModelsByCardFragment(currentAccountCardFragment);
                /*分组*/
                TreeMap<String, ArrayList<TallyViewModel>> tallyViewModelWithGroups = mainViewModel.
                        groupTallyViewModel(tallyViewModels, GroupType.DATE);
                tallyOneExpandableAdapter = new TallyOneExpandableAdapter(context, tallyViewModelWithGroups);
                binding.recentTalliesExpandableListView.setAdapter(tallyOneExpandableAdapter);
                /*展开所有分组*/
                tallyOneExpandableAdapter.expandAllGroup(binding.recentTalliesExpandableListView);
            }
        }

    }

    private boolean isBtnClick = false;
    /*点击添加加号后添加一个账户*/
    public void addAccount(View view) {
        final PopupwindowAddaccountBinding popupViewBinding;
        popupViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.popupwindow_addaccount,
                null,
                false
        );
        final AlertDialog alertDialog = DialogHelper.buildDialog(context, popupViewBinding);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(isBtnClick){
                    String accountName = popupViewBinding.accountNameField.getText().toString();
                    String accountCode = popupViewBinding.accountCodeField.getText().toString();
                    ToastHelper.showToast(context, accountName, Toast.LENGTH_SHORT);
                    ToastHelper.showToast(context, accountCode, Toast.LENGTH_SHORT);
                    Account newAccount = new Account(accountName,accountCode);
                    AccountJudgeType accountJudge = newAccount.checkIfAccountValid();
                    /*添加账户成功*/
                    if(accountJudge == AccountJudgeType.SUCCESSFUL){
                        App.dataBaseHelper.addAccount(newAccount);
                        AccountCardFragment newCardFragment = newAccount.parseToAccountCardFragment(context, ClickType.TAP);
                        newCardFragment.setLongClickListener(new AccountCardFragment.LongClickListener() {
                            @Override
                            public void onLongClick(AccountCardFragment accountCardFragment, View v) {
                                onLongClickImplement(accountCardFragment, v);
                            }
                        });
                        accountCardAdapter.addAccountCard(binding.accountCardBanner, newCardFragment);
                    }
                    /*账号重复*/
                    else if(accountJudge == AccountJudgeType.CODE_SAME){
                        ToastHelper.showToast(context, Constants.accountCodeSameHint
                                , Toast.LENGTH_SHORT);
                    }
                    /*账号长度不足*/
                    else if(accountJudge == AccountJudgeType.CODE_NOT_ENOUGH){
                        ToastHelper.showToast(context, Constants.accountCodeNotEnoughHint
                                , Toast.LENGTH_SHORT);
                    }
                    isBtnClick = false;
                }

            }
        });

        popupViewBinding.addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                isBtnClick = true;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("RtlHardcoded")
                @Override
                public void run() {
                    NewbieGuide.with(MainActivity.this)
                            .setLabel("guide_main")
                            .addGuidePage(GuidePage.newInstance()
                                    .addHighLight(binding.accountCardBanner, new RelativeGuide(R.layout.guide_main_card,
                                            Gravity.BOTTOM, 20)))
                            .addGuidePage(GuidePage.newInstance()
                                    .addHighLight(binding.floatingButton, new RelativeGuide(R.layout.guide_main_floating_button,
                                            Gravity.LEFT, 20)))
                            .alwaysShow(true)
                            .show();
                }
            },500);

        }
    }
    private void onLongClickImplement(final AccountCardFragment accountCardFragment, View v){
        PopupwindowDialogNormalBinding dialogNormalBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.popupwindow_dialog_normal,
                        null,
                        false
                );
        final AlertDialog alertDialog =
                DialogHelper.buildDialog(context, dialogNormalBinding);
        dialogNormalBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountCardAdapter.removeAccountCard(binding.accountCardBanner,
                        accountCardFragments.indexOf(accountCardFragment));
                AccountCardViewModel accountCardViewModel =
                        accountCardFragment.getBinding().getAccountCardViewModel();
                if(accountCardViewModel != null){
                    Account account = accountCardViewModel.getAccount();
                    ArrayList<Tally> tallies = App.dataBaseHelper.getTallies(new DataBaseFilter(
                            null,
                            null,
                            DataBaseFilter.IDInvalid,
                            null,
                            account,
                            null
                    ));
                    for (Tally tally:
                            tallies) {
                        App.backupDataBaseHelper.addTally(tally);
                        App.dataBaseHelper.delTally(tally);
                    }
                    App.dataBaseHelper.delAccount(account);
                }
                alertDialog.dismiss();
            }
        });
        dialogNormalBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}