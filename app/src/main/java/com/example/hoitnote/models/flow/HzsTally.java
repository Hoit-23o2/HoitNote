package com.example.hoitnote.models.flow;

import android.annotation.SuppressLint;
import com.example.hoitnote.models.Tally;


import java.text.SimpleDateFormat;

public class HzsTally {
    public String day;
    public String weekday;
    public String classname;
    public String time;
    public String account;
    public String money;



    public HzsTally(Tally tally){
        day = String.valueOf(tally.getDate().getDate());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        weekday = formatter.format(tally.getDate());

        if(!tally.getClassification2().equals("无")){
            classname = (tally.getClassification2());
        }else{
            classname = (tally.getClassification1());
        }
        time = (tally.getTime().toString());
        account = (tally.getAccount().getAccountName());
        money = (String.valueOf(tally.getMoney()));

    }
}
