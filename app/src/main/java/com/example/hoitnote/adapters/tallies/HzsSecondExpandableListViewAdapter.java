package com.example.hoitnote.adapters.tallies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.daimajia.swipe.SwipeLayout;
import com.example.hoitnote.R;

import com.example.hoitnote.databinding.HzsExpandItemTallyBinding;
import com.example.hoitnote.databinding.HzsExpandItemYearMenuBinding;
import com.example.hoitnote.models.flow.HzsMonthData;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;


import java.util.ArrayList;
import java.util.List;


public class HzsSecondExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "HzsSecondExpandableList";
    private List<HzsMonthData> months;
    private Context context;
    private LayoutInflater inflater;
    int id1;
    int id2;
    private HzsFirstExpandableListViewAdapter parentAdapter;
    public HzsSecondExpandableListViewAdapter(List<HzsMonthData> months, Context context){
        this.months = months;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    public HzsSecondExpandableListViewAdapter(List<HzsMonthData> months, Context context, int id1,
                                              int id2, HzsFirstExpandableListViewAdapter parentAdapter){
        this.months = months;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.parentAdapter = parentAdapter;
        this.id1 = id1;
        this.id2 = id2;
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
        holder.binding.header.setText(months.get(i).getYear()+"年"+months.get(i).getMonth()+"月");
        List<String> info = new ArrayList<>();
        info.add(context.getString(R.string.account_remain_money) + months.get(i).getBalance());
        info.add(context.getString(R.string.account_income) + months.get(i).getIncome());
        info.add(context.getString(R.string.account_outcome) + months.get(i).getOutcome());
        holder.binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        return view;
        /*SecondHolder holder = null;
        HzsExpandItemMonthMenuBinding binding = null;
        if(view == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_month_menu,viewGroup,false);
            view = binding.getRoot();
            holder = new SecondHolder();
            holder.binding = binding;
            view.setTag(holder);
        }else{
            holder = (SecondHolder)view.getTag();
        }
        holder.binding.setHzsMonthData(months.get(i));
        return view;*/
    }
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        ThirdHolder holder = null;
        HzsExpandItemTallyBinding binding = null;
        TextView delete = null;
        if(view == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_tally,viewGroup,false);
            holder = new ThirdHolder(binding);
            view = binding.getRoot();
            view.setTag(holder);
        }else{
            holder = (ThirdHolder)view.getTag();
        }
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewWithTag("bottom"));
        holder.binding.setTally(new HzsTally(months.get(i).getData().get(i1),HzsTally.DAYANDTIME));
        final ThirdHolder finalHolder = holder;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delTally(months.get(i).getData().get(i1).getId());
                App.backupDataBaseHelper.addTally(months.get(i).getData().get(i1));
                months.get(i).getData().remove(i1);
                months.get(i).refreshData();
                if(parentAdapter != null){
                    parentAdapter.refreshData();
                }
                if(months.get(i).getData().size() == 0){
                    months.remove(i);
                    if(parentAdapter != null){
                        if(parentAdapter.getYears().get(id1).getMonthDataList().size() == 0){
                            parentAdapter.getYears().remove(id1);
                            parentAdapter.getFirstHolders().remove(id1);
                        }
                        parentAdapter.notifyDataSetChanged();
                    }
                }
                notifyDataSetChanged();
                HistoryActivity.getInstance().refreshMainData();
                HistoryActivity.getInstance().showMainData();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class FirstHolder{
        HzsExpandItemYearMenuBinding binding;
    }
    static class ThirdHolder{
        HzsExpandItemTallyBinding binding;
        TextView delete;
        SwipeLayout swipeLayout;
        public ThirdHolder(HzsExpandItemTallyBinding binding){
            this.binding = binding;
            this.delete = (TextView)binding.getRoot().findViewById(R.id.hzs_expand_item_tally_delete);
            this.swipeLayout = (SwipeLayout) binding.getRoot().findViewById(R.id.swipe);
        }
    }

}
