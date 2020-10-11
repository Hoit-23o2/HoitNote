package com.example.hoitnote.utils.Interfaces;

import android.content.Context;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ThirdPartyType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IDataBase {


    /*
     * 删除数据库
     * */
    boolean myDeleteDatabase(Context context);

    /*
     * 根据相应条件拿到数据库中所有账单数据
     * */
    ArrayList<Tally> getTallies(DataBaseFilter filter);

    /*
     * 增加一条账单数据至数据库中
     * */
    boolean addTally(Tally tally);

    /*
     * 根据ID或者某一个Tally对象修改账单数据
     * */
    boolean modifyTally(long id,Tally tally);

    /*
     * 根据ID或者某一个Tally对象删除账单数据，重载
     * */
    boolean delTally(long id);
    boolean delTally(Tally tally);

    /*
     * 保存设置
     * */
    boolean saveConfig(Config config);
    /*
     * 获取设置
     * */
    ArrayList<Config> getConfig();


    /*
     * 获取分类的增删查
     * */
    ArrayList<String> getAllClassification1(boolean ifActionType,ActionType actionType);
    ArrayList<String> getClassification2(String classification1,boolean ifActionType,ActionType actionType);
    boolean delClassification1(String classification1,ActionType actionType);
    boolean delClassification2(String classification1, String classification2,ActionType actionType);
    boolean addClassification(String classification1, String classification2,ActionType actionType);

    /*
     * Vendor Project Member 选项的增删查
     * */
    ArrayList<String> getThirdParties(ThirdPartyType thirdPartyType);
    boolean delThirdParty(ThirdPartyType thirdPartyType, String field);
    boolean addThirdParty(ThirdPartyType thirdPartyType, String field);

    ArrayList<Account> getAccounts();
    boolean delAccount(Account account);
    boolean addAccount(Account account);
}
