package com.example.hoitnote.adapters.tallies;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemYearMenuBinding;
import com.example.hoitnote.models.flow.HzsYearData;
import com.example.hoitnote.views.flow.HzsCustomExpandableListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HzsFirstExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "HzsFirstExpandableListV";
    private List<HzsYearData> years;
    private Context context;
    private LayoutInflater inflater;
    private List<FirstHolder> holders = new ArrayList<>();
    private HashSet<Integer> visitedHolders = new HashSet<>();
    public List<HzsYearData> getYears() {
        return years;
    }

    public List<FirstHolder> getHolders() {
        return holders;
    }
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
        HzsExpandItemYearMenuBinding binding;
        if(view == null){
            holder = new FirstHolder();
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_year_menu,viewGroup,false);
            view = binding.getRoot();
            holder.binding = binding;
            view.setTag(holder);
        }else{
            holder = (FirstHolder)view.getTag();
        }
        holder.binding.setHzsYearData(years.get(i));
        if(!visitedHolders.contains(i)) {
            visitedHolders.add(i);
            holders.add(i, holder);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        HzsCustomExpandableListView lv = ((HzsCustomExpandableListView)view);
        if(lv == null){
            lv = new HzsCustomExpandableListView(context);
        }
        HzsSecondExpandableListViewAdapter secondAdapter = new HzsSecondExpandableListViewAdapter(years.get(i).getMonthDataList(),context, i,i1,this);
        lv.setAdapter(secondAdapter);
        return lv;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class FirstHolder{
        HzsExpandItemYearMenuBinding binding;
    }

    public void refreshData(){
        visitedHolders.clear();
        for(int i=0;i<holders.size();i++){
            holders.get(i).binding.setHzsYearData(years.get(i));
            visitedHolders.add(i);
        }
    }
}
