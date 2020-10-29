package com.example.hoitnote.adapters.tallies;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemAddClass1Binding;
import com.example.hoitnote.databinding.HzsExpandItemAddClass2Binding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.views.flow.AddOptionActivity;
import com.example.hoitnote.views.flow.ManageClassActivity;

import java.util.List;

public class HzsAddClassExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    public List<String> classifications1;
    public List<List<String>> classifications2;
    private ActionType actionType;
    private IconType class1Type;
    private IconType class2Type;
    public HzsAddClassExpandableListViewAdapter(BookingType bookingType, Context context){
        classifications1 = BookingDataHelper.getClassifications1(bookingType);
        classifications2 = BookingDataHelper.getClassifications2(bookingType);
        for(List<String> subList:classifications2){
            subList.add("添加二级分类");
        }
        this.context = context;
        switch (bookingType){
            case OUTCOME:
                actionType = ActionType.OUTCOME;
                class1Type = IconType.OUTCOMECLASS1;
                class2Type = IconType.OUTCOMECLASS2;
                break;
            case INCOME:
                actionType = ActionType.INCOME;
                class1Type = IconType.INCOMECLASS1;
                class2Type = IconType.INCOMECLASS2;
                break;
            case TRANSFER:
                actionType = ActionType.TRANSFER;
                break;
        }
    }
    @Override
    public int getGroupCount() {
        return classifications1.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return classifications2.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return classifications1.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return classifications2.get(i).get(i1);
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
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        FirstHolder holder = null;
        HzsExpandItemAddClass1Binding binding = null;
        if(view == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.hzs_expand_item_add_class1,viewGroup,false);
            holder = new FirstHolder(binding);
            view = binding.getRoot();
            view.setTag(holder);

        }else{
            holder = (FirstHolder)view.getTag();
        }
        holder.binding.setClassification(classifications1.get(i));
        holder.binding.classIcon.setText(App.dataBaseHelper.getIconInformation(classifications1.get(i),class1Type));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delClassification1(classifications1.get(i),actionType);
                classifications1.remove(i);
                classifications2.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if(i1 == classifications2.get(i).size()-1){
            view = LayoutInflater.from(context).inflate(R.layout.hzs_expand_item_add_class3,viewGroup,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,"hhhh",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddOptionActivity.class);
                    intent.putExtra("id",i);
                    intent.putExtra("icon_type","二级分类");
                    intent.putExtra("classification1",classifications1.get(i));
                    intent.putExtra("action_type",actionType.ordinal());
                    ((ManageClassActivity)context).startActivityForResult(intent,1);
                }
            });
        }else{
            SecondHolder holder = null;
            HzsExpandItemAddClass2Binding binding = null;
            if(view == null){
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.hzs_expand_item_add_class2,viewGroup,false);
                holder = new SecondHolder(binding);
                view = binding.getRoot();
                view.setTag(holder);
            }else{
                holder = (SecondHolder) view.getTag();
            }

            if(holder == null){
                binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.hzs_expand_item_add_class2,viewGroup,false);
                holder = new SecondHolder(binding);
                view = binding.getRoot();
                view.setTag(holder);
            }

            holder.binding.setClassification(classifications2.get(i).get(i1));
            holder.binding.classIcon.setText(App.dataBaseHelper.getIconInformation(classifications1.get(i)+classifications2.get(i).get(i1), class2Type));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.dataBaseHelper.delClassification2(classifications1.get(i),classifications2.get(i).get(i1),actionType);
                    classifications2.get(i).remove(i1);
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    static class FirstHolder{
        HzsExpandItemAddClass1Binding binding;
        public TextView delete;
        public FirstHolder(HzsExpandItemAddClass1Binding binding){
            this.binding = binding;
            delete = binding.getRoot().findViewById(R.id.delete_button);
        }
    }

    static class SecondHolder{
        HzsExpandItemAddClass2Binding binding;
        public TextView delete;
        public SecondHolder(HzsExpandItemAddClass2Binding binding){
            this.binding = binding;
            delete = binding.getRoot().findViewById(R.id.delete_button);
        }
    }
    public void addClassificaition2(int index, String classification2){
        classifications2.get(index).add(classifications2.get(index).size()-1,classification2);
        notifyDataSetChanged();
    }
}
