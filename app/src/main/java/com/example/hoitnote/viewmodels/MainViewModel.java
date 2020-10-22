package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.constants.Maps;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.enums.GroupType;

import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainViewModel extends BaseViewModel {

    public MainViewModel(Context context){
        super(context);
    }

    /*
    * 获取所有账户的Fragments，用于填满ViewPager
    * */
    public ArrayList<AccountCardFragment> getAccountCardFragments(){
        ArrayList<AccountCardFragment> accountCardFragments = new ArrayList<>();
        ArrayList<Account> accounts = App.dataBaseHelper.getAccounts();
        for (Account account:
                accounts) {
            AccountCardFragment accountCardFragment = account.
                    parseToAccountCardFragment(context, ClickType.TAP);
            accountCardFragments.add(0,accountCardFragment);
        }
        return accountCardFragments;
    }

    /*
    * 根据GroupType对TallyViewModel进行分组
    * */
    public HashMap<String, ArrayList<TallyViewModel>> groupTallyViewModel(ArrayList<TallyViewModel> tallyViewModels,
                                                                          GroupType groupType){
        HashMap<String, ArrayList<TallyViewModel>> tallyViewModelWithGroups = new
                HashMap<>();
        if(groupType == GroupType.DATE){
            for (TallyViewModel tallyViewModel:
                 tallyViewModels) {
                String timeStr = tallyViewModel.getTally().getDate().toString();
                ArrayList<TallyViewModel> tallies = tallyViewModelWithGroups.get(timeStr);
                if(tallies == null){
                    tallies = new ArrayList<>();
                }
                tallies.add(tallyViewModel);
                tallyViewModelWithGroups.put(timeStr, tallies);
            }
        }
        return tallyViewModelWithGroups;
    }

    /*
    * 获取最近7天内的TallyViewModel
    * */
    public ArrayList<TallyViewModel> getRecentTallyViewModelsByCardFragment(AccountCardFragment currentAccountCardFragment){
        AccountCardViewModel currentAccountCardViewModel = currentAccountCardFragment.getBinding().getAccountCardViewModel();
        Account currentAccount = currentAccountCardViewModel.getAccount();
        ArrayList<TallyViewModel> tallyViewModels = new ArrayList<>();
        long recentDay = 7;
        long dayMillsUnit = 24*60*60*1000;
        Date endDate = new Date(System.currentTimeMillis());
        Date startDate = new Date(System.currentTimeMillis() - recentDay * dayMillsUnit);
        Account account = new Account(Constants.noneAccountName,Constants.noneAccountCode);
        if(currentAccount != null){
            account = currentAccount;
        }
        DataBaseFilter filter = new DataBaseFilter(
                startDate,
                endDate,
                DataBaseFilter.IDInvalid,
                null,
                account,
                null
        );

        ArrayList<Tally> tallies = App.dataBaseHelper.getTallies(filter);
        /**/
        return  parseTalliesToViewModel(tallies);
    }


}
