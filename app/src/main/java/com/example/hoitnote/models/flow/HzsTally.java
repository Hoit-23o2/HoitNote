package com.example.hoitnote.models.flow;

import android.annotation.SuppressLint;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.IconType;


import java.text.SimpleDateFormat;

public class HzsTally {
    public String day;
    public String weekday;
    public String classname;
    public String time;
    public String account;
    public String money;
    public String iconCode;
    private IconType iconType;
    private int classType = 2;

    public HzsTally(Tally tally){
        day = String.valueOf(tally.getDate().getDate());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        weekday = formatter.format(tally.getDate());

        if(tally.getClassification2().equals("无")){
            classname = (tally.getClassification1());
            classType = 1;
        }else{
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
    }
}
