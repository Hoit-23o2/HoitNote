package com.example.hoitnote.adapters.tallies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.daimajia.swipe.SwipeLayout;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemDayMenuBinding;

import com.example.hoitnote.databinding.HzsExpandItemTallyBinding;
import com.example.hoitnote.databinding.HzsExpandItemYearMenuBinding;
import com.example.hoitnote.models.flow.HzsDayData;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        /*DayTotalHolder holder = null;
        HzsExpandItemDayMenuBinding binding = null;
        if(view == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_day_menu,viewGroup,false);
            view = binding.getRoot();
            holder = new DayTotalHolder();
            holder.binding = binding;
            view.setTag(holder);
        }else{
            holder = (DayTotalHolder)view.getTag();
        }
        holder.binding.setHzsDayData(days.get(i));
        return view;*/
        HzsSecondExpandableListViewAdapter.FirstHolder holder = null;
        HzsExpandItemYearMenuBinding binding;
        if(view == null){
            holder = new HzsSecondExpandableListViewAdapter.FirstHolder();
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_year_menu,viewGroup,false);
            view = binding.getRoot();
            holder.binding = binding;
            view.setTag(holder);
        }else{
            holder = (HzsSecondExpandableListViewAdapter.FirstHolder)view.getTag();
        }
        holder.binding.header.setText(days.get(i).getDay()+"日");
        List<String> info = new ArrayList<>();
        info.add(context.getString(R.string.account_remain_money) + days.get(i).getBalance());
        info.add(context.getString(R.string.account_income) + days.get(i).getIncome());
        info.add(context.getString(R.string.account_outcome) + days.get(i).getOutcome());
        holder.binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        HzsSecondExpandableListViewAdapter.ThirdHolder holder = null;
        HzsExpandItemTallyBinding binding = null;
        TextView delete = null;
        if(view == null){

            binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.hzs_expand_item_tally,viewGroup,false);
            holder = new HzsSecondExpandableListViewAdapter.ThirdHolder(binding);
            view = binding.getRoot();
            view.setTag(holder);

        }else{
            holder = (HzsSecondExpandableListViewAdapter.ThirdHolder)view.getTag();
        }
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewWithTag("bottom"));
        holder.binding.setTally(new HzsTally(days.get(i).getData().get(i1),HzsTally.TIME));
        final HzsSecondExpandableListViewAdapter.ThirdHolder finalHolder = holder;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delTally(days.get(i).getData().get(i1).getId());
                App.backupDataBaseHelper.addTally(days.get(i).getData().get(i1));
                days.get(i).getData().remove(i1);
                days.get(i).refreshData();
                if(days.get(i).getData().size() == 0){
                    days.remove(i);
                }
                notifyDataSetChanged();
                HistoryActivity.getInstance().refreshMainData();
                HistoryActivity.getInstance().showMainData();
            }
        });
        return view;
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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
