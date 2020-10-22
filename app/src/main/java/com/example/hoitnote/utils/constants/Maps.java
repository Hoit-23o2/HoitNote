package com.example.hoitnote.utils.constants;

import android.graphics.Color;

import com.example.hoitnote.utils.enums.ActionType;

import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static Map<ActionType, Integer> actionTypeToTypeColor = new HashMap<ActionType, Integer>() {
        {
            put(ActionType.INCOME, Color.parseColor(Constants.sweetColorAccent));
            put(ActionType.OUTCOME, Color.parseColor(Constants.defaultColorAccent));
        }
    };

    public static Map<String, String> accountNameToNameIcon = new HashMap<String, String>(){
        {

        }
    };


}
