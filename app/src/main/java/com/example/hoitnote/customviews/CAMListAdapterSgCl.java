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
import com.example.hoitnote.models.TallyAnalysisCl;
import com.example.hoitnote.models.TallyAnalysisPc;

import java.util.ArrayList;
import java.util.Stack;

public class CAMListAdapterSgCl extends BaseAdapter {

    private ChartAnalysisManager chartAnalysisManager;

    private ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList = null;
    private Context context;
    private LayoutInflater layoutInflater;

    Stack<ArrayList<TallyAnalysisPc>> tallyAnalysisPcArrayListStack;            //回退栈

    public CAMListAdapterSgCl(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        tallyAnalysisPcArrayListStack = new Stack<>();
    }

    public void setList(ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList){
        this.tallyAnalysisClArrayList = tallyAnalysisClArrayList;
        reLoad();
    }

    public void setManager(ChartAnalysisManager chartAnalysisManager){
        this.chartAnalysisManager = chartAnalysisManager;
    }

    public void reLoad(){
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        if(tallyAnalysisClArrayList != null){
            return tallyAnalysisClArrayList.size();
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
            convertView = layoutInflater.inflate(R.layout.cam_list_item_cl_sign,null);
            viewHolder = new ViewHolder();
            viewHolder.view = (View)convertView.findViewById(R.id.v_color);
            viewHolder.signName = (TextView)convertView.findViewById(R.id.tv_sign_name);
            viewHolder.myOnClickListener = new MyOnClickListener();
            convertView.setOnClickListener(viewHolder.myOnClickListener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mPosition = position;
        viewHolder.signName.setText(tallyAnalysisClArrayList.get(position).signName);
        viewHolder.myOnClickListener.position = position;
        viewHolder.view.setBackgroundColor(tallyAnalysisClArrayList.get(position).selfColor);
        return convertView;
    }

    class ViewHolder{
        public View view;
        public TextView signName;
        public int mPosition;
        public MyOnClickListener myOnClickListener;
    }

    class MyOnClickListener implements View.OnClickListener{
        public int position;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            TallyAnalysisCl tallyAnalysisCl = tallyAnalysisClArrayList.get(position);
            chartAnalysisManager.notifyMainSignChange(tallyAnalysisCl);
        }
    }
}
