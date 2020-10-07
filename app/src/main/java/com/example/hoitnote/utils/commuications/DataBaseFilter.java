package com.example.hoitnote.utils.commuications;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.enums.FilterType;

import java.sql.Date;
import java.util.ArrayList;

/*
* 查询数据库时所用到的
* 过滤器模型
* */
public class DataBaseFilter {



    /*
    * 如果不想使用ID查找，传入IDInvalid
    * */
    public static final int IDInvalid = -1;
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
    private int id;

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

    public int getId() {
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
                          int id, ArrayList<String> classifications, Account account) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.classifications = classifications;
        this.account = account;
    }

    /*
    * 预测Filter的类型
    * */
    public FilterType predictFilterType(){
        if(getId() != IDInvalid)
            return FilterType.ID;
        if(getClassifications() != null && getStartDate() == null
        && getEndDate() == null)
            return FilterType.ClassificationOnly;
        if(getClassifications() != null && (getStartDate() != null
                || getEndDate() != null))
            return FilterType.DurationAndClassification;
        if(getClassifications() == null && (getStartDate() != null
                || getEndDate() != null))
            return FilterType.DurationOnly;
        return FilterType.All;
    }



}
