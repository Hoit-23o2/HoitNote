package com.example.hoitnote.utils.constants;

import android.graphics.Color;

import com.example.hoitnote.R;
import com.example.hoitnote.utils.enums.ActionType;

import java.util.HashMap;
import java.util.Map;

public class Maps {
    /*类型颜色*/
    public static Map<ActionType, Integer> actionTypeToTypeColorMap = new HashMap<ActionType, Integer>() {
        {
            put(ActionType.INCOME, Color.parseColor(Constants.lightGreen));
            put(ActionType.OUTCOME, Color.parseColor(Constants.lightRed));
        }
    };

    /*银行关键词*/
    public static Map<String, Integer> accountNameToBankIconMap = new HashMap<String, Integer>(){
        {
            put(Constants.PingAnKeyWord, R.drawable.bank_pingan_icon);
            put(Constants.HuaXiaKeyWord, R.drawable.bank_huaxia_icon);
            put(Constants.JianSheKeyWord, R.drawable.bank_jianshe_icon);
            put(Constants.ZhongGuoKeyWord, R.drawable.china_icon);
            put(Constants.ZhongXinKeyWord, R.drawable.bank_zhongxin_icon);
        }
    };


}
