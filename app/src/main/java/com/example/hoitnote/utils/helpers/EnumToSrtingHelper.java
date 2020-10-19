package com.example.hoitnote.utils.helpers;

import com.example.hoitnote.utils.enums.ActionType;

public class EnumToSrtingHelper {
    public static String actionType2String(ActionType actionType){
        String actionTypeStr= "";
        switch (actionType){
            case INCOME:
                actionTypeStr = "收入";
                break;
            case OUTCOME:
                actionTypeStr = "支出";
                break;
        }
        return actionTypeStr;
    }
}
