package com.example.hoitnote.adapters.analysis.charts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;


import com.example.hoitnote.R;

import java.util.ArrayList;

public class CAMSecondScreenListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> moreArrayList = null;
    private boolean[] checkedList;
    private int firstLevelPosition;
    private CAMFirstScreenListAdapter parentAdapter;

    public CAMSecondScreenListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<String> moreArrayList){
        this.moreArrayList = moreArrayList;
        notifyDataSetChanged();
    }

    public void setCheckedList(boolean [] checkedList){
        this.checkedList = checkedList;
    }

    public void setFirstLevelPosition(int firstLevelPosition){
        this.firstLevelPosition = firstLevelPosition;
    }

    public void setParentAdapter(CAMFirstScreenListAdapter parentAdapter){
        this.parentAdapter = parentAdapter;
    }

    @Override
    public int getCount() {
        if(moreArrayList == null) return 0;
        else return moreArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.cam_list_item_choose_2level_screen,null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.cb_choose_2levelScreen);
            viewHolder.myOnClickListener = new MyOnClickListener(position);
            viewHolder.checkBox.setOnClickListener(viewHolder.myOnClickListener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.checkBox.setText(moreArrayList.get(position));
        viewHolder.myOnClickListener.position = position;
        if(checkedList[position]){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }


    static class ViewHolder{
        public CheckBox checkBox;
        public MyOnClickListener myOnClickListener;
    }


    class MyOnClickListener implements View.OnClickListener{
        public int position;
        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            parentAdapter.updateAllCheckedList(firstLevelPosition,position);
            notifyDataSetChanged();
        }
    }
}
