package com.example.hoitnote.models.charts;

import java.util.ArrayList;

public class TallyAnalysisPC {
    public double allMoney;     //本扇区的总钱数
    public double percent;      //本扇区的占比

    public float fanStart;      //本扇区的相对0起点
    public float fanEnd;        //本扇区的相对0终点

    public String signName;     //本扇区的名称
    public String screen;       //本扇区的筛选
    public int selfColor;       //本扇区的颜色

    public ArrayList<TallyAnalysisPC> nextScreen;

    public TallyAnalysisPC() {
        this.nextScreen = null;
    }
}
