package com.example.hoitnote.utils.commuications;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.FilterType;

import java.sql.Date;
import java.util.ArrayList;

/*
 * 查询数据库时所用到的
 * 过滤器模型
 * */
public class DataBaseFilter {

    /*
     * ActionType
     * */
    private ActionType actionType;
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

    /*
     * 根据ThirdParty查找
     * */
    private String projectName = null;
    private String memberName = null;
    private String vendorName = null;


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
                          int id, ArrayList<String> classifications, Account account,
                          ActionType actionType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.classifications = classifications;
        this.account = account;
        this.actionType = actionType;
    }

    public DataBaseFilter(Date startDate, Date endDate,
                          int id, ArrayList<String> classifications, Account account,
                          ActionType actionType, String projectName, String memberName, String vendorName) {
        this.actionType = actionType;
        this.account = account;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.classifications = classifications;
        this.projectName = projectName;
        this.memberName = memberName;
        this.vendorName = vendorName;
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


    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getVendorName() {
        return vendorName;
    }
}
