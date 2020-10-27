package com.example.hoitnote.adapters.tallies;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemNewMonthMenuBinding;
import com.example.hoitnote.databinding.HzsExpandItemYearMenuBinding;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.models.flow.HzsYearData;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HzsFirstExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "HzsFirstExpandableListV";
    private List<HzsYearData> years;
    private Context context;
    private LayoutInflater inflater;
    private List<FirstHolder> firstHolders = new ArrayList<>();
    private HashSet<Integer> visitedFirstHolders = new HashSet<>();

    private List<List<SecondHolder>> secondHolders = new ArrayList<>();
    private List<HashSet<Integer>> visitedSecondHolders = new ArrayList<>();
    public List<HzsYearData> getYears() {
        return years;
    }

    public List<FirstHolder> getFirstHolders() {
        return firstHolders;
    }
    public List<List<SecondHolder>> getSecondHolders() {
        return secondHolders;
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
        if(!visitedFirstHolders.contains(i)) {
            visitedFirstHolders.add(i);
            firstHolders.add(i, holder);
        }
        holder.binding.header.setText(getYears().get(i).getYear()+"年");
        List<String> info = new ArrayList<>();
        info.add(context.getString(R.string.account_remain_money) + getYears().get(i).getBalance());
        info.add(context.getString(R.string.account_income) + getYears().get(i).getIn().toString());
        info.add(context.getString(R.string.account_outcome) + getYears().get(i).getOut().toString());
        holder.binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        /*HzsCustomExpandableListView lv = ((HzsCustomExpandableListView)view);
        if(lv == null){
            lv = new HzsCustomExpandableListView(context);
        }
        HzsSecondExpandableListViewAdapter secondAdapter = new HzsSecondExpandableListViewAdapter(years.get(i).getMonthDataList(),context, i,i1,this);
        lv.setAdapter(secondAdapter);
        return lv;*/
        SecondHolder holder = null;
        HzsExpandItemNewMonthMenuBinding binding = null;
        if(view == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_new_month_menu,viewGroup,false);
            view = binding.getRoot();
            holder = new SecondHolder();
            holder.binding = binding;
            view.setTag(holder);
        }else{
            holder = (SecondHolder)view.getTag();
        }
        holder.binding.setHzsMonthData(getYears().get(i).getMonthDataList().get(i1));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.binding.recyclerView.setLayoutManager(linearLayoutManager);

        final SecondHolder finalHolder = holder;
        holder.binding.expandableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!finalHolder.binding.expandableLayout.isExpanded()){
                    finalHolder.binding.expandableLayout.expand();
                    finalHolder.binding.colorBar.setVisibility(View.VISIBLE);
                }else{
                    finalHolder.binding.expandableLayout.collapse();
                    //finalHolder.binding.colorBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.binding.expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                //Log.i(TAG, "onExpansionUpdate: "+expansionFraction+" "+state);
                if(state == 0){
                    finalHolder.binding.colorBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        List<String> info = new ArrayList<>();
        info.add(context.getString(R.string.account_remain_money) + getYears().get(i).getMonthDataList().get(i1).getBalance());
        info.add(context.getString(R.string.account_income) + getYears().get(i).getMonthDataList().get(i1).getIncome());
        info.add(context.getString(R.string.account_outcome) + getYears().get(i).getMonthDataList().get(i1).getOutcome());
        holder.binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        if(visitedSecondHolders.size() <= i){

            while(visitedSecondHolders.size() <= i){
                HashSet<Integer> hashSet = new HashSet<>();
                visitedSecondHolders.add(hashSet);
                List<SecondHolder> holders = new ArrayList<>();
                secondHolders.add(holders);
            }
            visitedSecondHolders.get(i).add(i1);
            secondHolders.get(i).add(holder);
        }else if(!visitedSecondHolders.get(i).contains(i1)){
            visitedSecondHolders.get(i).add(i1);
            secondHolders.get(i).add(holder);
        }
        HzsContentDayRecyclerViewAdapter adapter = new HzsContentDayRecyclerViewAdapter(getYears().get(i).getMonthDataList().get(i1).getData(), HzsTally.DATE, this,
                years.get(i),years.get(i).getMonthDataList().get(i1),firstHolders.get(i),secondHolders.get(i),secondHolders.get(i).get(i1),context);
        holder.binding.recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class FirstHolder{
        HzsExpandItemYearMenuBinding binding;
    }
    static class SecondHolder{
        HzsExpandItemNewMonthMenuBinding binding;
    }
    public void refreshData(){
        visitedFirstHolders.clear();
        visitedSecondHolders.clear();
        for(int i = 0; i< firstHolders.size(); i++){
            visitedFirstHolders.add(i);
            firstHolders.get(i).binding.header.setText(getYears().get(i).getYear()+"年");
            List<String> info = new ArrayList<>();
            info.add(context.getString(R.string.account_remain_money) + getYears().get(i).getBalance());
            info.add(context.getString(R.string.account_income) + getYears().get(i).getIn().toString());
            info.add(context.getString(R.string.account_outcome) + getYears().get(i).getOut().toString());
            firstHolders.get(i).binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
            HashSet<Integer> hashSet = new HashSet<>();
            visitedSecondHolders.add(hashSet);
            for(int j=0;j<secondHolders.get(i).size();j++){
                visitedSecondHolders.get(i).add(j);
                secondHolders.get(i).get(j).binding.setHzsMonthData(getYears().get(i).getMonthDataList().get(j));
                List<String> info1 = new ArrayList<>();
                info1.add(context.getString(R.string.account_remain_money) + getYears().get(i).getMonthDataList().get(j).getBalance());
                info1.add(context.getString(R.string.account_income) + getYears().get(i).getMonthDataList().get(j).getIncome());
                info1.add(context.getString(R.string.account_outcome) + getYears().get(i).getMonthDataList().get(j).getOutcome());
                secondHolders.get(i).get(j).binding.tipsTextView.startWithList(info1, R.anim.anim_bottom_in, R.anim.anim_top_out);
            }
        }
    }
}
