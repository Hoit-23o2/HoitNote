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
import com.example.hoitnote.models.TallyAnalysisPc;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;
import java.util.Stack;

public class CAMListAdapterPc extends BaseAdapter {

    private ChartAnalysisManager chartAnalysisManager;

    private ArrayList<TallyAnalysisPc> tallyAnalysisPcArrayList = null;
    private ArrayList<TallyAnalysisPc> nowShowList = null;
    private Context context;
    private LayoutInflater layoutInflater;

    Stack<ArrayList<TallyAnalysisPc>> tallyAnalysisPcArrayListStack;            //回退栈

    public CAMListAdapterPc(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        tallyAnalysisPcArrayListStack = new Stack<>();
    }

    public void setList(ArrayList<TallyAnalysisPc> tallyAnalysisPcArrayList){
        this.tallyAnalysisPcArrayList = tallyAnalysisPcArrayList;
        reLoad();
    }

    public void setManager(ChartAnalysisManager chartAnalysisManager){
        this.chartAnalysisManager = chartAnalysisManager;
    }

    public void enterTallyAnalysisPc(TallyAnalysisPc tallyAnalysisPc){
        tallyAnalysisPcArrayListStack.push(this.nowShowList);
        this.nowShowList = tallyAnalysisPc.nextScreen;
        notifyDataSetChanged();
    }

    public void goBack(){
        if(!tallyAnalysisPcArrayListStack.isEmpty()){
            nowShowList = tallyAnalysisPcArrayListStack.pop();
            notifyDataSetChanged();
        }
    }

    public void reLoad(){
        nowShowList = this.tallyAnalysisPcArrayList;
        while(!this.tallyAnalysisPcArrayListStack.empty()){
            this.tallyAnalysisPcArrayListStack.pop();
        }
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        if(nowShowList != null){
            return nowShowList.size();
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
            convertView = layoutInflater.inflate(R.layout.cam_list_item_pc_screen,null);
            viewHolder = new ViewHolder();
            viewHolder.view = (View)convertView.findViewById(R.id.v_color);
            viewHolder.classification = (TextView)convertView.findViewById(R.id.tv_screen_name);
            viewHolder.amountsAndScale = (TextView) convertView.findViewById(R.id.tv_screen_aands);
            viewHolder.myOnClickListener = new MyOnClickListener();
            convertView.setOnClickListener(viewHolder.myOnClickListener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mPosition = position;
        viewHolder.classification.setText(nowShowList.get(position).signName);
        viewHolder.myOnClickListener.position = position;
        String str1 = String.valueOf(nowShowList.get(viewHolder.mPosition).allMoney);
        String str2 = String.valueOf(nowShowList.get(viewHolder.mPosition).percent * 100);

        int loca = str2.indexOf(".");
        if(loca != -1){
            if(str2.length() - 1 - loca >= 2){
                str2 = str2.substring(0,str2.indexOf(".")+3);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(str1);
        sb.append("元 \\ ");
        sb.append(str2);
        sb.append("%");
        viewHolder.amountsAndScale.setText(sb.toString());
        viewHolder.view.setBackgroundColor(nowShowList.get(viewHolder.mPosition).selfColor);
        return convertView;
    }

    class ViewHolder{
        public View view;
        public TextView classification,amountsAndScale;
        public int mPosition;
        public MyOnClickListener myOnClickListener;
    }

    class MyOnClickListener implements View.OnClickListener{
        public int position;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            TallyAnalysisPc tallyAnalysisPc = nowShowList.get(position);
            if(tallyAnalysisPc.nextScreen != null){
                chartAnalysisManager.notifyEnterTallyAnalysisPc(tallyAnalysisPc,tallyAnalysisPc.nextScreen.get(0).screen);
            }
        }
    }
}
