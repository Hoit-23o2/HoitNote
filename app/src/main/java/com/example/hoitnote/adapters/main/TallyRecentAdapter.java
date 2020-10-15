package com.example.hoitnote.adapters.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ItemTallyBinding;
import com.example.hoitnote.databinding.ItemTallyGroupBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.viewmodels.TallyViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class TallyRecentAdapter extends BaseExpandableListAdapter {
    ItemTallyBinding binding;
    ItemTallyGroupBinding groupBinding;
    HashMap<String, ArrayList<TallyViewModel>> talliesWithGroup;
    ArrayList<String> groupsTitle;
    Context context;


    @Override
    public int getGroupCount() {
        return groupsTitle.size();
    }

    @Override
    public int getChildrenCount(int groupId) {
        ArrayList<TallyViewModel> innerList =
                this.talliesWithGroup.get(this.groupsTitle.get(groupId));
        int count = 0;
        if(innerList != null){
            count = innerList.size();
        }
        return count;
    }

    @Override
    public Object getGroup(int groupId) {
        return this.groupsTitle.get(groupId);
    }

    @Override
    public Object getChild(int groupId, int childId) {
        ArrayList<TallyViewModel> innerList =
                this.talliesWithGroup.get(this.groupsTitle.get(groupId));
        TallyViewModel tallyViewModel = null;
        if(innerList != null){
            tallyViewModel = innerList.get(childId);
        }
        return tallyViewModel;
    }

    @Override
    public long getGroupId(int groupId) {
        return groupId;
    }

    @Override
    public long getChildId(int groupId, int childId) {
        return childId;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupId, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (isExpanded) {

        } else {

        }

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally_group,null);
            groupBinding = DataBindingUtil.bind(convertView);
            convertView.setTag(groupBinding);
        }
        else{
            groupBinding = (ItemTallyGroupBinding) convertView.getTag();
        }
        groupBinding.groupTitle.setText(this.groupsTitle.get(groupId));
        return groupBinding.getRoot();
    }

    @Override
    public View getChildView(int groupId, int childId, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally,null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }
        else{
            binding = (ItemTallyBinding) convertView.getTag();
        }

        assert binding != null;
        ArrayList<TallyViewModel> group = talliesWithGroup.get(this.groupsTitle.get(groupId));
        if(group != null){
            binding.setTally(group.get(childId));
        }
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public TallyRecentAdapter(Context context,
                              HashMap<String, ArrayList<TallyViewModel>> talliesWithGroup){
        this.context = context;
        this.talliesWithGroup = talliesWithGroup;
        this.groupsTitle = new ArrayList<>(talliesWithGroup.keySet());
    }

    public void expandAllGroup(ExpandableListView expandableListView){
        for (int groupId = 0; groupId < getGroupCount(); groupId++) {
            expandableListView.expandGroup(groupId);
        }
    }

    /*public TallyRecentAdapter(@NonNull Context context, ArrayList<TallyViewModel> tallies) {
        super(context, R.layout.item_tally, tallies);
        this.tallies = tallies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tallies.size();
    }

    @Override
    public TallyViewModel getItem(int i) {
        return tallies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally,null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }
        else{
            binding = (ItemTallyBinding) convertView.getTag();
        }

        assert binding != null;
        binding.setTally(tallies.get(position));
        return binding.getRoot();
    }*/


}
