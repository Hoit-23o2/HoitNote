package com.example.hoitnote.customviews;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.hoitnote.R;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;

public class CAMListAdapterTdCl extends BaseAdapter {

    private ChartAnalysisManager chartAnalysisManager;
    private ArrayList<String> timeDivisionList;

    private Context context;
    private LayoutInflater layoutInflater;

    private String nowTimeDivision;


    public CAMListAdapterTdCl(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public void setList(ArrayList<String> timeDivisionList){
        this.timeDivisionList = timeDivisionList;
        nowTimeDivision = "日";
        reLoad();
    }

    public void setManager(ChartAnalysisManager chartAnalysisManager){
        this.chartAnalysisManager = chartAnalysisManager;
    }

    public void changeTimeDivision(String string){
        nowTimeDivision = string;
        notifyDataSetChanged();
    }

    public void reLoad(){
        changeTimeDivision("日");
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if(timeDivisionList != null){
            return timeDivisionList.size();
        }else{
            return 0;
        }
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.cam_list_item_cl_time_division,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTimeDivision = convertView.findViewById(R.id.tv_time_division);
            viewHolder.myOnClickListener = new MyOnClickListener();
            convertView.setOnClickListener(viewHolder.myOnClickListener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mPosition = position;
        viewHolder.myOnClickListener.position = position;
        viewHolder.textViewTimeDivision.setText(timeDivisionList.get(position));
        return convertView;
    }

    class ViewHolder{
        public TextView textViewTimeDivision;
        public int mPosition;
        public MyOnClickListener myOnClickListener;
    }

    class MyOnClickListener implements View.OnClickListener{
        public int position;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            String string = timeDivisionList.get(position);
            if(!nowTimeDivision.equals(string)){
                chartAnalysisManager.notifyTimeDivision(string);
            }
        }
    }
}
