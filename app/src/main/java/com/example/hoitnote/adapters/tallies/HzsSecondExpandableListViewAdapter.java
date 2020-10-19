package com.example.hoitnote.adapters.tallies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoitnote.R;
import com.example.hoitnote.models.flow.HzsMonthData;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;


import java.text.SimpleDateFormat;
import java.util.List;


public class HzsSecondExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "HzsSecondExpandableList";
    private List<HzsMonthData> months;
    private Context context;
    private LayoutInflater inflater;
    public HzsSecondExpandableListViewAdapter(List<HzsMonthData> months, Context context){
        this.months = months;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return months.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return months.get(i).getData().size();
    }

    @Override
    public Object getGroup(int i) {
        return months.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return months.get(i).getData().get(i1);
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
        SecondHolder holder = null;
        if(view == null){
            holder = new SecondHolder();
            view = inflater.inflate(R.layout.hzs_expand_item_main_menu,viewGroup,false);
            holder.balance = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_balance);
            holder.imageView = (ImageView)view.findViewById(R.id.hzs_expand_item_main_menu_image_button);
            holder.income = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_income);
            holder.outcome = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_outcome);
            holder.year = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_second_header);
            holder.month = (TextView)view.findViewById(R.id.hzs_expand_item_main_menu_first_header);
            view.setTag(holder);
        }else{
            holder = (SecondHolder)view.getTag();
        }
        holder.month.setText(months.get(i).getMonth()+"月");
        holder.outcome.setText(months.get(i).getOutcome());
        holder.income.setText(months.get(i).getIncome());
        holder.balance.setText(months.get(i).getBalance());
        holder.year.setText(months.get(i).getYear()+"年");
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
        holder.day.setText(String.valueOf(months.get(i).getData().get(i1).getDate().getDate()));
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String weekday = formatter.format(months.get(i).getData().get(i1).getDate());
        holder.weekday.setText(weekday);
        if(!months.get(i).getData().get(i1).getClassification2().equals("无")){
            holder.classname.setText(months.get(i).getData().get(i1).getClassification2());
        }else{
            holder.classname.setText(months.get(i).getData().get(i1).getClassification1());
        }
        holder.time.setText(months.get(i).getData().get(i1).getTime().toString());
        holder.account.setText(months.get(i).getData().get(i1).getAccount().getAccountName());
        holder.money.setText(String.valueOf(months.get(i).getData().get(i1).getMoney()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delTally(months.get(i).getData().get(i1).getId());
                HistoryActivity.getInstance().refreshData();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class SecondHolder{
        TextView year;
        TextView month;
        TextView balance;
        TextView income;
        TextView outcome;
        ImageView imageView;
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

}
