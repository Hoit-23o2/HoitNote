package com.example.hoitnote.utils.managers;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.hoitnote.customviews.CAMListAdapterPc;
import com.example.hoitnote.customviews.CAMListAdapterSgCl;
import com.example.hoitnote.customviews.CAMListAdapterTdCl;
import com.example.hoitnote.customviews.HoitNoteImageViewCl;
import com.example.hoitnote.customviews.HoitNoteImageViewPc;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.TallyAnalysisCl;
import com.example.hoitnote.models.TallyAnalysisClTimeDivision;
import com.example.hoitnote.models.TallyAnalysisPc;
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
    private HoitNoteImageViewPc hoitNoteImageViewPc;
    private CAMListAdapterPc CAMListAdapterPc;

    private ListView myListViewPc;
    private TextView myTextViewPc;
    private ContentValues showNameContentValues;

    //折线统计图相关
    static Calendar calendar = Calendar.getInstance();
    static Calendar calendar2 = Calendar.getInstance();

    private HoitNoteImageViewCl hoitNoteImageViewCl;
    private CAMListAdapterTdCl CAMListAdapterTdCl;
    private CAMListAdapterSgCl CAMListAdapterSgCl;
    private ListView myListViewClTimeDivision;
    private ListView myListViewClSignName;
    private ArrayList<String> timeDivisionList;


    public ChartAnalysisManager(Context context) {
        //初始化Pc
        hoitNoteImageViewPc = null;
        myListViewPc = null;
        this.context = context;
        //初始化Cl
        hoitNoteImageViewCl = null;
        timeDivisionList = new ArrayList<>();
        timeDivisionList.add("年");
        timeDivisionList.add("月");
        timeDivisionList.add("周");
        timeDivisionList.add("日");
    }

    //扇形统计图相关
    public void setHoitNoteImageViewPc(HoitNoteImageViewPc hoitNoteImageViewPc) {
        this.hoitNoteImageViewPc = hoitNoteImageViewPc;
        this.hoitNoteImageViewPc.setManager(this);
    }

    public void setListViewPc(ListView myListView) {
        this.myListViewPc = myListView;
        this.CAMListAdapterPc = new CAMListAdapterPc(context);
        this.myListViewPc.setAdapter(this.CAMListAdapterPc);
        this.CAMListAdapterPc.setManager(this);
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

    public void setTallyAnalysisPcListPc(ArrayList<TallyAnalysisPc> tallyAnalysisPcArrayList){
        //设置要绘制的数据array
        //设置扇形统计图数据array逻辑
        if(hoitNoteImageViewPc != null){
            hoitNoteImageViewPc.endAnimation();
            hoitNoteImageViewPc.setTallyAnalysisPcArrayList(tallyAnalysisPcArrayList);
        }
        if(myListViewPc != null){
            CAMListAdapterPc.setList(tallyAnalysisPcArrayList);
        }
        if(myTextViewPc != null){
            myTextViewPc.setText(showNameContentValues.getAsString("总计"));
        }
    }

    public void notifyEnterTallyAnalysisPc(TallyAnalysisPc tallyAnalysisPc, String key){
        //通知要深入
        //扇形统计图深入Screen逻辑
        if(hoitNoteImageViewPc != null){
            if(!hoitNoteImageViewPc.ifAnimation){
                hoitNoteImageViewPc.enterTallyAnalysisPc(tallyAnalysisPc);
                if(myListViewPc != null) {
                    CAMListAdapterPc.enterTallyAnalysisPc(tallyAnalysisPc);
                }
                if(myTextViewPc != null){
                    myTextViewPc.setText(showNameContentValues.getAsString(key));
                }
            }
        }else {
            if(myListViewPc != null){
                CAMListAdapterPc.enterTallyAnalysisPc(tallyAnalysisPc);
            }
            if(myTextViewPc != null){
                myTextViewPc.setText(showNameContentValues.getAsString(key));
            }
        }
    }

    public void notifyGoBackPc(String key){
        //通知要回退
        //扇形统计图深入Screen逻辑
        if(hoitNoteImageViewPc != null){
            if(!hoitNoteImageViewPc.ifAnimation){
                hoitNoteImageViewPc.goBack();
                if(myListViewPc != null) {
                    CAMListAdapterPc.goBack();
                }
                if(myTextViewPc != null){
                    myTextViewPc.setText(showNameContentValues.getAsString(key));
                }
            }
        }else {
            if(myListViewPc != null){
                CAMListAdapterPc.goBack();
            }
            if(myTextViewPc != null){
                myTextViewPc.setText(showNameContentValues.getAsString(key));
            }
        }
    }

    public void setBackgroundColorPc(int color){
        if(hoitNoteImageViewPc != null){
            hoitNoteImageViewPc.setSelfBackgroundColor(color);
        }
    }

    public void setCuttingLineColorPc(int color){
        if(hoitNoteImageViewPc != null){
            hoitNoteImageViewPc.setSelfCuttingLineColor(color);
        }
    }

    public void actImageViewPc(){
        if(hoitNoteImageViewPc != null){
            hoitNoteImageViewPc.actSelf();
        }
    }

    //折线统计图相关
    public void setHoitNoteImageViewCl(HoitNoteImageViewCl hoitNoteImageViewCl){
        this.hoitNoteImageViewCl = hoitNoteImageViewCl;
        this.hoitNoteImageViewCl.setManager(this);
    }

    public void setListViewCl(ListView listViewTD, ListView listViewSN){
        this.myListViewClTimeDivision= listViewTD;
        if(listViewTD != null){
            this.CAMListAdapterTdCl = new CAMListAdapterTdCl(context);
            this.myListViewClTimeDivision.setAdapter(this.CAMListAdapterTdCl);
            this.CAMListAdapterTdCl.setManager(this);
        }
        this.myListViewClSignName = listViewSN;
        if(listViewSN != null){
            this.CAMListAdapterSgCl = new CAMListAdapterSgCl(context);
            this.myListViewClSignName.setAdapter(this.CAMListAdapterSgCl);
            this.CAMListAdapterSgCl.setManager(this);
        }
    }

    public void setTallyAnalysisClListCl(ArrayList<TallyAnalysisCl> tallyAnalysisClListCl){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.endAnimation();
            hoitNoteImageViewCl.setTallyAnalysisClArrayList(tallyAnalysisClListCl);
        }
        if(myListViewClTimeDivision != null){
            CAMListAdapterTdCl.setList(timeDivisionList);
        }
        if(myListViewClSignName != null){
            CAMListAdapterSgCl.setList(tallyAnalysisClListCl);
        }
    }

    public void notifyTimeDivision(String newTimeDivision){
        if(hoitNoteImageViewCl != null){
            if(!hoitNoteImageViewCl.ifAnimation){
                hoitNoteImageViewCl.changeTimeDivision(newTimeDivision);
                if(myListViewClTimeDivision != null){
                    CAMListAdapterTdCl.changeTimeDivision(newTimeDivision);
                }
            }
        }else{
            if(myListViewClTimeDivision != null){
                CAMListAdapterTdCl.changeTimeDivision(newTimeDivision);
            }
        }
    }

    public void notifyMainSignChange(TallyAnalysisCl tallyAnalysisCl){
        if(hoitNoteImageViewCl != null){
            if(!hoitNoteImageViewCl.ifAnimation){
                hoitNoteImageViewCl.changeMainSign(tallyAnalysisCl);
                CAMListAdapterSgCl.notifyDataSetChanged();
            }
        }
    }

    public void setBackgroundColorCl(int color){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.setSelfBackgroundColor(color);
        }
    }

    public void setInsideBackgroundColorCl(int color){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.setSelfInsideBackgroundColor(color);
        }
    }

    public void setLineColorCl(int color){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.setSelfLineColor(color);
        }
    }

    public void setTextColorCl(int color){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.setSelfTextColor(color);
        }
    }

    public void actImageViewCl(){
        if(hoitNoteImageViewCl != null){
            hoitNoteImageViewCl.actSelf();
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
    public static ArrayList<TallyAnalysisPc> analyseTalliesPc(ArrayList<ContentValues> contentValuesTallies, ArrayList<String> screenList) {
        if (contentValuesTallies == null || contentValuesTallies.size() == 0) {
            return null;
        } else {
            //基础插入
            ArrayList<TallyAnalysisPc> tallyAnalysisPcArrayList = new ArrayList<>();
            TallyAnalysisPc tallyAnalysisPcIncome = new TallyAnalysisPc();
            tallyAnalysisPcIncome.signName = "收入";
            tallyAnalysisPcIncome.screen = "总计";
            tallyAnalysisPcIncome.allMoney = 0;
            tallyAnalysisPcArrayList.add(tallyAnalysisPcIncome);

            TallyAnalysisPc tallyAnalysisPcOutcome = new TallyAnalysisPc();
            tallyAnalysisPcOutcome.signName = "支出";
            tallyAnalysisPcOutcome.screen = "总计";
            tallyAnalysisPcOutcome.allMoney = 0;
            tallyAnalysisPcArrayList.add(tallyAnalysisPcOutcome);

            TallyAnalysisPc tallyAnalysisPcTransfer = new TallyAnalysisPc();
            tallyAnalysisPcTransfer.signName = "转账";
            tallyAnalysisPcTransfer.screen = "总计";
            tallyAnalysisPcTransfer.allMoney = 0;
            tallyAnalysisPcArrayList.add(tallyAnalysisPcTransfer);

            int len = contentValuesTallies.size();
            int i;
            for(i = 0; i < len;i++){
                ContentValues contentValuesTally = contentValuesTallies.get(i);
                String actionType = contentValuesTally.getAsString(Constants.tallyTableColumn_actionType);
                if(actionType.equals(ActionType.INCOME.toString())){
                    addTallyIntoNode(contentValuesTally,tallyAnalysisPcIncome,screenList);
                    if(contentValuesTally.getAsString(Constants.tallyTableColumn_c1).equals("转账收入")){
                        addTallyIntoNode(contentValuesTally,tallyAnalysisPcTransfer,screenList);
                    }
                }else{
                    addTallyIntoNode(contentValuesTally,tallyAnalysisPcOutcome,screenList);
                    if(contentValuesTally.getAsString(Constants.tallyTableColumn_c1).equals("转账支出")){
                        addTallyIntoNode(contentValuesTally,tallyAnalysisPcTransfer,screenList);
                    }
                }
            }
            //详细信息统计
            getDetailedInformation(tallyAnalysisPcArrayList);
            return tallyAnalysisPcArrayList;
        }
    }

    private static void addTallyIntoNode(ContentValues contentValuesTally, TallyAnalysisPc tallyAnalysisPc, ArrayList<String> screenList){;
        TallyAnalysisPc tallyAnalysisPcTmp;
        tallyAnalysisPc.allMoney += contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
        tallyAnalysisPcTmp = tallyAnalysisPc;
        ArrayList<TallyAnalysisPc> nowTallyAnalysisPcArrayList;
        nowTallyAnalysisPcArrayList = tallyAnalysisPcTmp.nextScreen;
        int allLevel = screenList.size();
        int nowLevel = 0;
        //仅使用tallyAnalysisPcTmp，nowTallyAnalysisPcArrayList
        while(nowLevel < allLevel){
            if(nowTallyAnalysisPcArrayList == null){
                tallyAnalysisPcTmp.nextScreen = new ArrayList<>();
                nowTallyAnalysisPcArrayList = tallyAnalysisPcTmp.nextScreen;
            }
            //获取当前正在统计的Screen
            String nowScreen = screenList.get(nowLevel);
            String nowTallySignName = contentValuesTally.getAsString(nowScreen);
            int i,len;
            len = nowTallyAnalysisPcArrayList.size();
            for(i = 0; i < len;i++){
                if(nowTallyAnalysisPcArrayList.get(i).signName.equals(nowTallySignName)){
                    nowTallyAnalysisPcArrayList.get(i).allMoney += contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
                    tallyAnalysisPcTmp = nowTallyAnalysisPcArrayList.get(i);
                    nowTallyAnalysisPcArrayList = tallyAnalysisPcTmp.nextScreen;
                    break;
                }
            }
            if(i == len){
                TallyAnalysisPc newTallyAnalysisPc = new TallyAnalysisPc();
                newTallyAnalysisPc.screen = nowScreen;
                newTallyAnalysisPc.allMoney = contentValuesTally.getAsDouble(Constants.tallyTableColumn_money);
                newTallyAnalysisPc.signName = nowTallySignName;
                nowTallyAnalysisPcArrayList.add(newTallyAnalysisPc);
                tallyAnalysisPcTmp = newTallyAnalysisPc;
                nowTallyAnalysisPcArrayList = tallyAnalysisPcTmp.nextScreen;
            }
            nowLevel += 1;
        }
    }

    private static void getDetailedInformation(ArrayList<TallyAnalysisPc> tallyAnalysisPcArrayList){
        double allMoney = 0;
        int len = tallyAnalysisPcArrayList.size();
        int i;
        for(i = 0; i < len; i++){
            allMoney += tallyAnalysisPcArrayList.get(i).allMoney;
        }
        float fanStart = 0.0f;
        ArrayList<Integer> colorList = createColor(len,50);
        for(i = 0; i < len; i++){
            TallyAnalysisPc nowTallyAnalysisPc = tallyAnalysisPcArrayList.get(i);
            nowTallyAnalysisPc.percent = nowTallyAnalysisPc.allMoney / allMoney;
            nowTallyAnalysisPc.selfColor = colorList.get(i);
            nowTallyAnalysisPc.fanStart = fanStart;
            nowTallyAnalysisPc.fanEnd = fanStart + (float)(nowTallyAnalysisPc.percent * 360.0D);
            fanStart = fanStart + (nowTallyAnalysisPc.fanEnd - nowTallyAnalysisPc.fanStart);
            if(nowTallyAnalysisPc.nextScreen != null){
                getDetailedInformation(nowTallyAnalysisPc.nextScreen);
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
        ArrayList<Integer> integerArrayList = createColor(len,10);
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
