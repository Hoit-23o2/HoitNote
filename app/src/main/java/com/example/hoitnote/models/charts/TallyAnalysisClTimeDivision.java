package com.example.hoitnote.models.charts;


import java.util.ArrayList;

public class TallyAnalysisClTimeDivision {

    public double maxMoney;         //本时间分隔的最大钱数
    public double minMoney;         //本时间分隔的最小钱数
    public String divisionName;     //时间分隔名称

    public ArrayList<TimeAndMoney> timeAndMoneyArrayList;

    private TimeAndMoney nowTimeAndMoney;

    public TallyAnalysisClTimeDivision() {
        this.timeAndMoneyArrayList = new ArrayList<>();
    }

    public void addNewNode(String name){
        nowTimeAndMoney = new TimeAndMoney();
        nowTimeAndMoney.timeName = name;
        nowTimeAndMoney.Money = 0;
        timeAndMoneyArrayList.add(nowTimeAndMoney);
    }

    public void addMoney(double money){
        nowTimeAndMoney.Money += money;
    }

    public static class TimeAndMoney{
        public String timeName;
        public double Money;
    }
}
