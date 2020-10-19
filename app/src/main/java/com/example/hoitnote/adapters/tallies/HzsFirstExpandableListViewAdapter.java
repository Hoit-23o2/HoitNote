package com.example.hoitnote.adapters.tallies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.hoitnote.R;
import com.example.hoitnote.models.flow.HzsYearData;
import com.example.hoitnote.views.flow.HzsCustomExpandableListView;

import java.util.List;

public class HzsFirstExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "HzsFirstExpandableListV";
    private List<HzsYearData> years;
    private Context context;
    private LayoutInflater inflater;

    public HzsFirstExpandableListViewAdapter(List<HzsYearData> years, Context context){
        this.years = years;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return years.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return years.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return years.get(i).getMonthDataList().get(i1);
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
        FirstHolder holder = null;
        if(view == null){
            holder = new FirstHolder();
            view = inflater.inflate(R.layout.hzs_expand_item_sub_menu,viewGroup,false);
            holder.balance = (TextView)view.findViewById(R.id.hzs_expand_item_sub_menu_balance);
            holder.year = (TextView)view.findViewById(R.id.hzs_expand_item_sub_menu_header);
            view.setTag(holder);
        }else{
            holder = (FirstHolder)view.getTag();
        }
        holder.year.setText(years.get(i).getYear() + "年");
        holder.balance.setText(years.get(i).getBalance());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        HzsCustomExpandableListView lv = ((HzsCustomExpandableListView)view);
        if(lv == null){
            lv = new HzsCustomExpandableListView(context);
        }
        HzsSecondExpandableListViewAdapter secondAdapter = new HzsSecondExpandableListViewAdapter(years.get(i).getMonthDataList(),context);
        lv.setAdapter(secondAdapter);
        return lv;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class FirstHolder{
        TextView year;
        TextView balance;
    }
}
