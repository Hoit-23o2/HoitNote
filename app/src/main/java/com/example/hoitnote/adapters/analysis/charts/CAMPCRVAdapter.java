package com.example.hoitnote.adapters.analysis.charts;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hoitnote.R;
import com.example.hoitnote.utils.helpers.ThemeHelper;

import java.util.ArrayList;


public class CAMPCRVAdapter extends  RecyclerView.Adapter<CAMPCRVAdapter.CAMPCRVViewHolder>{

    private ArrayList<String> screenList;
    private ContentValues screenContentValues;
    private Context context;
    public int nowPosition;

    public CAMPCRVAdapter(Context context) {
        this.context = context;
        screenList = null;
        nowPosition = 0;
    }

    public void setScreenContentValues(ContentValues screenContentValues){
        this.screenContentValues = screenContentValues;
    }

    public void setScreenMarkList(ArrayList<String> screenMarkList) {
        screenList = new ArrayList<>();
        if(screenMarkList == null) return;
        screenList.add("总   计");
        int len = screenMarkList.size();
        int i;
        for(i = 0; i < len;i++){
            String nowKey = screenMarkList.get(i);
            screenList.add(screenContentValues.getAsString(nowKey));
        }
        nowPosition = 0;
        notifyDataSetChanged();
    }

    public void enterOnce(){
        if(nowPosition < screenList.size() - 1){
            nowPosition += 1;
            notifyDataSetChanged();
        }
    }

    public void goBack(){
        if(nowPosition > 0){
            nowPosition -= 1;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CAMPCRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CAMPCRVViewHolder(LayoutInflater.from(context).inflate(R.layout.cam_list_item_pcrv_screen,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CAMPCRVViewHolder holder, int position) {
        holder.textView.setText(screenList.get(position));
        if(position == nowPosition) {
            holder.textView.setBackgroundColor(ThemeHelper.getAccentColor(context));
        }else{
            holder.textView.setBackgroundColor(ThemeHelper.getPrimaryColor(context));
        }
    }

    @Override
    public int getItemCount() {
        if(screenList == null){
            return 0;
        }else{
            return screenList.size();
        }
    }


    static class CAMPCRVViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public CAMPCRVViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_rv_screen_name);
        }
    }
}
