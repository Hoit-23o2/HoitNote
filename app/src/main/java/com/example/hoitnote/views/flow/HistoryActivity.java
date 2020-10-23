package com.example.hoitnote.views.flow;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
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
import com.bigkoo.pickerview.listener.CustomListener;
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

    private static HistoryActivity instance;
    private int mode = 0;
    private Typeface tf;
    private OptionsPickerView pvBottomClassOptions;
    private OptionsPickerView pvBottomMemberOptions;
    private OptionsPickerView pvBottomVendorOptions;

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
        tf = ResourcesCompat.getFont(this, R.font.fontawesome5solid);
        instance = this;
        contentLayout = (LinearLayout)findViewById(R.id.hzs_history_content);
        actionBarInit();
        DataBaseFilter filter = new DataBaseFilter(null,null,-1,null,null,null);
        totalData = getTotalData(filter);
        showDataAsTotal();

        initBottomTimeframeButton();
        initBottomClassButton();
        initBottomAccountButton();
        initBottomProjectButton();
        initBottomMoreInfoButton();
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
        if(account == null){
            return null;
        }else{
            Account res = new Account();
            String[] strings = account.split("\\s+");
            if(strings.length > 1){
                res.setAccountName(strings[0]);
                res.setAccountCode(strings[1]);
            }else{
                res.setAccountName(strings[0]);
            }
            return res;
        }
    }
    public void refreshMainData(){
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        switch (mode){
            case TOTAL:
                DataBaseFilter filter1 = new DataBaseFilter(null,null,-1,getFilterClassifications(),getFilterAccount(),filterActionType);
                totalData = getTotalData(filter1);
                for(HzsYearData data:totalData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case YEAR:
                Date startDate2 = new Date(year,0,1);
                Date endDate2 = new Date(year,11,31);
                DataBaseFilter filter2 = new DataBaseFilter(startDate2,endDate2,-1,getFilterClassifications(),getFilterAccount(),filterActionType);
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
                        startDate3 = new Date(year,1,1);
                        endDate3 = new Date(year,3,31);
                        break;
                    case Constants.SeasonSummer:
                        startDate3 = new Date(year,4,1);
                        endDate3 = new Date(year,6,31);
                        break;
                    case Constants.SeasonAutumn:
                        startDate3 = new Date(year,7,1);
                        endDate3 = new Date(year,9,30);
                        break;
                    default:
                        startDate3 = new Date(year,10,1);
                        endDate3 = new Date(year+1,0,31);
                        break;
                }
                DataBaseFilter filter3 = new DataBaseFilter(startDate3,endDate3,-1,getFilterClassifications(),getFilterAccount(),filterActionType);
                seasonData = getSeasonData(filter3);
                for(HzsMonthData data:seasonData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case MONTH:
                Date startDate4 = new Date(year,month,1);
                Date endDate4 = new Date(year,month,getDaysOfMonth(year,month));
                DataBaseFilter filter4 = new DataBaseFilter(startDate4,endDate4,-1,getFilterClassifications(),getFilterAccount(),filterActionType);
                monthData = getMonthData(filter4);
                for(HzsDayData data:monthData){
                    bal += data.getBal();
                    in += data.getIn();
                    out += data.getOut();
                }
                break;
            case DAY:
                Date startDate5 = new Date(year,month,day);
                Date endDate5 = new Date(year,month,day);
                DataBaseFilter filter5 = new DataBaseFilter(startDate5,endDate5,-1,getFilterClassifications(),getFilterAccount(),filterActionType);
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
        View totalContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_total,contentLayout,false);
        HzsFirstExpandableListViewAdapter adapter = new HzsFirstExpandableListViewAdapter(totalData,this);
        ExpandableListView expandableListView = (ExpandableListView)totalContentView.findViewById(R.id.hzs_history_content_as_total_expandable_listview);
        expandableListView.setAdapter(adapter);
        contentLayout.addView(totalContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsYearData data:totalData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsYear(){
        contentLayout.removeAllViews();
        View yearContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_year,contentLayout,false);
        ((Button)yearContentView.findViewById(R.id.hzs_history_content_as_year_year)).setText(String.valueOf(year)+"年");
        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(yearData,this);
        ExpandableListView yearListView = (ExpandableListView)yearContentView.findViewById(R.id.hzs_history_content_as_year_expandable_listview);
        yearListView.setAdapter(adapter);
        initYearContentButton(yearContentView);
        contentLayout.addView(yearContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsMonthData data:yearData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsSeason(){
        contentLayout.removeAllViews();
        View seasonContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_season,contentLayout,false);
        ((Button)seasonContentView.findViewById(R.id.hzs_history_content_as_season_year)).setText(String.valueOf(year)+"年");
        ((FontAwesome)seasonContentView.findViewById(R.id.hzs_history_content_as_season_season)).setText(season);

        HzsSecondExpandableListViewAdapter adapter = new HzsSecondExpandableListViewAdapter(seasonData,this);
        ExpandableListView seasonListView = (ExpandableListView)seasonContentView.findViewById(R.id.hzs_history_content_as_season_expandable_listview);
        seasonListView.setAdapter(adapter);
        initSeasonContentButton(seasonContentView);
        contentLayout.addView(seasonContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsMonthData data:seasonData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsMonth(){
        contentLayout.removeAllViews();
        View monthContentView = LayoutInflater.from(this).inflate(R.layout.hzs_history_content_as_month,contentLayout,false);
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_year)).setText(String.valueOf(year)+"年");
        ((Button)monthContentView.findViewById(R.id.hzs_history_content_as_month_month)).setText(String.valueOf(month)+"月");
        HzsThirdExpandableListViewAdapter adapter = new HzsThirdExpandableListViewAdapter(monthData,this);
        ExpandableListView seasonListView = (ExpandableListView)monthContentView.findViewById(R.id.hzs_history_content_as_month_expandable_listview);
        seasonListView.setAdapter(adapter);
        initMonthContentButton(monthContentView);
        contentLayout.addView(monthContentView);
        Double bal = 0.0;
        Double in = 0.0;
        Double out = 0.0;
        for(HzsDayData data:monthData){
            bal += data.getBal();
            in += data.getIn();
            out += data.getOut();
        }
        showMainData(bal.toString(),out.toString(),in.toString());
    }
    private void showDataAsDay(){
        contentLayout.removeAllViews();
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
                year = Integer.parseInt(chosenYear);
                season = chosenSeason;
                showDataAsSeason();
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
            public void onClick(View view) {
                pvOptions.show();
            }
        });

    }
    private void initBottomClassButton(){
        final TextView bottomClassButton = findViewById(R.id.hzs_history_class_btn);
        pvBottomClassOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                classification1 = BookingDataHelper.getClassifications1(bookingType).get(options1);
                classification2 = BookingDataHelper.getClassifications2(bookingType).get(options1).get(options2);
                if(classification1 == Constants.HzsNullString){
                    classification1 = null;
                }
                if(classification2 == Constants.HzsNullString){
                    classification2 = null;
                }
                String classification2WithIcon = BookingDataHelper.getClassifications2WithIcons(bookingType).get(options1).get(options2);
                bottomClassButton.setText(classification2WithIcon);
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
        final TextView bottomAccountButton = findViewById(R.id.hzs_history_account_btn);
        final List<String> items = BookingDataHelper.getAccounts();
        final OptionsPickerView pvBottomAccountOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                account = items.get(options1);
                if(account == Constants.HzsNullString){
                    account = null;
                }
                bottomAccountButton.setText(account);
                showDataAsMode();
            }
        }).setCancelText("取消")
                .setSubmitText("完成")
                .setTitleText("选择账户")
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
        final TextView bottomProjectButton = findViewById(R.id.hzs_history_project_btn);
        final List<String> items = BookingDataHelper.getProjectsWithIcons();
        final OptionsPickerView pvBottomProjectOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                project = BookingDataHelper.getProjects().get(options1);
                if(project == Constants.HzsNullString){
                    project = null;
                }
                String projectWithIcon = items.get(options1);
                bottomProjectButton.setText(projectWithIcon);
                showDataAsMode();
            }
        }).setCancelText("取消")
                .setSubmitText("完成")
                .setTitleText("选择项目")
                .setTypeface(tf).build();
        pvBottomProjectOptions.setPicker(items);
        bottomProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvBottomProjectOptions.show();
            }
        });
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
    private void initBottomMemberButton(){
        final List<String> items = BookingDataHelper.getPersonsWithIcons();
        pvBottomMemberOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                member = BookingDataHelper.getPersons().get(options1);
                if(member == Constants.HzsNullString){
                    member = null;
                }
                showDataAsMode();
            }
        }).setCancelText("取消")
                .setSubmitText("完成")
                .setTitleText("选择成员")
                .setTypeface(tf).build();
        pvBottomMemberOptions.setPicker(items);
    }
    private void initBottomVendorButton(){
        final List<String> items = BookingDataHelper.getStoresWithIcons();
        pvBottomVendorOptions = new OptionsPickerBuilder(HistoryActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                vendor = BookingDataHelper.getStores().get(options1);
                if(vendor == Constants.HzsNullString){
                    vendor = null;
                }
                showDataAsMode();
            }
        }).setCancelText("取消")
                .setSubmitText("完成")
                .setTitleText("选择商家")
                .setTypeface(tf).build();
        pvBottomVendorOptions.setPicker(items);
    }
    private void initBottomMoreInfoButton(){
        final TextView bottomMoreInfoButton = findViewById(R.id.hzs_history_moreinfo_btn);
        //准备PopupWindow的布局View
        final View popupMoreInfoView = LayoutInflater.from(this).inflate(R.layout.hzs_popup_moreinfo, null);
        //初始化一个PopupWindow，width和height都是WRAP_CONTENT
        final PopupWindow popupMoreInfoWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow的视图内容
        popupMoreInfoWindow.setContentView(popupMoreInfoView);
        //点击空白区域PopupWindow消失，这里必须先设置setBackgroundDrawable，否则点击无反应
        popupMoreInfoWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupMoreInfoWindow.setOutsideTouchable(true);
        //设置PopupWindow动画
        popupMoreInfoWindow.setAnimationStyle(R.style.hzs_popup_vertical_anim_style);
        //设置是否允许PopupWindow的范围超过屏幕范围
        popupMoreInfoWindow.setClippingEnabled(true);
        //设置PopupWindow消失监听
        popupMoreInfoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        popupMoreInfoView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        initPopupMoreInfoView(popupMoreInfoView,bottomMoreInfoButton,popupMoreInfoWindow);
        bottomMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupMoreInfoWindow != null) {
                    popupMoreInfoWindow.setFocusable(true);
                }
                if(!popupMoreInfoWindow.isShowing()){
                    final int[] locationTimeframeButton = new int[2];
                    bottomMoreInfoButton.getLocationOnScreen(locationTimeframeButton);

                    popupMoreInfoWindow.showAtLocation(bottomMoreInfoButton, Gravity.NO_GRAVITY,locationTimeframeButton[0],locationTimeframeButton[1]-popupMoreInfoView.getMeasuredHeight());
                }
            }
        });
    }

    private void initPopupMoreInfoView(View view, TextView button, PopupWindow popupWindow) {
        PopupMoreInfoClickListener clickListener = new PopupMoreInfoClickListener(button,popupWindow);
        Button btn0 = (Button)view.findViewById(R.id.member_btn);
        btn0.setTag("成员");
        Button btn1 = (Button)view.findViewById(R.id.vendor_btn);
        btn1.setTag("商家");
        btn0.setOnClickListener(clickListener);
        btn1.setOnClickListener(clickListener);
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
                    refreshMainData();
                    showDataAsTotal();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "年":
                    mode = YEAR;
                    refreshMainData();
                    showDataAsYear();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "季":
                    mode = SEASON;
                    refreshMainData();
                    showDataAsSeason();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "月":
                    mode = MONTH;
                    refreshMainData();
                    showDataAsMonth();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;
                case "日":
                    mode = DAY;
                    refreshMainData();
                    showDataAsDay();
                    button.setText(tag);
                    popupWindow.dismiss();
                    break;

            }
        }
    }

    public class PopupMoreInfoClickListener implements View.OnClickListener{
        private TextView button;
        private PopupWindow popupWindow;
        public PopupMoreInfoClickListener(TextView button, PopupWindow popupWindow){
            this.button = button;
            this.popupWindow = popupWindow;
        }
        @Override
        public void onClick(View view) {
            String tag = (String)view.getTag();
            switch (tag){
                case "成员":
                    pvBottomMemberOptions.show();
                    popupWindow.dismiss();
                    break;
                case "商家":
                    pvBottomVendorOptions.show();
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