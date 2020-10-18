package com.example.hoitnote.utils.managers;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.hoitnote.adapters.analysis.charts.CAMPCListAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMTdClListAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMSgClListAdapter;
import com.example.hoitnote.customviews.charts.HoitNoteClView;
import com.example.hoitnote.customviews.charts.HoitNotePCView;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.charts.TallyAnalysisCl;
import com.example.hoitnote.models.charts.TallyAnalysisClTimeDivision;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChartAnalysisManager {
    private static Random random = new Random(System.currentTimeMillis());

    private Context context;

    //扇形统计图相关
    private HoitNotePCView hoitNotePCView;
    private CAMPCListAdapter CAMPCListAdapter;

    private ListView myListViewPc;
    private TextView myTextViewPc;
    private ContentValues showNameContentValues;

    //折线统计图相关
    static Calendar calendar = Calendar.getInstance();
    static Calendar calendar2 = Calendar.getInstance();

    private HoitNoteClView hoitNoteCLView;
    private CAMTdClListAdapter CAMTdClListAdapter;
    private CAMSgClListAdapter CAMSgClListAdapter;
    private ListView myListViewClTimeDivision;
    private ListView myListViewClSignName;
    private ArrayList<String> timeDivisionList;


    public ChartAnalysisManager(Context context) {
        //初始化Pc
        hoitNotePCView = null;
        myListViewPc = null;
        this.context = context;
        //初始化Cl
        hoitNoteCLView = null;
        timeDivisionList = new ArrayList<>();
        timeDivisionList.add("年");
        timeDivisionList.add("月");
        timeDivisionList.add("周");
        timeDivisionList.add("日");
    }

    //扇形统计图相关
    public void setHoitNotePCView(HoitNotePCView hoitNotePCView) {
        this.hoitNotePCView = hoitNotePCView;
        this.hoitNotePCView.setManager(this);
    }

    public void setListViewPC(ListView myListView) {
        this.myListViewPc = myListView;
        this.CAMPCListAdapter = new CAMPCListAdapter(context);
        this.myListViewPc.setAdapter(this.CAMPCListAdapter);
        this.CAMPCListAdapter.setManager(this);
    }

    public void setTextViewPc(TextView textView){
        this.myTextViewPc = textView;
        this.showNameContentValues = new ContentValues();
        showNameContentValues.put(Constants.tallyTableColumn_c1,"一级分类");
        showNameContentValues.put(Constants.tallyTableColumn_c2,"二级分类");
        showNameContentValues.put(Constants.tallyTableColumn_account,"账户");
        showNameContentValues.put(Constants.tallyTableColumn_member,"成员");
        showNameContentValues.put(Constants.tallyTableColumn_project,"项目");
        showNameContentValues.put(Constants.tallyTableColumn_vendor,"商家");
        showNameContentValues.put("总计","总计");
    }

    public void setTallyAnalysisPCListPC(ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList){
        //设置要绘制的数据array
        //设置扇形统计图数据array逻辑
        if(hoitNotePCView != null){
            hoitNotePCView.endAnimation();
            hoitNotePCView.setTallyAnalysisPCArrayList(tallyAnalysisPCArrayList);
        }
        if(myListViewPc != null){
            CAMPCListAdapter.setList(tallyAnalysisPCArrayList);
        }
        if(myTextViewPc != null){
            myTextViewPc.setText(showNameContentValues.getAsString("总计"));
        }
    }

    public void notifyEnterTallyAnalysisPC(TallyAnalysisPC tallyAnalysisPC, String key){
        //通知要深入
        //扇形统计图深入Screen逻辑
        if(hoitNotePCView != null){
            if(!hoitNotePCView.ifAnimation){
                hoitNotePCView.enterTallyAnalysisPC(tallyAnalysisPC);
                if(myListViewPc != null) {
                    CAMPCListAdapter.enterTallyAnalysisPC(tallyAnalysisPC);
                }
                if(myTextViewPc != null){
                    myTextViewPc.setText(showNameContentValues.getAsString(key));
                }
            }
        }else {
            if(myListViewPc != null){
                CAMPCListAdapter.enterTallyAnalysisPC(tallyAnalysisPC);
            }
            if(myTextViewPc != null){
                myTextViewPc.setText(showNameContentValues.getAsString(key));
            }
        }
    }



    public void notifyGoBackPc(String key){
        //通知要回退
        //扇形统计图深入Screen逻辑
        if(hoitNotePCView != null){
            if(!hoitNotePCView.ifAnimation){
                hoitNotePCView.goBack();
                if(myListViewPc != null) {
                    CAMPCListAdapter.goBack();
                }
                if(myTextViewPc != null){
                    myTextViewPc.setText(showNameContentValues.getAsString(key));
                }
            }
        }else {
            if(myListViewPc != null){
                CAMPCListAdapter.goBack();
            }
            if(myTextViewPc != null){
                myTextViewPc.setText(showNameContentValues.getAsString(key));
            }
        }
    }

    public void setBackgroundColorPc(int color){
        if(hoitNotePCView != null){
            hoitNotePCView.setSelfBackgroundColor(color);
        }
    }

    public void setCuttingLineColorPc(int color){
        if(hoitNotePCView != null){
            hoitNotePCView.setSelfCuttingLineColor(color);
        }
    }

    public void actImageViewPC(){
        if(hoitNotePCView != null){
            hoitNotePCView.actSelf();
        }
    }

    //折线统计图相关
    public void setHoitNoteCLView(HoitNoteClView hoitNoteCLView){
        this.hoitNoteCLView = hoitNoteCLView;
        this.hoitNoteCLView.setManager(this);
    }

    public void setListViewCl(ListView listViewTD, ListView listViewSN){
        this.myListViewClTimeDivision= listViewTD;
        if(listViewTD != null){
            this.CAMTdClListAdapter = new CAMTdClListAdapter(context);
            this.myListViewClTimeDivision.setAdapter(this.CAMTdClListAdapter);
            this.CAMTdClListAdapter.setManager(this);
        }
        this.myListViewClSignName = listViewSN;
        if(listViewSN != null){
            this.CAMSgClListAdapter = new CAMSgClListAdapter(context);
            this.myListViewClSignName.setAdapter(this.CAMSgClListAdapter);
            this.CAMSgClListAdapter.setManager(this);
        }
    }

    public void setTallyAnalysisClListCl(ArrayList<TallyAnalysisCl> tallyAnalysisClListCl){
        if(hoitNoteCLView != null){
            hoitNoteCLView.endAnimation();
            hoitNoteCLView.setTallyAnalysisClArrayList(tallyAnalysisClListCl);
        }
        if(myListViewClTimeDivision != null){
            CAMTdClListAdapter.setList(timeDivisionList);
        }
        if(myListViewClSignName != null){
            CAMSgClListAdapter.setList(tallyAnalysisClListCl);
        }
    }

    public void notifyTimeDivision(String newTimeDivision){
        if(hoitNoteCLView != null){
            if(!hoitNoteCLView.ifAnimation){
                hoitNoteCLView.changeTimeDivision(newTimeDivision);
                if(myListViewClTimeDivision != null){
                    CAMTdClListAdapter.changeTimeDivision(newTimeDivision);
                }
            }
        }else{
            if(myListViewClTimeDivision != null){
                CAMTdClListAdapter.changeTimeDivision(newTimeDivision);
            }
        }
    }

    public void notifyMainSignChange(TallyAnalysisCl tallyAnalysisCl){
        if(hoitNoteCLView != null){
            if(!hoitNoteCLView.ifAnimation){
                hoitNoteCLView.changeMainSign(tallyAnalysisCl);
                CAMSgClListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setBackgroundColorCl(int color){
        if(hoitNoteCLView != null){
            hoitNoteCLView.setSelfBackgroundColor(color);
        }
    }

    public void setInsideBackgroundColorCl(int color){
        if(hoitNoteCLView != null){
            hoitNoteCLView.setSelfInsideBackgroundColor(color);
        }
    }

    public void setLineColorCl(int color){
        if(hoitNoteCLView != null){
            hoitNoteCLView.setSelfLineColor(color);
        }
    }

    public void setTextColorCl(int color){
        if(hoitNoteCLView != null){
            hoitNoteCLView.setSelfTextColor(color);
        }
    }

    public void actImageViewCl(){
        if(hoitNoteCLView != null){
            hoitNoteCLView.actSelf();
        }
    }


    //处理数据函数
    //公用处理
    public static ArrayList<ContentValues> getContentValuesTallies(ArrayList<Tally> tallyArrayList){
        if (tallyArrayList == null || tallyArrayList.size() == 0) {
            return null;
        } else {
            //获取键值对型Tally List
            ArrayList<ContentValues> contentValuesTallies = new ArrayList<>();
            int lenTallies, i;
            lenTallies = tallyArrayList.size();
            for (i = 0; i < lenTallies; i++) {
                Tally tally = tallyArrayList.get(i);
                ContentValues contentValues = new ContentValues();
                //添加信息
                //添加id
                contentValues.put(Constants.tallyTableColumn_id,tally.getId());
                //添加money
                contentValues.put(Constants.tallyTableColumn_money,tally.getMoney());
                //添加date 添加为String
                contentValues.put(Constants.tallyTableColumn_date,tally.getDate().toString());
                //添加time 添加为String
                contentValues.put(Constants.tallyTableColumn_time,tally.getTime().toString());
                //添加remark
                contentValues.put(Constants.tallyTableColumn_remark,tally.getRemark());
                //添加account ACN\nACC
                Account account = tally.getAccount();
                contentValues.put(Constants.tallyTableColumn_account,account.getAccountName() + "\n" + account.getAccountCode());
                //添加actionType
                contentValues.put(Constants.tallyTableColumn_actionType,tally.getActionType().toString());
                //添加c1
                contentValues.put(Constants.tallyTableColumn_c1,tally.getClassification1());
                //添加c2  "c1-c2"
                contentValues.put(Constants.tallyTableColumn_c2,tally.getClassification1() + "-" + tally.getClassification2());
                //添加member
                contentValues.put(Constants.tallyTableColumn_member,tally.getMember());
                //添加project
                contentValues.put(Constants.tallyTableColumn_project,tally.getProject());
                //添加vendor
                contentValues.put(Constants.tallyTableColumn_vendor,tally.getVendor());

                contentValuesTallies.add(contentValues);
            }
            return contentValuesTallies;
        }
    }

    private static ArrayList<Integer> createColor(int colorNum, int luminanceP){
        double everyRGBdAngle = 255 / 60.0D;
        int red = 0,green = 0,blue = 0;
        int angle = random.nextInt() % 360;
        while(angle < 0){
            angle += 360;
        }
        int i;
        int everyAngle = 360 / colorNum;
        ArrayList<Integer> colorList = new ArrayList<>();
        for(i = 0;i < colorNum;i++){
            while(angle >= 360){
                angle -= 360;
            }
            if(angle == 0){
                red = 255;
            }else if(angle == 60){
                red = 255;
                green = 255;
            }else if(angle == 120){
                green = 255;
            }else if(angle == 180){
                green = 255;
                blue = 255;
            }else if(angle == 240){
                blue = 255;
            }else if(angle == 300){
                blue = 255;
                red = 255;
            } else if(angle > 0 && angle < 60){
                red = 255;
                green = 255 - (int)(everyRGBdAngle * (60 - angle));
            }else if(angle > 60 && angle < 120){
                green = 255;
                red = 255 - (int)(everyRGBdAngle * (angle - 60));
            }else if(angle > 120 && angle < 180){
                green = 255;
                blue = 255 - (int)(everyRGBdAngle * (180 - angle));
            }else if(angle > 180 && angle < 240){
                blue = 255;
                green = 255 - (int)(everyRGBdAngle * (angle - 180));
            }else if(angle > 240 && angle < 300){
                blue = 255;
                red = 255 - (int)(everyRGBdAngle * (300 - angle));
            }else{
                red = 255;
                blue = 255 - (int)(everyRGBdAngle * (angle - 300));
            }
            //亮度调节 百分比方式
            red += luminanceP / 100.0D * (255- red);
            green += luminanceP / 100.0D * (255- green);
            blue += luminanceP / 100.0D * (255- blue);

            //加入颜色
            colorList.add(Color.rgb(red,blue,green));

            //重置
            angle += everyAngle;
            red = 0;
            green = 0;
            blue = 0;
        }
        return colorList;
    }
    //Pc处理
    public static ArrayList<TallyAnalysisPC> analyseTalliesPC(ArrayList<ContentValues> contentValuesTallies, ArrayList<String> screenList) {
        if (contentValuesTallies == null || contentValuesTallies.size() == 0) {
            return null;
        } else {
            //基础插入
            ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList = new ArrayList<>();
            TallyAnalysisPC tallyAnalysisPCIncome = new TallyAnalysisPC();
            tallyAnalysisPCIncome.signName = "收入";
            tallyAnalysisPCIncome.screen = "总计";
            tallyAnalysisPCIncome.allMoney = 0;
            tallyAnalysisPCArrayList.add(tallyAnalysisPCIncome);

            TallyAnalysisPC tallyAnalysisPCOutcome = new TallyAnalysisPC();
            tallyAnalysisPCOutcome.signName = "支出";
            tallyAnalysisPCOutcome.screen = "总计";
            tallyAnalysisPCOutcome.allMoney = 0;
            tallyAnalysisPCArrayList.add(tallyAnalysisPCOutcome);

            TallyAnalysisPC tallyAnalysisPCTransfer = new TallyAnalysisPC();
            tallyAnalysisPCTransfer.signName = "转账";
            tallyAnalysisPCTransfer.screen = "总计";
            tallyAnalysisPCTransfer.allMoney = 0;
            tallyAnalysisPCArrayList.add(tallyAnalysisPCTransfer);

            int len = contentValuesTallies.size();
            int i;
            for(i = 0; i < len;i++){
                ContentValues contentValuesTally = contentValuesTallies.get(i);
                String actionType = contentValuesTally.getAsString(Constants.tallyTableColumn_actionType);
                if(actionType.equals(ActionType.INCOME.toString())){
                    addTallyIntoNode(contentValuesTally,tallyAnalysisPCIncome,screenList);
                    if(contentValuesTally.getAsString(Constants.tallyTableColumn_c1).equals("转账收入")){
                        addTallyIntoNode(contentValuesTally,tallyAnalysisPCTransfer,screenList);
                    }
                }else{
                    addTallyIntoNode(contentValuesTally,tallyAnalysisPCOutcome,screenList);
                    if(contentValuesTally.getAsString(Constants.tallyTableColumn_c1).equals("转账支出")){
                        addTallyIntoNode(contentValuesTally,tallyAnalysisPCTransfer,screenList);
                    }
                }
            }
            //详细信息统计
            getDetailedInformation(tallyAnalysisPCArrayList);
            return tallyAnalysisPCArrayList;
        }
    }

    private static void addTallyIntoNode(ContentValues contentValuesTally, TallyAnalysisPC tallyAnalysisPC, ArrayList<String> screenList){;
        TallyAnalysisPC tallyAnalysisPCTmp;
        tallyAnalysisPC.allMoney += contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
        tallyAnalysisPCTmp = tallyAnalysisPC;
        ArrayList<TallyAnalysisPC> nowTallyAnalysisPCArrayList;
        nowTallyAnalysisPCArrayList = tallyAnalysisPCTmp.nextScreen;
        int allLevel = screenList.size();
        int nowLevel = 0;
        //仅使用tallyAnalysisPCTmp，nowTallyAnalysisPCArrayList
        while(nowLevel < allLevel){
            if(nowTallyAnalysisPCArrayList == null){
                tallyAnalysisPCTmp.nextScreen = new ArrayList<>();
                nowTallyAnalysisPCArrayList = tallyAnalysisPCTmp.nextScreen;
            }
            //获取当前正在统计的Screen
            String nowScreen = screenList.get(nowLevel);
            String nowTallySignName = contentValuesTally.getAsString(nowScreen);
            int i,len;
            len = nowTallyAnalysisPCArrayList.size();
            for(i = 0; i < len;i++){
                if(nowTallyAnalysisPCArrayList.get(i).signName.equals(nowTallySignName)){
                    nowTallyAnalysisPCArrayList.get(i).allMoney += contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
                    tallyAnalysisPCTmp = nowTallyAnalysisPCArrayList.get(i);
                    nowTallyAnalysisPCArrayList = tallyAnalysisPCTmp.nextScreen;
                    break;
                }
            }
            if(i == len){
                TallyAnalysisPC newTallyAnalysisPC = new TallyAnalysisPC();
                newTallyAnalysisPC.screen = nowScreen;
                newTallyAnalysisPC.allMoney = contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
                newTallyAnalysisPC.signName = nowTallySignName;
                nowTallyAnalysisPCArrayList.add(newTallyAnalysisPC);
                tallyAnalysisPCTmp = newTallyAnalysisPC;
                nowTallyAnalysisPCArrayList = tallyAnalysisPCTmp.nextScreen;
            }
            nowLevel += 1;
        }
    }

    private static void getDetailedInformation(ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList){
        double allMoney = 0;
        int len = tallyAnalysisPCArrayList.size();
        int i;
        for(i = 0; i < len; i++){
            allMoney += tallyAnalysisPCArrayList.get(i).allMoney;
        }
        float fanStart = 0.0f;
        ArrayList<Integer> colorList = createColor(len,50);
        for(i = 0; i < len; i++){
            TallyAnalysisPC nowTallyAnalysisPC = tallyAnalysisPCArrayList.get(i);
            nowTallyAnalysisPC.percent = nowTallyAnalysisPC.allMoney / allMoney;
            nowTallyAnalysisPC.selfColor = colorList.get(i);
            nowTallyAnalysisPC.fanStart = fanStart;
            nowTallyAnalysisPC.fanEnd = fanStart + (float)(nowTallyAnalysisPC.percent * 360.0D);
            fanStart = fanStart + (nowTallyAnalysisPC.fanEnd - nowTallyAnalysisPC.fanStart);
            if(nowTallyAnalysisPC.nextScreen != null){
                getDetailedInformation(nowTallyAnalysisPC.nextScreen);
            }
        }
    }

    //Cl处理
    public static ArrayList<TallyAnalysisCl> analyseTalliesCl(ArrayList<ContentValues> contentValuesTallies){
        //获取时间信息
        ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList = new ArrayList<>();
        if(contentValuesTallies == null){
            return tallyAnalysisClArrayList;
        }
        int len = contentValuesTallies.size();
        String startDateStr = contentValuesTallies.get(len - 1).getAsString(Constants.tallyTableColumn_date);
        String endDateStr = contentValuesTallies.get(0).getAsString(Constants.tallyTableColumn_date);
        //开始分析
        TallyAnalysisCl tallyAnalysisCl;

        tallyAnalysisCl = new TallyAnalysisCl();
        tallyAnalysisCl.signName = "收入";
        initializeTimeDivisionOfTallyAnalysisCl(tallyAnalysisCl);
        ArrayList<ContentValues> Income = classifyContentValuesTallies(contentValuesTallies,Constants.tallyTableColumn_actionType,ActionType.INCOME.toString());
        getDetailedInformationCl(tallyAnalysisCl,Income,startDateStr,endDateStr);
        tallyAnalysisClArrayList.add(tallyAnalysisCl);

        tallyAnalysisCl = new TallyAnalysisCl();
        tallyAnalysisCl.signName = "支出";
        initializeTimeDivisionOfTallyAnalysisCl(tallyAnalysisCl);
        ArrayList<ContentValues> Outcome = classifyContentValuesTallies(contentValuesTallies,Constants.tallyTableColumn_actionType, ActionType.OUTCOME.toString());
        getDetailedInformationCl(tallyAnalysisCl,Outcome,startDateStr,endDateStr);
        tallyAnalysisClArrayList.add(tallyAnalysisCl);

        len = tallyAnalysisClArrayList.size();
        ArrayList<Integer> integerArrayList = createColor(len,60);
        for(int i = 0; i < len; i++){
            tallyAnalysisClArrayList.get(i).selfColor = integerArrayList.get(i);
        }
        return tallyAnalysisClArrayList;
    }

    private static ArrayList<ContentValues> classifyContentValuesTallies(ArrayList<ContentValues> contentValuesTallies, String key, String dst){
        ArrayList<ContentValues> newContentValuesArrayList = new ArrayList<>();
        int i,len = contentValuesTallies.size();
        for(i = 0;i < len;i++){
            ContentValues contentValues = contentValuesTallies.get(i);
            if(contentValues.getAsString(key).equals(dst)){
                newContentValuesArrayList.add(contentValues);
            }
        }
        return newContentValuesArrayList;
    }

    private static void initializeTimeDivisionOfTallyAnalysisCl(TallyAnalysisCl tallyAnalysisCl){
        tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList = new ArrayList<>();
        TallyAnalysisClTimeDivision tallyAnalysisClTimeDivision;
        tallyAnalysisClTimeDivision = new TallyAnalysisClTimeDivision();
        tallyAnalysisClTimeDivision.divisionName = "年";
        tallyAnalysisClTimeDivision.maxMoney = 0;
        tallyAnalysisClTimeDivision.minMoney = 0;
        tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.add(tallyAnalysisClTimeDivision);

        tallyAnalysisClTimeDivision = new TallyAnalysisClTimeDivision();
        tallyAnalysisClTimeDivision.divisionName = "月";
        tallyAnalysisClTimeDivision.maxMoney = 0;
        tallyAnalysisClTimeDivision.minMoney = 0;
        tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.add(tallyAnalysisClTimeDivision);

        tallyAnalysisClTimeDivision = new TallyAnalysisClTimeDivision();
        tallyAnalysisClTimeDivision.divisionName = "周";
        tallyAnalysisClTimeDivision.maxMoney = 0;
        tallyAnalysisClTimeDivision.minMoney = 0;
        tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.add(tallyAnalysisClTimeDivision);

        tallyAnalysisClTimeDivision = new TallyAnalysisClTimeDivision();
        tallyAnalysisClTimeDivision.divisionName = "日";
        tallyAnalysisClTimeDivision.maxMoney = 0;
        tallyAnalysisClTimeDivision.minMoney = 0;
        tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.add(tallyAnalysisClTimeDivision);
    }

    private static void getDetailedInformationCl(TallyAnalysisCl tallyAnalysisCl, ArrayList<ContentValues> contentValuesTallies, String startDateStr, String endDateStr){
        //年 月 周 日
        int len = contentValuesTallies.size();


        len -= 1;       //注意此处================================================================
        //获取年数据
        TallyAnalysisClTimeDivision nowTallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(0);
        int nowCountYear;
        int count = len;
        calendar.setTime(Date.valueOf(startDateStr));
        nowCountYear = calendar.get(Calendar.YEAR);
        nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年");
        while(count >= 0){
            ContentValues contentValues = contentValuesTallies.get(count);
            calendar2.setTime(Date.valueOf(contentValues.getAsString(Constants.tallyTableColumn_date)));
            int nowYear = calendar2.get(Calendar.YEAR);
            while(nowCountYear != nowYear){
                calendar.add(Calendar.YEAR,1);
                nowCountYear += calendar.get(Calendar.YEAR);
                nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年");
            }
            nowTallyAnalysisClTimeDivision.addMoney(contentValues.getAsDouble(Constants.tallyTableColumn_money));
            count -= 1;
        }
        calendar2.setTime(Date.valueOf(endDateStr));
        int endYear = calendar2.get(Calendar.YEAR);
        while(nowCountYear != endYear){
            calendar.add(Calendar.YEAR,1);
            nowCountYear += calendar.get(Calendar.YEAR);
            nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年");
        }
        int len2 = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.size();
        for(count = 0; count < len2; count++){
            double nowMoney = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.get(count).Money;
            if(nowMoney > nowTallyAnalysisClTimeDivision.maxMoney){
                nowTallyAnalysisClTimeDivision.maxMoney = nowMoney;
            }
            if(nowMoney < nowTallyAnalysisClTimeDivision.minMoney){
                nowTallyAnalysisClTimeDivision.minMoney = nowMoney;
            }
        }

        //获取月数据
        nowTallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(1);
        int nowCountMonth;
        count = len;
        calendar.setTime(Date.valueOf(startDateStr));
        nowCountYear = calendar.get(Calendar.YEAR);
        nowCountMonth = calendar.get(Calendar.MONTH) + 1;
        nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月");
        while(count >= 0){
            ContentValues contentValues = contentValuesTallies.get(count);
            calendar2.setTime(Date.valueOf(contentValues.getAsString(Constants.tallyTableColumn_date)));
            int nowYear = calendar2.get(Calendar.YEAR);
            int nowMonth = calendar2.get(Calendar.MONTH) + 1;
            while(nowCountYear != nowYear || nowCountMonth != nowMonth){
                calendar.add(Calendar.MONTH,1);
                nowCountYear = calendar.get(Calendar.YEAR);
                nowCountMonth = calendar.get(Calendar.MONTH) + 1;
                nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月");
            }
            nowTallyAnalysisClTimeDivision.addMoney(contentValues.getAsDouble(Constants.tallyTableColumn_money));
            count -= 1;
        }
        calendar2.setTime(Date.valueOf(endDateStr));
        endYear = calendar2.get(Calendar.YEAR);
        int endMonth = calendar2.get(Calendar.MONTH) + 1;
        while(nowCountYear != endYear || nowCountMonth != endMonth){
            calendar.add(Calendar.MONTH,1);
            nowCountYear = calendar.get(Calendar.YEAR);
            nowCountMonth = calendar.get(Calendar.MONTH) + 1;
            nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月");
        }
        len2 = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.size();
        for(count = 0; count < len2; count++){
            double nowMoney = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.get(count).Money;
            if(nowMoney > nowTallyAnalysisClTimeDivision.maxMoney){
                nowTallyAnalysisClTimeDivision.maxMoney = nowMoney;
            }
            if(nowMoney < nowTallyAnalysisClTimeDivision.minMoney){
                nowTallyAnalysisClTimeDivision.minMoney = nowMoney;
            }
        }

        //获取周数据
        nowTallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(2);
        int nowCountWeek;
        count = len;
        calendar.setTime(Date.valueOf(startDateStr));
        nowCountYear = calendar.get(Calendar.YEAR);
        nowCountMonth = calendar.get(Calendar.MONTH) + 1;
        nowCountWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountWeek + "周");
        while(count >= 0){
            ContentValues contentValues = contentValuesTallies.get(count);
            calendar2.setTime(Date.valueOf(contentValues.getAsString(Constants.tallyTableColumn_date)));
            int nowYear = calendar2.get(Calendar.YEAR);
            int nowMonth = calendar2.get(Calendar.MONTH) + 1;
            int nowWeek = calendar2.get(Calendar.WEEK_OF_MONTH);
            while(nowCountYear != nowYear || nowCountMonth != nowMonth || nowCountWeek != nowWeek){
                calendar.add(Calendar.WEEK_OF_MONTH,1);
                nowCountYear = calendar.get(Calendar.YEAR);
                nowCountMonth = calendar.get(Calendar.MONTH) + 1;
                nowCountWeek = calendar.get(Calendar.WEEK_OF_MONTH);
                nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountWeek + "周");
            }
            nowTallyAnalysisClTimeDivision.addMoney(contentValues.getAsDouble(Constants.tallyTableColumn_money));
            count -= 1;
        }
        calendar2.setTime(Date.valueOf(endDateStr));
        endYear = calendar2.get(Calendar.YEAR);
        endMonth = calendar2.get(Calendar.MONTH) + 1;
        int endWeek = calendar2.get(Calendar.WEEK_OF_MONTH);
        while(nowCountYear !=  endYear || nowCountMonth != endMonth || nowCountWeek != endWeek){
            calendar.add(Calendar.WEEK_OF_MONTH,1);
            nowCountYear = calendar.get(Calendar.YEAR);
            nowCountMonth = calendar.get(Calendar.MONTH) + 1;
            nowCountWeek = calendar.get(Calendar.WEEK_OF_MONTH);
            nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountWeek + "周");
        }
        len2 = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.size();
        for(count = 0; count < len2; count++){
            double nowMoney = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.get(count).Money;
            if(nowMoney > nowTallyAnalysisClTimeDivision.maxMoney){
                nowTallyAnalysisClTimeDivision.maxMoney = nowMoney;
            }
            if(nowMoney < nowTallyAnalysisClTimeDivision.minMoney){
                nowTallyAnalysisClTimeDivision.minMoney = nowMoney;
            }
        }

        //获取日数据
        nowTallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(3);
        int nowCountDay;
        count = len;
        calendar.setTime(Date.valueOf(startDateStr));
        nowCountYear = calendar.get(Calendar.YEAR);
        nowCountMonth = calendar.get(Calendar.MONTH) + 1;
        nowCountDay = calendar.get(Calendar.DAY_OF_MONTH);
        nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountDay+ "日");
        while(count >= 0){
            ContentValues contentValues = contentValuesTallies.get(count);
            calendar2.setTime(Date.valueOf(contentValues.getAsString(Constants.tallyTableColumn_date)));
            int nowYear = calendar2.get(Calendar.YEAR);
            int nowMonth = calendar2.get(Calendar.MONTH) + 1;
            int nowDay = calendar2.get(Calendar.DAY_OF_MONTH);
            while(nowCountYear != nowYear || nowCountMonth != nowMonth || nowCountDay != nowDay){
                calendar.add(Calendar.DAY_OF_MONTH,1);
                nowCountYear = calendar.get(Calendar.YEAR);
                nowCountMonth = calendar.get(Calendar.MONTH) + 1;
                nowCountDay = calendar.get(Calendar.DAY_OF_MONTH);
                nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountDay + "日");
            }
            nowTallyAnalysisClTimeDivision.addMoney(contentValues.getAsDouble(Constants.tallyTableColumn_money));
            count -= 1;
        }
        calendar2.setTime(Date.valueOf(endDateStr));
        endYear = calendar2.get(Calendar.YEAR);
        endMonth = calendar2.get(Calendar.MONTH) + 1;
        int endDay = calendar2.get(Calendar.DAY_OF_MONTH);
        while(nowCountYear != endYear || nowCountMonth !=  endMonth || nowCountDay != endDay){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            nowCountYear = calendar.get(Calendar.YEAR);
            nowCountMonth = calendar.get(Calendar.MONTH) + 1;
            nowCountDay = calendar.get(Calendar.DAY_OF_MONTH);
            nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年" + nowCountMonth + "月" + nowCountDay + "日");
        }
        len2 = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.size();
        for(count = 0; count < len2; count++){
            double nowMoney = nowTallyAnalysisClTimeDivision.timeAndMoneyArrayList.get(count).Money;
            if(nowMoney > nowTallyAnalysisClTimeDivision.maxMoney){
                nowTallyAnalysisClTimeDivision.maxMoney = nowMoney;
            }
            if(nowMoney < nowTallyAnalysisClTimeDivision.minMoney){
                nowTallyAnalysisClTimeDivision.minMoney = nowMoney;
            }
        }
    }

}
