package com.example.hoitnote.adapters.tallies;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemTallyBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.views.flow.HistoryActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class HzsContentDayRecyclerViewAdapter extends RecyclerView.Adapter<HzsContentDayRecyclerViewAdapter.ViewHolder> {
    private List<Tally> singleDayData;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HzsExpandItemTallyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.hzs_expand_item_tally,parent,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    public HzsContentDayRecyclerViewAdapter(List<Tally> singleDayData){
        this.singleDayData = singleDayData;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Tally tally = singleDayData.get(position);

        holder.binding.setTally(new HzsTally(tally));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delTally(tally.getId());
                App.backupDataBaseHelper.addTally(tally);
                singleDayData.remove(position);
                notifyDataSetChanged();
                HistoryActivity.getInstance().refreshMainData();
            }
        });
    }

    @Override
    public int getItemCount() {
        return singleDayData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        HzsExpandItemTallyBinding binding;
        TextView delete;
        SwipeLayout swipeLayout;
        public ViewHolder(HzsExpandItemTallyBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            this.delete = (TextView)binding.getRoot().findViewById(R.id.hzs_expand_item_tally_delete);
            this.swipeLayout = (SwipeLayout) binding.getRoot().findViewById(R.id.swipe);
        }
    }
}
