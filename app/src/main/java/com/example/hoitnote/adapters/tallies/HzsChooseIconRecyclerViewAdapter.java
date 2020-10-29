package com.example.hoitnote.adapters.tallies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsIconItemBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.views.flow.ChooseIconActivity;

import java.util.ArrayList;
import java.util.List;

public class HzsChooseIconRecyclerViewAdapter extends RecyclerView.Adapter<HzsChooseIconRecyclerViewAdapter.ViewHolder>  {
    List<String> iconTextList = new ArrayList<>();
    private Context context;

    public HzsChooseIconRecyclerViewAdapter(List<String> iconTextList, Context context){
        this.iconTextList = iconTextList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HzsIconItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.hzs_icon_item,parent,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String iconText = iconTextList.get(position);
        holder.binding.iconImage.setText(iconText);
        holder.binding.iconImage.setBackgroundColor(ThemeHelper.generateColor(context));
        holder.binding.iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("icon_code", iconText);
                ((ChooseIconActivity)context).setResult(Constants.ChooseIconResultCode,i);
                ((ChooseIconActivity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconTextList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        HzsIconItemBinding binding;
        public ViewHolder(HzsIconItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
