package com.example.hoitnote.views.flow;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;

import com.example.hoitnote.adapters.tallies.HzsContentDayRecyclerViewAdapter;
import com.example.hoitnote.adapters.tallies.HzsFirstExpandableListViewAdapter;
import com.example.hoitnote.adapters.tallies.HzsSecondExpandableListViewAdapter;
import com.example.hoitnote.adapters.tallies.HzsThirdExpandableListViewAdapter;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.flow.HzsDayData;
import com.example.hoitnote.models.flow.HzsMonthData;
import com.example.hoitnote.models.flow.HzsYearData;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;

import com.example.hoitnote.utils.enums.ActionType;


import java.sql.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends BaseActivity{
    private static final String TAG = "HistoryActivity";

    private LinearLayout contentLayout;
    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH)+1;
    private int day = calendar.get(Calendar.DATE);
    private String season = "秋";
    private String classification1 = "";
    private String classification2 = "";

    private static HistoryActivity instance;
    private int mode = 0;
    public static final int TOTAL = 0;
    public static final int YEAR = 1;
    public static final int SEASON = 2;
    public static final int MONTH = 3;
    public static final int DAY = 4;
    public int getYear() {
        return year;
    }

    public String getSeason() {
        return season;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        instance = this;
        contentLayout = (LinearLayout)findViewById(R.id.hzs_history_content);
        actionBarInit();


        showDataAsTotal();

        initBottomTimeframeButton();
        initBottomClassButton();
    }

    public static HistoryActivity getInstance() {
        return instance;
    }

    public void refreshMainData(){
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        switch (mode){
            case TOTAL:
                List<HzsYearData> totalData = getTotalData();
                for(HzsYearData data:totalData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case YEAR:
                List<HzsMonthData> yearData = getYearData(year-1900);
                for(HzsMonthData data:yearData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case SEASON:
                List<HzsMonthData> seasonData = getSeasonData(year-1900,season);
                for(HzsMonthData data:seasonData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case MONTH:
                List<HzsDayData> monthData = getMonthData(year-1900,month-1);
                for(HzsDayData data:monthData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case DAY:
                List<Tally> dayData = getDayData(year-1900,month-1,day);
                for(Tally data:dayData){
                    if(data.getActionType() == ActionType.OUTCOME){
                        bal -= data.getMoney();
                        out += data.getMoney();
                    }else{
                        bal += data.getMoney();
                        in += data.getMoney();
                    }
                }
                break;
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showMainData(String balance,String outcome,String income){
        TextView balanceTextView = findViewById(R.id.hzs_history_balance);
        TextView outcomeTextView = findViewById(R.id.hzs_history_outcome);
        TextView incomeTextView = findViewById(R.id.hzs_history_income);
        balanceTextView.setText(balance);
        outcomeTextView.setText(outcome);
        incomeTextView.setText(income);
    }
    private void showDataAsTotal(){
        contentLayout.removeAllViews();
        List<HzsYearData> yearData = getTotalData();
        View totalContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_total,contentLayout,false);
        HzsFirstExpandableListViewAdapter adapter = new HzsFirstExpandableListViewAdapter(yearData,this);
        ExpandableListView expandableListView = (ExpandableListView)totalContentView.findViewById(R.id.hzs_history_content_as_total_expandable_listview);
        expandableListView.setAdapter(adapter);
        contentLayout.addView(totalContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsYearData data:yearData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsYear(){
        contentLayout.removeAllViews();
        List<HzsMonthData> monthData = getYearData(year-1900);
        View yearContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_year,contentLayout,false);
        ((Button)yearContentView.findViewById(R.id.hzs_history_content_as_year_year)).setText(String.valueOf(year)+"年");
        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(monthData,this);
        ExpandableListView yearListView = (ExpandableListView)yearContentView.findViewById(R.id.hzs_history_content_as_year_expandable_listview);
        yearListView.setAdapter(adapter);
        initYearContentButton(yearContentView);
        contentLayout.addView(yearContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsMonthData data:monthData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsSeason(){
        contentLayout.removeAllViews();
        List<HzsMonthData> monthData = getSeasonData(year-1900,season);
        View seasonContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_season,contentLayout,false);
        ((Button)seasonContentView.findViewById(R.id.hzs_history_content_as_season_year)).setText(String.valueOf(year)+"年");
        ((Button)seasonContentView.findViewById(R.id.hzs_history_content_as_season_season)).setText(season);

        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(monthData,this);
        ExpandableListView seasonListView = (ExpandableListView)seasonContentView.findViewById(R.id.hzs_history_content_as_season_expandable_listview);
        seasonListView.setAdapter(adapter);
        initSeasonContentButton(seasonContentView);
        contentLayout.addView(seasonContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsMonthData data:monthData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsMonth(){
        contentLayout.removeAllViews();
        List<HzsDayData> dayData = getMonthData(year-1900,month-1);
        View monthContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_month,contentLayout,false);
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_year)).setText(String.valueOf(year)+"年");
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_month)).setText(String.valueOf(month)+"月");
        HzsThirdExpandableListViewAdapter adapter = new HzsThirdExpandableListViewAdapter(dayData,this);
        ExpandableListView seasonListView = (ExpandableListView)monthContentView.findViewById(R.id.hzs_history_content_as_month_expandable_listview);
        seasonListView.setAdapter(adapter);
        initMonthContentButton(monthContentView);
        contentLayout.addView(monthContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsDayData data:dayData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsDay(){
        contentLayout.removeAllViews();
        List<Tally> dayData = getDayData(year-1900,month-1,day);
        View dayContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_day,contentLayout,false);
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_year)).setText(String.valueOf(year)+"年");
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_month)).setText(String.valueOf(month)+"日");
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_day)).setText(String.valueOf(day)+"日");
        HzsContentDayRecyclerViewAdapter adapter = new HzsContentDayRecyclerViewAdapter(dayData);
        RecyclerView recyclerView = (RecyclerView)dayContentView.findViewById(R.id.hzs_history_content_as_day_recyclerview);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        initDayContentButton(dayContentView);
        contentLayout.addView(dayContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(Tally data:dayData){
            if(data.getActionType() == ActionType.OUTCOME){
                bal -= data.getMoney();
                out += data.getMoney();
            }else{
                bal += data.getMoney();
                in += data.getMoney();
            }
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void initDayContentButton(View view){
        View chooseButton = (View) view.findViewById(R.id.hzs_history_content_as_day_button);
        final TextView yearTextView = (TextView) view.findViewById(R.id.hzs_history_content_as_day_year);
        final TextView monthTextView = (TextView) view.findViewById(R.id.hzs_history_content_as_day_month);
        final TextView dayTextView = (TextView) view.findViewById(R.id.hzs_history_content_as_day_day);

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1970,0,1);
        endDate.set(2020,11,31);
        //时间选择器
        final TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date, View v) {
                yearTextView.setText(String.valueOf(date.getYear()+1900)+"年");
                monthTextView.setText(String.valueOf(date.getMonth()+1)+"月");
                dayTextView.setText(String.valueOf(date.getDate())+"日");
                year = date.getYear()+1900;
                month = date.getMonth()+1;
                day = date.getDate();
                showDataAsDay();
            }
        }).setCancelText("取消")
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setSubmitText("确认")
                .build();
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
    }
    private void initMonthContentButton(final View view){
        Button chooseYearButton = (Button)view.findViewById(R.id.hzs_history_content_as_month_year);
        Button chooseMonthButton = (Button)view.findViewById(R.id.hzs_history_content_as_month_month);
        final List<String> yearsItems = new ArrayList<>();
        final List<String> monthItems = new ArrayList<>();
        for (int i=2020;i>1969;i--){
            yearsItems.add(String.valueOf(i));
        }
        for (int i=1;i<13;i++){
            monthItems.add(String.valueOf(i));
        }
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenYear = yearsItems.get(options1);
                String chosenMonth = monthItems.get(options2);
                TextView yearTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_month_year);
                TextView monthTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_month_month);
                yearTextView.setText(chosenYear + "年");
                monthTextView.setText(chosenMonth+"月");
                year = Integer.parseInt(chosenYear);
                month = Integer.parseInt(chosenMonth);
                showDataAsMonth();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .build();
        pvOptions.setNPicker(yearsItems,monthItems,null);
        chooseYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
        chooseMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

    }
    private void initYearContentButton(final View contentView){
        final Button chooseYearButton = (Button)contentView.findViewById(R.id.hzs_history_content_as_year_year);
        final List<String> yearsItems = new ArrayList<>();
        for (int i=2020;i>1969;i--){
            yearsItems.add(String.valueOf(i));
        }
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenYear = yearsItems.get(options1);
                TextView yearTextView = (TextView)contentView.findViewById(R.id.hzs_history_content_as_year_year);
                yearTextView.setText(chosenYear + "年");
                year = Integer.parseInt(chosenYear);
                showDataAsYear();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择年份")
                .build();
        pvOptions.setPicker(yearsItems);
        chooseYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
    }
    private void initSeasonContentButton(final View view){
        Button chooseYearButton = (Button)view.findViewById(R.id.hzs_history_content_as_season_year);
        Button chooseSeasonButton = (Button)view.findViewById(R.id.hzs_history_content_as_season_season);
        final List<String> yearsItems = new ArrayList<>();
        final List<String> seasonItems = new ArrayList<>();
        for (int i=2020;i>1969;i--){
            yearsItems.add(String.valueOf(i));
        }
        seasonItems.add("春");
        seasonItems.add("夏");
        seasonItems.add("秋");
        seasonItems.add("冬");
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenYear = yearsItems.get(options1);
                String chosenSeason = seasonItems.get(options2);
                TextView yearTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_season_year);
                TextView seasonTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_season_season);
                yearTextView.setText(chosenYear + "年");
                seasonTextView.setText(chosenSeason);
                year = Integer.parseInt(chosenYear);
                season = chosenSeason;
                showDataAsSeason();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .build();
        pvOptions.setNPicker(yearsItems,seasonItems,null);
        chooseYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
        chooseSeasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

    }
    private void initBottomClassButton(){
        final Button bottomClassButton = (Button)findViewById(R.id.hzs_history_class_btn);
        final List<String> options1Items = new ArrayList<>();
        final List<List<String>> options2Items = new ArrayList<>();
        initClassOptionItems(options1Items,options2Items);
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                classification1 = options1Items.get(options1);
                classification2 = options2Items.get(options1).get(options2);
                bottomClassButton.setText(classification2);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择分类")
                .build();
        pvOptions.setPicker(options1Items, options2Items);
        bottomClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HistoryActivity.this,"Click Class Btn",Toast.LENGTH_SHORT).show();
                pvOptions.show();
            }
        });
    }
    private void initClassOptionItems(List<String> options1Items,List<List<String>> options2Items){
        options1Items.add("饮食");
        options1Items.add("娱乐");
        List<String> options1 = new ArrayList<>();
        options1.add("早餐");
        options1.add("午餐");
        options1.add("晚餐");
        options2Items.add(options1);
        List<String> options2 = new ArrayList<>();
        options2.add("唱歌");
        options2.add("游戏");
        options2.add("电影");
        options2Items.add(options2);
    }
    private void initBottomTimeframeButton(){
        final Button bottomTimeframeButton = (Button)findViewById(R.id.hzs_history_time_btn);
        //准备PopupWindow的布局View
        final View popupTimeframeView = LayoutInflater.from(this).inflate(R.layout.hzs_popup_timeframe, null);
        //初始化一个PopupWindow，width和height都是WRAP_CONTENT
        final PopupWindow popupTimeframeWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow的视图内容
        popupTimeframeWindow.setContentView(popupTimeframeView);
        //点击空白区域PopupWindow消失，这里必须先设置setBackgroundDrawable，否则点击无反应
        popupTimeframeWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupTimeframeWindow.setOutsideTouchable(true);
        //设置PopupWindow动画
        popupTimeframeWindow.setAnimationStyle(R.style.hzs_popup_vertical_anim_style);
        //设置是否允许PopupWindow的范围超过屏幕范围
        popupTimeframeWindow.setClippingEnabled(true);
        //设置PopupWindow消失监听
        popupTimeframeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        popupTimeframeView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        initPopupTimeframeView(popupTimeframeView,bottomTimeframeButton,popupTimeframeWindow);
        bottomTimeframeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupTimeframeWindow != null) {
                    popupTimeframeWindow.setFocusable(true);
                }
                if(!popupTimeframeWindow.isShowing()){
                    final int[] locationTimeframeButton = new int[2];
                    bottomTimeframeButton.getLocationOnScreen(locationTimeframeButton);

                    popupTimeframeWindow.showAtLocation(bottomTimeframeButton, Gravity.NO_GRAVITY,locationTimeframeButton[0],locationTimeframeButton[1]-popupTimeframeView.getMeasuredHeight());
                }
            }
        });
    }
    private void initPopupTimeframeView(View view, Button button, PopupWindow popupWindow){
        PopupTimeframeClickListener clickListener = new PopupTimeframeClickListener(button,popupWindow);
        Button btn0 = (Button)view.findViewById(R.id.hzs_history_timeframe_total_btn);
        btn0.setTag("总");
        Button btn1 = (Button)view.findViewById(R.id.hzs_history_timeframe_year_btn);
        btn1.setTag("年");
        Button btn2 = (Button)view.findViewById(R.id.hzs_history_timeframe_season_btn);
        btn2.setTag("季");
        Button btn3 = (Button)view.findViewById(R.id.hzs_history_timeframe_month_btn);
        btn3.setTag("月");
        Button btn4 = (Button)view.findViewById(R.id.hzs_history_timeframe_day_btn);
        btn4.setTag("日");
        btn0.setOnClickListener(clickListener);
        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);
        btn3.setOnClickListener(clickListener);
        btn4.setOnClickListener(clickListener);
    }
    /*
     * get开头的函数year必须减1900, show开头的函数则不用
     * */
    public List<HzsYearData> getTotalData(){
        DataBaseFilter filter = new DataBaseFilter(null,null,-1,null,null,null);
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        List<HzsYearData> yearData = new ArrayList<>();
        if(tallyData.size()>0){
            int lastYear = tallyData.get(0).getDate().getYear();
            int i = 0;
            while(i<tallyData.size()){
                List<Tally> singleYear = new ArrayList<>();
                while(i<tallyData.size() && tallyData.get(i).getDate().getYear() == lastYear){
                    singleYear.add(tallyData.get(i));
                    i += 1;
                }
                HzsYearData hzsYearData = new HzsYearData(singleYear);
                yearData.add(hzsYearData);
                if(i<tallyData.size()){
                    lastYear = tallyData.get(i).getDate().getYear();
                }
            }
        }
        return yearData;
    }

    public List<HzsMonthData> getYearData(int year){
        Date startDate = new Date(year,0,1);
        Date endDate = new Date(year,11,31);
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        List<HzsMonthData> monthData = new ArrayList<>();
        if(tallyData.size()>0){
            int i = 0;
            int lastMonth = tallyData.get(0).getDate().getMonth();
            while(i < tallyData.size()){
                List<Tally> singleMonth = new ArrayList<>();
                while(i<tallyData.size() && tallyData.get(i).getDate().getMonth() == lastMonth){
                    singleMonth.add(tallyData.get(i));
                    i += 1;
                }
                HzsMonthData hzsMonthData = new HzsMonthData(singleMonth, null);
                monthData.add(hzsMonthData);
                if(i<tallyData.size()){
                    lastMonth = tallyData.get(i).getDate().getMonth();
                }
            }
        }
        return monthData;
    }
    public List<HzsMonthData> getSeasonData(int year, String season){

        Date startDate;
        Date endDate;
        switch (season){
            case "春":
                startDate = new Date(year,1,1);
                endDate = new Date(year,3,31);
                break;
            case "夏":
                startDate = new Date(year,4,1);
                endDate = new Date(year,6,31);
                break;
            case "秋":
                startDate = new Date(year,7,1);
                endDate = new Date(year,9,30);
                break;
            default:
                startDate = new Date(year,10,1);
                endDate = new Date(year+1,0,31);
                break;
        }
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        List<HzsMonthData> monthData = new ArrayList<>();
        if(tallyData.size()>0){
            int i = 0;
            int lastMonth = tallyData.get(0).getDate().getMonth();
            while(i < tallyData.size()){
                List<Tally> singleMonth = new ArrayList<>();
                while(i<tallyData.size() && tallyData.get(i).getDate().getMonth() == lastMonth){
                    singleMonth.add(tallyData.get(i));
                    i += 1;
                }
                HzsMonthData hzsMonthData = new HzsMonthData(singleMonth, null);
                monthData.add(hzsMonthData);
                if(i<tallyData.size()){
                    lastMonth = tallyData.get(i).getDate().getMonth();
                }
            }
        }
        return monthData;
    }
    public List<HzsDayData> getMonthData(int year,int month){
        Date startDate = new Date(year,month,1);
        Date endDate = new Date(year,month,getDaysOfMonth(year,month));
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        List<HzsDayData> dayData = new ArrayList<>();
        if(tallyData.size()>0){
            int i = 0;
            int lastDay = tallyData.get(0).getDate().getDate();
            while(i < tallyData.size()){
                List<Tally> singleDay = new ArrayList<>();
                while(i<tallyData.size() && tallyData.get(i).getDate().getDate() == lastDay){
                    singleDay.add(tallyData.get(i));
                    i += 1;
                }
                HzsDayData hzsDayData = new HzsDayData(singleDay);
                dayData.add(hzsDayData);
                if(i<tallyData.size()){
                    lastDay = tallyData.get(i).getDate().getDate();
                }
            }
        }
        return dayData;
    }
    public List<Tally> getDayData(int year, int month,int day){
        Date startDate = new Date(year,month,day);
        Date endDate = new Date(year,month,day);
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        return tallyData;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hzs_history_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.hzs_history_menu_search:
                Toast.makeText(HistoryActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.hzs_history_menu_share:
                Toast.makeText(HistoryActivity.this,"分享",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void actionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.actionbar_history);  //绑定自定义的布局：actionbar_layout.xml

            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    public class PopupTimeframeClickListener implements View.OnClickListener{
        private Button button;
        private PopupWindow popupWindow;
        public PopupTimeframeClickListener(Button button, PopupWindow popupWindow){
            this.button = button;
            this.popupWindow = popupWindow;
        }
        @Override
        public void onClick(View view) {
            String tag = (String)view.getTag();
            switch (tag){
                case "总":
                    mode = TOTAL;
                    showDataAsTotal();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "年":
                    mode = YEAR;
                    showDataAsYear();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "季":
                    mode = SEASON;
                    showDataAsSeason();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "月":
                    mode = MONTH;
                    showDataAsMonth();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "日":
                    mode = DAY;
                    showDataAsDay();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;

            }
        }
    }

    public static int getDaysOfMonth(int year, int month) {
        java.util.Date date = new java.util.Date(year,month,1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}