package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.hoitnote.BR;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.constants.Maps;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.helpers.BlueToothHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.FileHelper;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/*
* 请有ViewModel继承该BaseViewModel
* */
public class BaseViewModel extends BaseObservable  {
    public Context context;

    public BaseViewModel(Context context){
        this.context = context;
    }

    /*将Tally转化为ViewModel*/
    public ArrayList<TallyViewModel> parseTalliesToViewModel(ArrayList<Tally> tallies){
        ArrayList<TallyViewModel> tallyViewModels = new ArrayList<>();
        for (Tally tally:
                tallies) {
            TallyViewModel tallyViewModel = new TallyViewModel(
                    context,
                    tally,
                    "2222",
                    Color.parseColor(Constants.sweetColorAccent),
                    Maps.actionTypeToTypeColor.get(tally.getActionType())
            );
            tallyViewModels.add(tallyViewModel);
        }
        /*测试用*/
        if(tallyViewModels.size() == 0){
            tallyViewModels.add(
                    new TallyViewModel(context, new Tally(
                            20.0,
                            new Date(System.currentTimeMillis()),
                            new Time(10000),
                            "hahah",
                            new Account(),
                            ActionType.INCOME,
                            "餐   饮",
                            "早   餐",
                            "张楠",
                            "大创",
                            "楼下超市"

                    ), "\uf805", Color.parseColor(Constants.sweetColorAccent),
                            Maps.actionTypeToTypeColor.get(ActionType.INCOME))
            );
            tallyViewModels.add(
                    new TallyViewModel(context,new Tally(
                            20.0,
                            new Date(System.currentTimeMillis()-1000000000),
                            new Time(10001),
                            "hahah",
                            new Account(),
                            ActionType.OUTCOME,
                            "餐   饮",
                            "早   餐",
                            "张楠",
                            "大创",
                            "楼下超市"

                    ), "\uf805", Color.parseColor(Constants.sweetColorAccent),
                            Maps.actionTypeToTypeColor.get(ActionType.OUTCOME))
            );
        }
        return tallyViewModels;
    }


}
