package com.example.hoitnote.adapters.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ItemTallyBinding;
import com.example.hoitnote.viewmodels.TallyViewModel;

import java.util.ArrayList;

public class TallyRecentAdapter extends ArrayAdapter<TallyViewModel> {
    ItemTallyBinding binding;
    ArrayList<TallyViewModel> tallies;
    Context context;

    public TallyRecentAdapter(@NonNull Context context, ArrayList<TallyViewModel> tallies) {
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
    }
}
