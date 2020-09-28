package com.example.hoitnote.utils.helpers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.Interfaces.IDataBase;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.FilterType;

import java.util.ArrayList;
/*
* 该类用于实现数据库交互方法,可能会用到SQLiteOpenHelper
* */
public class DataBaseHelper implements IDataBase {

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
    public Config getConfig() {
        return null;
    }

}
