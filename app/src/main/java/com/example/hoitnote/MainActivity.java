package com.example.hoitnote;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hoitnote.adapters.main.AccountCardAdapter;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.databinding.ActivityMainBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.viewmodels.MainViewModel;
import com.example.hoitnote.views.settings.SettingsActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    MainViewModel mainViewModel;
    ActivityMainBinding binding;
    AccountCardAdapter accountCardAdapter;
    ArrayList<AccountCardFragment> accountCardFragments = new ArrayList<>();
    AccountCardFragment currentAccountCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new MainViewModel();

        //App.dataBaseHelper.getAccounts();
        accountCardAdapter = new AccountCardAdapter(getSupportFragmentManager(),getLifecycle(), accountCardFragments);
        initActivity();
        this.context = MainActivity.this;
    }

    private void initActivity() {
        binding.setMainViewModel(mainViewModel);
        binding.accountCardBanner.setAdapter(accountCardAdapter);
        binding.accountCardBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentAccountCard = accountCardAdapter.getFragment(position);
                AccountCardViewModel currentAccountCardViewModel = currentAccountCard.getBinding().getAccountCardViewModel();
                if(currentAccountCardViewModel.getAccount()!=null){
                    ToastHelper.showToast(context,"现在是:"+position+","+currentAccountCardViewModel.getAccount().getAccountName(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void addAccount(View view) {
        /*Fake card*/
        AccountCardViewModel accountCardViewModel = new AccountCardViewModel(
                new Account("中国电信","111"),
                "",
                "",
                "",
                "",
                true
        );
        AccountCardFragment accountCardFragment = new AccountCardFragment(accountCardViewModel);
        accountCardAdapter.addAccountCard(binding.accountCardBanner, accountCardFragment);

        App.dataBaseHelper.addAccount(new Account());
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}