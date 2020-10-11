package com.example.hoitnote.viewmodels;

import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.ActionType;

import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

public class MainViewModel extends BaseViewModel {
    public MainViewModel(){

    }

    public ArrayList<AccountCardFragment> getAccountCardFragments(){
        ArrayList<AccountCardFragment> accountCardFragments = new ArrayList<>();
        ArrayList<Account> accounts = App.dataBaseHelper.getAccounts();
        for (Account account:
                accounts) {
            ArrayList<Tally> tallies = App.dataBaseHelper.getTallies(new DataBaseFilter(
                    null,
                    null,
                    DataBaseFilter.IDInvalid,
                    null,
                    account,
                    null
            ));
            int incomes = 0;
            int outcomes = 0;
            for (Tally tally:
                    tallies) {
                if(tally.getActionType() == ActionType.OUTCOME){
                    outcomes = outcomes + (int) tally.getMoney();
                }
                else if(tally.getActionType() == ActionType.INCOME){
                    incomes += (int)tally.getMoney();
                }
            }
            AccountCardViewModel accountCardViewModel = new AccountCardViewModel(
                    account,
                    "",
                    String.valueOf(incomes),
                    String.valueOf(outcomes),
                    "",
                    true
            );
            AccountCardFragment accountCardFragment = new AccountCardFragment(accountCardViewModel);
            accountCardFragments.add(accountCardFragment);
        }
        return accountCardFragments;
    }

    public ArrayList<TallyViewModel> getRecentTallyViewModels(AccountCardFragment currentAccountCardFragment){
        AccountCardViewModel currentAccountCardViewModel = currentAccountCardFragment.getBinding().getAccountCardViewModel();
        Account currentAccount = currentAccountCardViewModel.getAccount();
        ArrayList<TallyViewModel> tallyViewModels = new ArrayList<>();
        long recentDay = 7;
        long dayMillsUnit = 24*60*60*1000;
        Date endDate = new Date(System.currentTimeMillis());
        Date startDate = new Date(System.currentTimeMillis() - recentDay * dayMillsUnit);
        ArrayList<Tally> tallies = App.dataBaseHelper.getTallies(
                new DataBaseFilter(
                    startDate,
                    endDate,
                    DataBaseFilter.IDInvalid,
                    null,
                    currentAccount,
                    null
                )
        );
        for (Tally tally:
             tallies) {
            TallyViewModel tallyViewModel = new TallyViewModel(
                    tally,
                    "2222"
            );
            tallyViewModels.add(tallyViewModel);
        }
        /*测试用*/
        if(tallyViewModels.size() == 0){
            tallyViewModels.add(
                    new TallyViewModel(new Tally(
                            20.0,
                            null,
                            new Time(10000),
                            "hahah",
                            new Account(),
                            ActionType.INCOME,
                            "餐饮",
                            "早餐",
                            "张楠",
                            "大创",
                            "楼下超市"

                    ), "ss")
            );
            tallyViewModels.add(
                    new TallyViewModel(new Tally(
                            20.0,
                            null,
                            new Time(10000),
                            "hahah",
                            new Account(),
                            ActionType.INCOME,
                            "餐  饮",
                            "早 餐",
                            "张楠",
                            "大创",
                            "楼下超市"

                    ), "ss")
            );
        }
        /**/
        return tallyViewModels;
    }
}
