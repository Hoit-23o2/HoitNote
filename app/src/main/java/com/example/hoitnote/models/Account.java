package com.example.hoitnote.models;

import android.content.Context;

import com.example.hoitnote.R;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Maps;
import com.example.hoitnote.utils.enums.AccountJudgeType;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.viewmodels.AccountCardViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Account implements Serializable {
    private String accountName;
    private String accountCode;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Account(){

    }

    public Account(String accountName, String accountCode) {
        this.accountName = accountName;
        this.accountCode = accountCode;
    }

    public AccountJudgeType checkIfAccountValid(){
        ArrayList<Account> existAccounts = App.dataBaseHelper.getAccounts();

        for (Account existAccount:
             existAccounts) {
            if(existAccount.accountCode.equals(this.accountCode)){
                return AccountJudgeType.CODE_SAME;
            }
        }
        return AccountJudgeType.SUCCESSFUL;
    }

    public AccountCardFragment parseToAccountCardFragment(Context context, ClickType clickType){
        AccountCardViewModel accountCardViewModel = parseToAccountCardViewModel(context, clickType);
        return new AccountCardFragment(accountCardViewModel);
    }

    public AccountCardViewModel parseToAccountCardViewModel(Context context, ClickType clickType){
        ArrayList<Tally> tallies = App.dataBaseHelper.getTallies(new DataBaseFilter(
                null,
                null,
                DataBaseFilter.IDInvalid,
                null,
                this,
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
        int accountRes = R.drawable.bank_union_pay_icon;
        for(Map.Entry<String, Integer> entry : Maps.accountNameToBankIconMap.entrySet()){
            String keyword = entry.getKey();
            int bankIcon = entry.getValue();
            if(accountName.contains(keyword)){
                accountRes = bankIcon;
                break;
            }
        }
        AccountCardViewModel accountCardViewModel = new AccountCardViewModel(
                this,
                accountRes,
                String.valueOf(incomes),
                String.valueOf(outcomes),
                "",
                true,
                context,
                clickType
        );
        return accountCardViewModel;
    }

}
