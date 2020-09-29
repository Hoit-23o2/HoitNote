package com.example.hoitnote.utils.Interfaces;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.enums.ThirdPartyType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IDataBase {
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
    boolean modifyTally(int id,Tally tally);

    /*
    * 根据ID或者某一个Tally对象删除账单数据，重载
    * */
    boolean delTally(int id);
    boolean delTally(Tally tally);

    /*
    * 保存设置
    * */
    boolean saveConfig(Config config);
    /*
    * 获取设置
    * */
    Config getConfig();


    /*
     * 获取分类的增删查
     * */
    ArrayList<String> getAllClassification1();
    ArrayList<String> getClassification2(String classification1);
    boolean delClassification1(String classification1);
    boolean delClassification2(String classification1, String classification2);
    boolean addClassification1(String classification1);
    boolean addClassification2(String classification1, String classification2);

    /*
    * Vendor Project Member 选项的增删查
    * */
    ArrayList<String> getThirdParties(ThirdPartyType thirdPartyType);
    boolean delThirdParty(ThirdPartyType thirdPartyType, String field);
    boolean addThirdParty(ThirdPartyType thirdPartyType, String field);
}
