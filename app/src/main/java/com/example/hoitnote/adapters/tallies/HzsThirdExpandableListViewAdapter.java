package com.example.hoitnote.adapters.tallies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoitnote.R;
import com.example.hoitnote.models.flow.HzsDayData;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;


import java.text.SimpleDateFormat;
import java.util.List;


public class HzsThirdExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    LayoutInflater inflater;
    List<HzsDayData> days;
    public HzsThirdExpandableListViewAdapter(List<HzsDayData> days,Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.days = days;
    }

    @Override
    public int getGroupCount() {
        return days.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return days.get(i).getData().size();
    }

    @Override
    public Object getGroup(int i) {
        return days.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return days.get(i).getData().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        DayTotalHolder holder = null;
        if(view == null){
            holder = new DayTotalHolder();
            view = inflater.inflate(R.layout.hzs_expand_item_main_menu,viewGroup,false);
            holder.day = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_first_header);
            holder.weekday = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_second_header);
            holder.balance = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_balance);
            holder.income = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_income);
            holder.outcome = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_outcome);
            view.setTag(holder);
        }else{
            holder = (DayTotalHolder) view.getTag();
        }
        holder.day.setText(days.get(i).getDay());
        holder.weekday.setText(days.get(i).getWeekday());
        holder.balance.setText(days.get(i).getBalance());
        holder.income.setText(days.get(i).getIncome());
        holder.outcome.setText(days.get(i).getOutcome());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ThirdHolder holder = null;
        if(view == null){
            holder = new ThirdHolder();
            view = inflater.inflate(R.layout.hzs_expand_item_tally,viewGroup,false);
            holder.account = (TextView)view.findViewById(R.id.hzs_expand_item_tally_account);
            holder.day = (TextView)view.findViewById(R.id.hzs_expand_item_tally_day);
            holder.weekday = (TextView)view.findViewById(R.id.hzs_expand_item_tally_weekday);
            holder.icon = (ImageView)view.findViewById(R.id.hzs_expand_item_tally_icon);
            holder.classname = (TextView)view.findViewById(R.id.hzs_expand_item_tally_classname);
            holder.time = (TextView)view.findViewById(R.id.hzs_expand_item_tally_time);
            holder.money = (TextView)view.findViewById(R.id.hzs_expand_item_tally_money);
            holder.delete = (TextView)view.findViewById(R.id.hzs_expand_item_tally_delete);
            view.setTag(holder);
        }else{
            holder = (ThirdHolder)view.getTag();
        }
        holder.day.setText(String.valueOf(days.get(i).getData().get(i1).getDate().getDate()));
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String weekday = formatter.format(days.get(i).getData().get(i1).getDate());
        holder.weekday.setText(weekday);
        if(!days.get(i).getData().get(i1).getClassification2().equals("无")){
            holder.classname.setText(days.get(i).getData().get(i1).getClassification2());
        }else{
            holder.classname.setText(days.get(i).getData().get(i1).getClassification1());
        }
        holder.time.setText(days.get(i).getData().get(i1).getTime().toString());
        holder.account.setText(days.get(i).getData().get(i1).getAccount().getAccountName());
        holder.money.setText(String.valueOf(days.get(i).getData().get(i1).getMoney()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delTally(days.get(i).getData().get(i1).getId());
                HistoryActivity.getInstance().refreshData();
            }
        });
        return view;
    }

    static class DayTotalHolder{
        TextView day;
        TextView weekday;
        TextView balance;
        TextView income;
        TextView outcome;
    }
    static class ThirdHolder{
        TextView day;
        TextView weekday;
        ImageView icon;
        TextView classname;
        TextView time;
        TextView account;
        TextView money;
        TextView delete;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
