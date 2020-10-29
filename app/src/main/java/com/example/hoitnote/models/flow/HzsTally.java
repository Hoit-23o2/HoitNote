package com.example.hoitnote.models.flow;

import android.annotation.SuppressLint;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.IconType;


import java.text.SimpleDateFormat;

public class HzsTally {
    public static final int DATE = 0;
    public static final int TIME = 1;
    public static final int DAYANDTIME = 2;
    public String day;
    public String weekday;
    public String classname;
    public String time;
    public String account;
    public String money;
    public String iconCode;
    public ActionType actionType;
    private IconType iconType;
    private int classType = 2;
    public String member;
    public String vendor;
    public String project;
    public String remark;

    public HzsTally(Tally tally, int timeType){


        actionType = tally.getActionType();
        day = tally.getDate().toString();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        weekday = formatter.format(tally.getDate());

        if(tally.getClassification2().equals("无")){
            classname = (tally.getClassification1());
            classType = 1;
        }else if(tally.getClassification1().equals("转账支出")){
            classType = 1;
            classname = (tally.getClassification1());
        }else if(tally.getClassification1().equals("转账收入")){
            classType = 1;
            classname = (tally.getClassification1());
        }
        else{
            classname = (tally.getClassification2());
        }
        time = (tally.getTime().toString());
        account = (tally.getAccount().getAccountName());
        money = (String.valueOf(tally.getMoney()));
        if(tally.getActionType() == ActionType.OUTCOME){
            if(classType == 1){
                iconType = IconType.OUTCOMECLASS1;
            }else{
                iconType = IconType.OUTCOMECLASS2;
            }
        }else{
            if(classType == 1){
                iconType = IconType.INCOMECLASS1;
            }else{
                iconType = IconType.INCOMECLASS2;
            }
        }
        if(classType == 1){
            iconCode = App.dataBaseHelper.getIconInformation(classname,iconType);
        }else{
            iconCode = App.dataBaseHelper.getIconInformation(tally.getClassification1()+tally.getClassification2(),iconType);
        }
        switch (timeType){
            case TIME:
                break;
            case DAYANDTIME:
                time = tally.getDate().getDate()+"日"+time;
                break;
            default:
                time = day;
                break;
        }

        member = tally.getMember();
        vendor = tally.getVendor();
        project = tally.getProject();
        remark = tally.getRemark();
    }
}
