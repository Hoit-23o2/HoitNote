package com.example.hoitnote.utils.enums;
/*
* 过滤器类型
* */
public enum FilterType {
    DurationOnly,   //仅筛选时段
    AccountOnly,    //仅筛选账户
    AccountAndDurationAndClassification, //账户
    DurationAndClassification,  //时段+分类
    ID, //ID
    ClassificationOnly, //进筛选分类
    All //无过滤
}
