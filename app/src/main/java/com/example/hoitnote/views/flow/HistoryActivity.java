package com.example.hoitnote.views.flow;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnDismissListener;
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
import com.example.hoitnote.customviews.FontAwesome;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.flow.HzsDayData;
import com.example.hoitnote.models.flow.HzsMonthData;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.models.flow.HzsYearData;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;

import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;


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
    private String season = Constants.SeasonAutumn;
    private String classification1 = null;
    private String classification2 = null;
    private BookingType bookingType = BookingType.OUTCOME;
    private ActionType filterActionType = null;
    private String account = null;
    private String project = null;
    private String vendor = null;
    private String member = null;
    private Account realAccount = null;
    private static HistoryActivity instance;
    private int mode = 0;
    private Typeface tf;
    private OptionsPickerView pvBottomClassOptions;
    private OptionsPickerView pvBottomMemberOptions;
    private OptionsPickerView pvBottomVendorOptions;
    private OptionsPickerView pvBottomTimeFrameOptions;
    private OptionsPickerView pvBottomProjectOptions;
    private OptionsPickerView pvBottomAccountOptions;
    private View totalContentView;
    private View yearContentView;
    private View seasonContentView;
    private View monthContentView;
    private View dayContentView;

    private List<HzsYearData> totalData;
    private List<HzsMonthData> yearData;
    private List<HzsMonthData> seasonData;
    private List<HzsDayData> monthData;
    private List<Tally> dayData;
    public static final int TOTAL = 0;
    public static final int YEAR = 1;
    public static final int SEASON = 2;
    public static final int MONTH = 3;
    public static final int DAY = 4;

    private Double bal = 0.0;
    private Double in = 0.0;
    private Double out = 0.0;
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tf = ResourcesCompat.getFont(this, R.font.fontawesome5solid);
        instance = this;
        contentLayout = findViewById(R.id.hzs_history_content);
        actionBarInit();
        DataBaseFilter filter = new DataBaseFilter(null,null,-1,null,null,null);
        totalData = getTotalData(filter);
        showDataAsTotal();
        /*可拖拽*/
        findViewById(R.id.floatingButton).setOnTouchListener(
                new BaseActionButtonDraggableLisener()
        );

        initBottomTimeFrameButton();
        initBottomClassButton();
        initBottomAccountButton();
        initBottomProjectButton();
        //initBottomMoreInfoButton();
        initBottomMemberButton();
        initBottomVendorButton();

    }

    public static HistoryActivity getInstance() {
        return instance;
    }

    private ArrayList<String> getFilterClassifications(){
        if(classification1 == null){
            return null;
        }else if(classification2 == null){
            ArrayList<String> res = new ArrayList<>();
            res.add(classification1);
            return res;
        }else{
            ArrayList<String> res = new ArrayList<>();
            res.add(classification1 + Constants.tallyTableC1C2Spliter + classification2);
            return res;
        }
    }

    private Account getFilterAccount(){
        return realAccount;
    }
    public void refreshMainData(){
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        switch (mode){
            case TOTAL:
                DataBaseFilter filter1 = new DataBaseFilter(null,null,-1,getFilterClassifications(),getFilterAccount(),filterActionType,project,member,vendor);
                totalData = getTotalData(filter1);
                for(HzsYearData data:totalData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case YEAR:
                Date startDate2 = new Date(year-1900,0,1);
                Date endDate2 = new Date(year-1900,11,31);
                DataBaseFilter filter2 = new DataBaseFilter(startDate2,endDate2,-1,getFilterClassifications(),getFilterAccount(),filterActionType,project,member,vendor);
                yearData = getYearData(filter2);
                for(HzsMonthData data:yearData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case SEASON:
                Date startDate3;
                Date endDate3;
                switch (season){
                    case Constants.SeasonSpring:
                        startDate3 = new Date(year-1900,1,1);
                        endDate3 = new Date(year-1900,3,31);
                        break;
                    case Constants.SeasonSummer:
                        startDate3 = new Date(year-1900,4,1);
                        endDate3 = new Date(year-1900,6,31);
                        break;
                    case Constants.SeasonAutumn:
                        startDate3 = new Date(year-1900,7,1);
                        endDate3 = new Date(year-1900,9,30);
                        break;
                    default:
                        startDate3 = new Date(year-1900,10,1);
                        endDate3 = new Date(year+1-1900,0,31);
                        break;
                }
                DataBaseFilter filter3 = new DataBaseFilter(startDate3,endDate3,-1,getFilterClassifications(),getFilterAccount(),filterActionType,project,member,vendor);
                seasonData = getSeasonData(filter3);
                for(HzsMonthData data:seasonData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case MONTH:
                Date startDate4 = new Date(year-1900,month-1,1);
                Date endDate4 = new Date(year-1900,month-1,getDaysOfMonth(year,month));
                DataBaseFilter filter4 = new DataBaseFilter(startDate4,endDate4,-1,getFilterClassifications(),getFilterAccount(),filterActionType,project,member,vendor);
                monthData = getMonthData(filter4);
                for(HzsDayData data:monthData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case DAY:
                Date startDate5 = new Date(year-1900,month-1,day);
                Date endDate5 = new Date(year-1900,month-1,day);
                DataBaseFilter filter5 = new DataBaseFilter(startDate5,endDate5,-1,getFilterClassifications(),getFilterAccount(),filterActionType,project,member,vendor);
                dayData = getDayData(filter5);
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
    }
    public void showMainData(){
        String balance = bal.toString();
        String income = in.toString();
        String outcome = out.toString();
        TextView balanceTextView = findViewById(R.id.hzs_history_balance);
        TextView outcomeTextView = findViewById(R.id.hzs_history_outcome);
        TextView incomeTextView = findViewById(R.id.hzs_history_income);
        switch (mode){
            case TOTAL:
                break;
            case YEAR:
                ((TextView)yearContentView.findViewById(R.id.hzs_history_content_as_year_balance)).setText(balance);
                break;
            case SEASON:
                ((TextView)seasonContentView.findViewById(R.id.hzs_history_content_as_season_balance)).setText(balance);
                break;
            case MONTH:
                ((TextView)monthContentView.findViewById(R.id.hzs_history_content_as_month_balance)).setText(balance);
                break;
            case DAY:
                ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_balance)).setText(balance);
                break;
        }
        balanceTextView.setText(balance);
        outcomeTextView.setText(outcome);
        incomeTextView.setText(income);
    }
    private void showDataAsMode(){
        refreshMainData();
        switch (mode){
            case TOTAL:
                showDataAsTotal();
                break;
            case YEAR:
                showDataAsYear();
                break;
            case SEASON:
                showDataAsSeason();
                break;
            case MONTH:
                showDataAsMonth();
                break;
            case DAY:
                showDataAsDay();
                break;
        }
    }
    private void showDataAsTotal(){
        contentLayout.removeAllViews();
        totalContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_total,contentLayout,false);
        HzsFirstExpandableListViewAdapter adapter = new HzsFirstExpandableListViewAdapter(totalData,this);
        ExpandableListView expandableListView = (ExpandableListView)totalContentView.findViewById(R.id.hzs_history_content_as_total_expandable_listview);
        expandableListView.setAdapter(adapter);
        contentLayout.addView(totalContentView);
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        for(HzsYearData data:totalData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData();
    }
    private void showDataAsYear(){
        contentLayout.removeAllViews();
        yearContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_year,contentLayout,false);
        ((Button)yearContentView.findViewById(R.id.hzs_history_content_as_year_year)).setText(String.valueOf(year)+"年");
        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(yearData,this);
        ExpandableListView yearListView = (ExpandableListView)yearContentView.findViewById(R.id.hzs_history_content_as_year_expandable_listview);
        yearListView.setAdapter(adapter);
        initYearContentButton(yearContentView);
        contentLayout.addView(yearContentView);
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        for(HzsMonthData data:yearData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData();

    }
    private void showDataAsSeason(){
        contentLayout.removeAllViews();
        seasonContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_season,contentLayout,false);
        ((Button)seasonContentView.findViewById(R.id.hzs_history_content_as_season_year)).setText(String.valueOf(year)+"年");
        ((FontAwesome)seasonContentView.findViewById(R.id.hzs_history_content_as_season_season)).setText(season);

        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(seasonData,this);
        ExpandableListView seasonListView = (ExpandableListView)seasonContentView.findViewById(R.id.hzs_history_content_as_season_expandable_listview);
        seasonListView.setAdapter(adapter);
        initSeasonContentButton(seasonContentView);
        contentLayout.addView(seasonContentView);
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        for(HzsMonthData data:seasonData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData();
    }
    private void showDataAsMonth(){
        contentLayout.removeAllViews();
        monthContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_month,contentLayout,false);
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_year)).setText(String.valueOf(year)+"年");
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_month)).setText(String.valueOf(month)+"月");
        HzsThirdExpandableListViewAdapter adapter = new HzsThirdExpandableListViewAdapter(monthData,this);
        ExpandableListView seasonListView = (ExpandableListView)monthContentView.findViewById(R.id.hzs_history_content_as_month_expandable_listview);
        seasonListView.setAdapter(adapter);
        initMonthContentButton(monthContentView);
        contentLayout.addView(monthContentView);
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        for(HzsDayData data:monthData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData();
    }
    private void showDataAsDay(){
        contentLayout.removeAllViews();
        dayContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_day,contentLayout,false);
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_year)).setText(String.valueOf(year)+"年");
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_month)).setText(String.valueOf(month)+"日");
        ((TextView)dayContentView.findViewById(R.id.hzs_history_content_as_day_day)).setText(String.valueOf(day)+"日");
        HzsContentDayRecyclerViewAdapter adapter = new HzsContentDayRecyclerViewAdapter(dayData, HzsTally.TIME,this);
        RecyclerView recyclerView = (RecyclerView)dayContentView.findViewById(R.id.hzs_history_content_as_day_recyclerview);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        initDayContentButton(dayContentView);
        contentLayout.addView(dayContentView);
        bal = 0.0;
        in = 0.0;
        out = 0.0;
        for(Tally data:dayData){
            if(data.getActionType() == ActionType.OUTCOME){
                bal -= data.getMoney();
                out += data.getMoney();
            }else{
                bal += data.getMoney();
                in += data.getMoney();
            }
        }
        showMainData();
    }
    private void initDayContentButton(final View view){
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
                mode = DAY;
                year = date.getYear()+1900;
                month = date.getMonth()+1;
                day = date.getDate();
                showDataAsMode();
            }
        }).setCancelText("取消")
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setSubmitText("确认")
                .build();
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                pvTime.show();
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconUpArrow);
            }
        });
        pvTime.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconDownArrow);
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
                mode = MONTH;
                year = Integer.parseInt(chosenYear);
                month = Integer.parseInt(chosenMonth);
                showDataAsMode();
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
            public void onClick(View view1) {
                pvOptions.show();
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconUpArrow);
            }
        });
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconDownArrow);
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
                mode = YEAR;
                year = Integer.parseInt(chosenYear);
                showDataAsMode();
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
                ((TextView)contentView.findViewById(R.id.expandableArrow)).setText(Constants.IconUpArrow);
            }
        });
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                ((TextView)contentView.findViewById(R.id.expandableArrow)).setText(Constants.IconDownArrow);
            }
        });
    }
    private void initSeasonContentButton(final View view){
        Button chooseYearButton = (Button)view.findViewById(R.id.hzs_history_content_as_season_year);
        FontAwesome chooseSeasonButton = view.findViewById(R.id.hzs_history_content_as_season_season);
        final List<String> yearsItems = new ArrayList<>();
        final List<String> seasonItems = new ArrayList<>();
        for (int i=2020;i>1969;i--){
            yearsItems.add(String.valueOf(i));
        }
        seasonItems.add(Constants.SeasonSpring);
        seasonItems.add(Constants.SeasonSummer);
        seasonItems.add(Constants.SeasonAutumn);
        seasonItems.add(Constants.SeasonWinter);
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenYear = yearsItems.get(options1);
                String chosenSeason = seasonItems.get(options2);
                TextView yearTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_season_year);
                TextView seasonTextView = (TextView)view.findViewById(R.id.hzs_history_content_as_season_season);
                yearTextView.setText(chosenYear + "年");
                seasonTextView.setText(chosenSeason);
                mode = SEASON;
                year = Integer.parseInt(chosenYear);
                season = chosenSeason;
                showDataAsMode();
            }
        }).setSubmitText("确定")//确定按钮文字
                .setTypeface(tf)
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
            public void onClick(View view1) {
                pvOptions.show();
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconUpArrow);
            }
        });
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                ((TextView)view.findViewById(R.id.expandableArrow)).setText(Constants.IconDownArrow);
            }
        });

    }
    private void initBottomClassButton(){
        final View bottomClassButton = findViewById(R.id.hzs_history_class_btn);
        pvBottomClassOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                classification1 = BookingDataHelper.getClassifications1(bookingType).get(options1);
                classification2 = BookingDataHelper.getClassifications2(bookingType).get(options1).get(options2);
                if(classification1.equals(Constants.HzsNullString)){
                    classification1 = null;
                }
                if(classification2.equals(Constants.HzsNullString)){
                    classification2 = null;
                }
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_history_class_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);
                final TextView tvOutcome = v.findViewById(R.id.outcome_button);
                final TextView tvIncome = v.findViewById(R.id.income_button);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    pvBottomClassOptions.returnData();
                    pvBottomClassOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    pvBottomClassOptions.dismiss();
                    }
                });

                tvOutcome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    bookingType = BookingType.OUTCOME;
                    filterActionType = ActionType.OUTCOME;
                    pvBottomClassOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                            BookingDataHelper.getClassifications2WithIcons(bookingType));
                    }
                });
                tvIncome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    bookingType = BookingType.INCOME;
                    filterActionType = ActionType.INCOME;
                    pvBottomClassOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                            BookingDataHelper.getClassifications2WithIcons(bookingType));
                    }
                });

            }
        })
                .setTypeface(tf).build();
        pvBottomClassOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                BookingDataHelper.getClassifications2WithIcons(bookingType));
        bottomClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomClassOptions.show();
            }
        });
    }
    private void initBottomAccountButton(){
        final View bottomAccountButton = findViewById(R.id.hzs_history_account_btn);
        final List<String> items = BookingDataHelper.getAccountsForShow();
        pvBottomAccountOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                account = items.get(options1);
                if(account.equals(Constants.HzsNullString)){
                    account = null;
                }
                realAccount = App.dataBaseHelper.getAccounts().get(options1);
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_time_frame_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomAccountOptions.returnData();
                        pvBottomAccountOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomAccountOptions.dismiss();
                    }
                });
            }
        })
                .setTypeface(tf).build();
        pvBottomAccountOptions.setPicker(items);
        bottomAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomAccountOptions.show();
            }
        });
    }
    private void initBottomProjectButton(){
        final View bottomProjectButton = findViewById(R.id.hzs_history_project_btn);
        final List<String> items = BookingDataHelper.getProjectsWithIcons();
        pvBottomProjectOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                project = BookingDataHelper.getProjects().get(options1);
                if(project.equals(Constants.HzsNullString)){
                    project = null;
                }
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_time_frame_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomProjectOptions.returnData();
                        pvBottomProjectOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomProjectOptions.dismiss();
                    }
                });
            }
        })
                .setTypeface(tf).build();
        pvBottomProjectOptions.setPicker(items);
        bottomProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomProjectOptions.show();
            }
        });
    }
    private void initBottomTimeFrameButton() {
        final View bottomTimeFrameButton = findViewById(R.id.hzs_history_time_btn);
        final List<String> items = new ArrayList<>();
        items.add("总");
        items.add("年");
        items.add("季");
        items.add("月");
        items.add("日");
        pvBottomTimeFrameOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mode = options1;
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_time_frame_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomTimeFrameOptions.returnData();
                        pvBottomTimeFrameOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomTimeFrameOptions.dismiss();
                    }
                });
            }
        })
                .setTypeface(tf).build();
        pvBottomTimeFrameOptions.setPicker(items);
        bottomTimeFrameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomTimeFrameOptions.show();
            }
        });
    }

    private void initBottomMemberButton(){
        final View bottomMemberButton = findViewById(R.id.hzs_history_person_btn);
        final List<String> items = BookingDataHelper.getPersonsWithIcons();
        pvBottomMemberOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                member = BookingDataHelper.getPersons().get(options1);
                if(member.equals(Constants.HzsNullString)){
                    member = null;
                }
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_time_frame_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomMemberOptions.returnData();
                        pvBottomMemberOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomMemberOptions.dismiss();
                    }
                });
            }
        })
                .setTypeface(tf).build();
        pvBottomMemberOptions.setPicker(items);
        bottomMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomMemberOptions.show();
            }
        });
    }
    private void initBottomVendorButton(){
        final View bottomVendorButton = findViewById(R.id.hzs_history_store_btn);
        final List<String> items = BookingDataHelper.getStoresWithIcons();
        pvBottomVendorOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                vendor = BookingDataHelper.getStores().get(options1);
                if(vendor.equals(Constants.HzsNullString)){
                    vendor = null;
                }
                showDataAsMode();
            }
        }).setLayoutRes(R.layout.hzs_time_frame_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.finish_button);
                final TextView tvCancel = v.findViewById(R.id.cancel_button);

                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomVendorOptions.returnData();
                        pvBottomVendorOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvBottomVendorOptions.dismiss();
                    }
                });
            }
        })
                .setTypeface(tf).build();
        pvBottomVendorOptions.setPicker(items);
        bottomVendorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomVendorOptions.show();
            }
        });
    }

    /*
     * get开头的函数year必须减1900, show开头的函数则不用
     * */
    public List<HzsYearData> getTotalData(DataBaseFilter filter){
        //DataBaseFilter filter = new DataBaseFilter(null,null,-1,null,null,null);
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

    public List<HzsMonthData> getYearData(DataBaseFilter filter){
       /* Date startDate = new Date(year,0,1);
        Date endDate = new Date(year,11,31);
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);*/
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
    public List<HzsMonthData> getSeasonData(DataBaseFilter filter){

        /*Date startDate;
        Date endDate;
        switch (season){
            case Constants.SeasonSpring:
                startDate = new Date(year,1,1);
                endDate = new Date(year,3,31);
                break;
            case Constants.SeasonSummer:
                startDate = new Date(year,4,1);
                endDate = new Date(year,6,31);
                break;
            case Constants.SeasonAutumn:
                startDate = new Date(year,7,1);
                endDate = new Date(year,9,30);
                break;
            default:
                startDate = new Date(year,10,1);
                endDate = new Date(year+1,0,31);
                break;
        }
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);*/
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
    public List<HzsDayData> getMonthData(DataBaseFilter filter){
        /*Date startDate = new Date(year,month,1);
        Date endDate = new Date(year,month,getDaysOfMonth(year,month));
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);*/
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
    public List<Tally> getDayData(DataBaseFilter filter){
        /*Date startDate = new Date(year,month,day);
        Date endDate = new Date(year,month,day);
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,-1,null,null,null);*/
        List<Tally> tallyData = App.dataBaseHelper.getTallies(filter);
        return tallyData;
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hzs_history_menu, menu);
        return true;
    }*/
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
            actionBar.setTitle("钱包");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static int getDaysOfMonth(int year, int month) {
        java.util.Date date = new java.util.Date(year,month,1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}