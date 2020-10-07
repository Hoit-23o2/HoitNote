package com.example.hoitnote.utils.helpers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.Interfaces.IDataBase;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.FilterType;
import com.example.hoitnote.utils.enums.ThirdPartyType;

import java.util.ArrayList;
/*
* 该类用于实现数据库交互方法,可能会用到SQLiteOpenHelper
* */
public class DataBaseHelper implements IDataBase {


    public DataBaseHelper(){

    }

    @Override
    public ArrayList<Tally> getTallies(DataBaseFilter filter) {
        //FilterType filterType = filter.predictFilterType();
        
        return null;
    }

    @Override
    public boolean addTally(Tally tally) {
        return false;
    }

    @Override
    public boolean modifyTally(int id, Tally tally) {
        return false;
    }


    @Override
    public boolean delTally(int id) {
        return false;
    }

    @Override
    public boolean delTally(Tally tally) {
        return false;
    }

    @Override
    public boolean saveConfig(Config config) {
        return false;
    }

    @Override
    public ArrayList<Config> getConfigs() {
        return null;
    }

    @Override
    public ArrayList<String> getAllClassification1() {
        return null;
    }

    @Override
    public ArrayList<String> getClassification2(String classification1) {
        return null;
    }

    @Override
    public boolean delClassification1(String classification1) {
        return false;
    }

    @Override
    public boolean delClassification2(String classification1, String classification2) {
        return false;
    }

    @Override
    public boolean addClassification1(String classification1) {
        return false;
    }

    @Override
    public boolean addClassification2(String classification1, String classification2) {
        return false;
    }

    @Override
    public ArrayList<String> getThirdParties(ThirdPartyType thirdPartyType) {
        return null;
    }

    @Override
    public boolean delThirdParty(ThirdPartyType thirdPartyType, String field) {
        return false;
    }

    @Override
    public boolean addThirdParty(ThirdPartyType thirdPartyType, String field) {
        return false;
    }

    @Override
    public ArrayList<Account> getAccounts() {
        return null;
    }

    @Override
    public boolean delAccount(Account account) {
        return false;
    }

    @Override
    public boolean addAccount(Account account) {
        return false;
    }

}
