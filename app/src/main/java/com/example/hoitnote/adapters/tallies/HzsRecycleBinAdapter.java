package com.example.hoitnote.adapters.tallies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsRecyclerBinItemBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.utils.App;


import java.util.ArrayList;
import java.util.List;

public class HzsRecycleBinAdapter extends RecyclerView.Adapter<HzsRecycleBinAdapter.ViewHolder>{

    private List<Tally> singleDayData;

    public List<ViewHolder> getHolders() {
        return holders;
    }

    private List<ViewHolder> holders = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HzsRecyclerBinItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.hzs_recycler_bin_item,parent,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    public HzsRecycleBinAdapter(List<Tally> singleDayData){
        this.singleDayData = singleDayData;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Tally tally = singleDayData.get(position);

        holder.binding.setTally(new HzsTally(tally));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.backupDataBaseHelper.delTally(tally.getId());
                singleDayData.remove(position);
                notifyDataSetChanged();
                holders.remove(position);
            }
        });
        holder.restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.backupDataBaseHelper.delTally(tally.getId());
                App.dataBaseHelper.addTally(tally);
                singleDayData.remove(position);
                notifyDataSetChanged();
                holders.remove(position);
            }
        });
        holders.add(position,holder);
    }

    @Override
    public int getItemCount() {
        return singleDayData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        HzsRecyclerBinItemBinding binding;
        TextView delete;
        TextView restore;
        SwipeLayout swipeLayout;
        public ViewHolder(HzsRecyclerBinItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            this.delete = (TextView)binding.getRoot().findViewById(R.id.delete_button);
            this.restore = (TextView)binding.getRoot().findViewById(R.id.restore_button);
            this.swipeLayout = (SwipeLayout) binding.getRoot().findViewById(R.id.swipe);
        }
    }
    public void allOpenAndChoose(){
        for(ViewHolder holder:holders){
            holder.swipeLayout.open(true,true);
            holder.binding.checkboxButton.setChecked(true);
        }
    }
    public void allOpenAndNotChoose(){
        for(ViewHolder holder:holders){
            holder.swipeLayout.open(true,true);
            holder.binding.checkboxButton.setChecked(false);
        }
    }
    public void allOpen(){
        for(ViewHolder holder:holders){
            holder.swipeLayout.open(true,true);
        }
    }

    public void allClose(){
        for(ViewHolder holder:holders){
            holder.swipeLayout.close(true,true);
        }
    }

    public void restoreAllChosed(){
        for(int i=0;i<holders.size();i++){
            boolean bool = holders.get(i).binding.checkboxButton.isChecked();
            if(bool){
                Tally tally = singleDayData.get(i);
                App.backupDataBaseHelper.delTally(tally.getId());
                App.dataBaseHelper.addTally(tally);
                ViewHolder holder = holders.get(i);
                tally = null;
                holder = null;
            }
        }
        while (singleDayData.remove(null));
        while (holders.remove(null));
        notifyDataSetChanged();
    }

    public void deleteAllChosed(){
        for(int i=0;i<holders.size();i++){
            boolean bool = holders.get(i).binding.checkboxButton.isChecked();
            if(bool){
                Tally tally = singleDayData.get(i);
                App.backupDataBaseHelper.delTally(tally.getId());
                ViewHolder holder = holders.get(i);
                tally = null;
                holder = null;
            }
        }
        while (singleDayData.remove(null));
        while (holders.remove(null));
        notifyDataSetChanged();
    }
}
