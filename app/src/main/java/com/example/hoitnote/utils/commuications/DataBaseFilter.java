package com.example.hoitnote.utils.commuications;


import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.FilterType;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/*
 * 查询数据库时所用到的
 * 过滤器模型
 * */
public class DataBaseFilter {

    /*
     *根据ActionType查找
     */
    private ActionType actionType;



    /*
     * 如果不想使用ID查找，传入IDInvalid
     * */
    public static final long IDInvalid = -1;
    /*
     * 根据账户查询
     * */
    private Account account;

    /*
     * 依据时间段查找
     * */
    private Date startDate;
    private Date endDate;

    /*
     * 依据ID查找
     * */
    private long id;

    /*
     * 依据分类查找 c1 c1&c2
     * 所有一级分类
     * 长度1：该分类下所有
     * 长度大于1：整体
     * */

    private ArrayList<String> classifications;

    public ArrayList<String> getClassifications() {
        return classifications;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Account getAccount() {
        return account;
    }
    /*
     * 构造函数
     * */
    public DataBaseFilter(Date startDate, Date endDate,
                          long id, ArrayList<String> classifications, Account account,ActionType actionType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.classifications = classifications;
        this.account = account;
        this.actionType = actionType;
    }
}
