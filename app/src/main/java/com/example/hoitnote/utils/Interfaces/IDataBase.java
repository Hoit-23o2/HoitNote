package com.example.hoitnote.utils.Interfaces;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;

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
}
