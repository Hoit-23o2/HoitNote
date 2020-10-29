package com.example.hoitnote.adapters.tallies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoitnote.R;

import java.util.List;

public class HzsListViewAdapter extends ArrayAdapter {
    private final int resourceId;
    public HzsListViewAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String string = (String)getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.data = (TextView)view.findViewById(R.id.hzs_simple_item_data);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }


        viewHolder.data.setText(string);
        return view;
    }
    static class ViewHolder{
        TextView data;
    }
}
