package com.example.hoitnote.adapters.analysis.charts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.hoitnote.R;
import com.example.hoitnote.models.charts.ChooseScreenListNode;

import java.util.ArrayList;

public class CAMFirstScreenListAdapter extends BaseAdapter {

    private ArrayList<ChooseScreenListNode> screenArrayList;    //所有Screen以及其下级的两级列表
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<boolean[]> checkedList;
    private boolean [] firstLevelCheckedList;
    private ArrayList<String> chooseScreenList;     //用于显示等级的那个列表，根据各screen在列表中的排序更新TvShowLevel的值

    private boolean[] showMoreList;                 //用于记录是否要展开ListView

    public CAMFirstScreenListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<ChooseScreenListNode> screenArrayList) {
        this.screenArrayList = screenArrayList;
        showMoreList = new boolean[screenArrayList.size()];
        notifyDataSetChanged();
    }

    public void setCheckedList(boolean[] firstLevelCheckedList, ArrayList<boolean []> checkedList){
        this.firstLevelCheckedList = firstLevelCheckedList;
        this.checkedList = checkedList;
    }

    public void setChooseScreenList(ArrayList<String> chooseScreenList){
        this.chooseScreenList = chooseScreenList;
    }

    public void updateAllCheckedList(int fPosition,int sPosition){
        boolean [] booleans = checkedList.get(fPosition);
        int i,len = booleans.length;
        if(sPosition != -1){
            booleans[sPosition] = !booleans[sPosition];
        }
        for(i = 0;i < len;i++){
            if(booleans[i]){
                break;
            }
        }
        if(i == len){
            firstLevelCheckedList[fPosition] = false;
            if(chooseScreenList.indexOf(screenArrayList.get(fPosition).nameSign) != -1){
                chooseScreenList.remove(screenArrayList.get(fPosition).nameSign);
            }
        }else{
            firstLevelCheckedList[fPosition] = true;
            if(chooseScreenList.indexOf(screenArrayList.get(fPosition).nameSign) == -1){
                chooseScreenList.add(screenArrayList.get(fPosition).nameSign);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return screenArrayList.size();
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
            convertView = layoutInflater.inflate(R.layout.cam_list_item_choose_screen,null);
            viewHolder = new ViewHolder();
            //显示一级Screen名称的Tv相关
            viewHolder.tVScreenName = convertView.findViewById(R.id.tv_screen_name);
            viewHolder.myOnClickListenerTvShowName = new MyOnClickListenerTvShowName();
            viewHolder.tVScreenName.setOnClickListener(viewHolder.myOnClickListenerTvShowName);
            //显示一级Screen选中等级的Tv相关
            viewHolder.tVScreenLevel = convertView.findViewById(R.id.tv_screen_level);
            //显示二级具体分类的按钮相关
            viewHolder.bTShowMore = convertView.findViewById(R.id.btn_show_more);
            viewHolder.myOnClickListener = new MyOnClickListener(position);
            viewHolder.bTShowMore.setOnClickListener(viewHolder.myOnClickListener);
            //获取布局中的LinearLayout
            viewHolder.lLScreenMain = convertView.findViewById(R.id.ll_screen_main);
            //用于显示二级具体内容的ListView相关
            viewHolder.lVMore = convertView.findViewById(R.id.lv_more);
            viewHolder.CAMSecondScreenListAdapter = new CAMSecondScreenListAdapter(context);
            viewHolder.CAMSecondScreenListAdapter.setParentAdapter(this);
            viewHolder.lVMore.setAdapter(viewHolder.CAMSecondScreenListAdapter);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //重置显示一级Screen的名字
        viewHolder.tVScreenName.setText(screenArrayList.get(position).nameShow);
        //重置展开二级按钮对应的Listener中的position
        viewHolder.myOnClickListener.position = position;
        //重置显示一级Screen的TV的Listener中的position
        viewHolder.myOnClickListenerTvShowName.position = position;
        viewHolder.myOnClickListenerTvShowName.CAMSecondScreenListAdapter = viewHolder.CAMSecondScreenListAdapter;


        //是否展开二级列表
        if(showMoreList[position]){
            //展开状态设置
            viewHolder.bTShowMore.setText("收起");
            //更新二级列表ListView的适配器中的各种列表。
            viewHolder.CAMSecondScreenListAdapter.setList(screenArrayList.get(position).content);
            viewHolder.CAMSecondScreenListAdapter.setCheckedList(checkedList.get(position));
            viewHolder.CAMSecondScreenListAdapter.setFirstLevelPosition(position);

            //计算ListView应有的高度，并展开ListView
            int totalHeight = 0;
            for (int i = 0, len = viewHolder.CAMSecondScreenListAdapter.getCount(); i < len; i++) {
                View listItem = viewHolder.CAMSecondScreenListAdapter.getView(i, null, viewHolder.lVMore);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = viewHolder.lVMore.getLayoutParams();
            params.height = totalHeight + (viewHolder.lVMore.getDividerHeight() * (viewHolder.CAMSecondScreenListAdapter.getCount() - 1));
            viewHolder.lVMore.setLayoutParams(params);
        }else{
            //收起状态设置
            viewHolder.bTShowMore.setText("展开");
            //设置ListView的高度
            ViewGroup.LayoutParams params = viewHolder.lVMore.getLayoutParams();
            params.height = 0;
            viewHolder.lVMore.setLayoutParams(params);
        }

        //设置一级Screen Level标记
        if(firstLevelCheckedList[position]){
            viewHolder.tVScreenLevel.setText((chooseScreenList.indexOf(screenArrayList.get(position).nameSign)+1)+"级分类");
        }else{
            viewHolder.tVScreenLevel.setText("");
        }
        return convertView;
    }


    static class ViewHolder{
        public TextView tVScreenName,tVScreenLevel;
        public Button bTShowMore;
        public ListView lVMore;
        public LinearLayout lLScreenMain;
        public CAMSecondScreenListAdapter CAMSecondScreenListAdapter;
        public MyOnClickListener myOnClickListener;
        public MyOnClickListenerTvShowName myOnClickListenerTvShowName;
    }


    class MyOnClickListener implements View.OnClickListener{
        public int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(showMoreList[position]){
                showMoreList[position] = false;
            }else{
                showMoreList[position] = true;
            }
            notifyDataSetChanged();
        }
    }

    class MyOnClickListenerTvShowName implements View.OnClickListener{
        public int position;
        public CAMSecondScreenListAdapter CAMSecondScreenListAdapter;

        @Override
        public void onClick(View v) {
            int i,len;
            if(firstLevelCheckedList[position]){
                boolean[] booleans = checkedList.get(position);
                len = booleans.length;
                for(i = 0; i < len;i++){
                    booleans[i] = false;
                }
            }else{
                boolean[] booleans = checkedList.get(position);
                len = booleans.length;
                for(i = 0; i < len;i++){
                    booleans[i] = true;
                }
            }
            updateAllCheckedList(position,-1);
            CAMSecondScreenListAdapter.notifyDataSetChanged();
        }
    }
}
