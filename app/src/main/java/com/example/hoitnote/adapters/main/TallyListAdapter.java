package com.example.hoitnote.adapters.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ItemTallyBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.viewmodels.TallyViewModel;

import java.util.ArrayList;

public class TallyListAdapter extends BaseAdapter {
    ItemTallyBinding binding;
    ArrayList<TallyViewModel> tallyViewModels;
   /* ArrayList<View> views = new ArrayList<>();*/
    Context context;
    SparseBooleanArray selectedTallysIds;

    public TallyListAdapter(Context context, ArrayList<TallyViewModel> tallyViewModels){
        this.tallyViewModels = tallyViewModels;
        this.context = context;
        selectedTallysIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return tallyViewModels.size();
    }

    @Override
    public Object getItem(int pos) {
        return tallyViewModels.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally,null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }
        else{
            binding = (ItemTallyBinding) convertView.getTag();
        }
        binding.setTallyViewModel(tallyViewModels.get(pos));
        /*views.add(binding.getRoot());*/
        return binding.getRoot();
    }

    public void remove(int pos){
        tallyViewModels.remove(pos);
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedTallysIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value){
            selectedTallysIds.put(position, true);
            /*views.get(position).setBackgroundColor(ThemeHelper.getAccentColor(context));*/
        }
        else
        {
            selectedTallysIds.delete(position);
            /*views.get(position).setBackgroundColor(Color.TRANSPARENT);*/
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedTallysIds.size();
    }

    public void removeSelection() {
        selectedTallysIds = new SparseBooleanArray();
        /*for (View view:
             views) {
            view.setBackgroundColor(Color.TRANSPARENT);
        }*/
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedTallysIds;
    }




}
