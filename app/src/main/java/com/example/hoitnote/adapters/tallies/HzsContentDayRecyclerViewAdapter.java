package com.example.hoitnote.adapters.tallies;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoitnote.R;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class HzsContentDayRecyclerViewAdapter extends RecyclerView.Adapter<HzsContentDayRecyclerViewAdapter.ViewHolder> {
    private List<Tally> singleDayData;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hzs_expand_item_tally,parent,false);
        ViewHolder holder = new ViewHolder(view,parent);
        return holder;
    }

    public HzsContentDayRecyclerViewAdapter(List<Tally> singleDayData){
        this.singleDayData = singleDayData;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Tally tally = singleDayData.get(position);
        holder.day.setText(String.valueOf(tally.getDate().getDate()));
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        String weekday = formatter.format(tally.getDate().getDate());
        holder.weekday.setText(weekday);
        if(!tally.getClassification2().equals("无")){
            holder.classname.setText(tally.getClassification2());
        }else{
            holder.classname.setText(tally.getClassification1());
        }
        holder.time.setText(tally.getTime().toString());
        holder.account.setText(tally.getAccount().getAccountName());
        holder.money.setText(String.valueOf(tally.getMoney()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                App.dataBaseHelper.delTally(tally.getId());
                HistoryActivity.getInstance().refreshData();
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleDayData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        ViewGroup parent;
        TextView day;
        TextView weekday;
        ImageView icon;
        TextView classname;
        TextView time;
        TextView account;
        TextView money;
        TextView delete;
        public ViewHolder(View view, ViewGroup parent){
            super(view);
            this.view = view;
            this.parent = parent;
            day = (TextView)view.findViewById(R.id.hzs_expand_item_tally_day);
            weekday = (TextView)view.findViewById(R.id.hzs_expand_item_tally_weekday);
            icon = (ImageView)view.findViewById(R.id.hzs_expand_item_tally_icon);
            classname = (TextView)view.findViewById(R.id.hzs_expand_item_tally_classname);
            time = (TextView)view.findViewById(R.id.hzs_expand_item_tally_time);
            account = (TextView)view.findViewById(R.id.hzs_expand_item_tally_account);
            money = (TextView)view.findViewById(R.id.hzs_expand_item_tally_money);
            delete = (TextView)view.findViewById(R.id.hzs_expand_item_tally_delete);
        }
    }
}
