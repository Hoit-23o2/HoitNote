package com.example.hoitnote.adapters.tallies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsIconItemBinding;
import com.example.hoitnote.databinding.HzsOptionItemBinding;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.enums.ThirdPartyType;
import com.example.hoitnote.views.flow.HistoryActivity;

import java.util.List;

public class HzsAddOptionRecyclerViewAdapter extends RecyclerView.Adapter<HzsAddOptionRecyclerViewAdapter.ViewHolder> {

    public List<String> getOptionList() {
        return optionList;
    }

    private List<String> optionList;
    private Context context;
    private IconType iconType;
    private ThirdPartyType thirdPartyType;
    public HzsAddOptionRecyclerViewAdapter(IconType iconType, Context context){
        this.context = context;
        this.iconType = iconType;
        switch (iconType){
            case PROJECT:
                this.thirdPartyType = ThirdPartyType.PROJECT;
                break;
            case MEMBER:
                this.thirdPartyType = ThirdPartyType.MEMBER;
                break;
            case VENDOR:
                this.thirdPartyType = ThirdPartyType.VENDOR;
                break;
        }
        this.optionList = App.dataBaseHelper.getThirdParties(thirdPartyType);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HzsOptionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hzs_option_item,parent,false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.binding.classIcon.setText(App.dataBaseHelper.getIconInformation(optionList.get(position),iconType));
        holder.binding.className.setText(optionList.get(position));
        holder.binding.hzsExpandItemAddClassDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.dataBaseHelper.delThirdParty(thirdPartyType,optionList.get(position));
                optionList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        HzsOptionItemBinding binding;
        public ViewHolder(HzsOptionItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
