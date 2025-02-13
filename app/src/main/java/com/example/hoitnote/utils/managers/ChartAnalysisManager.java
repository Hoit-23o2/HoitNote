package com.example.hoitnote.utils.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.hoitnote.R;
import com.example.hoitnote.adapters.analysis.charts.CAMFirstScreenListAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMPCListAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMPCRVAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMTdClListAdapter;
import com.example.hoitnote.adapters.analysis.charts.CAMSgClListAdapter;
import com.example.hoitnote.customviews.FontAwesome;
import com.example.hoitnote.customviews.charts.HoitNoteClView;
import com.example.hoitnote.customviews.charts.HoitNotePCView;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.charts.ChooseScreenListNode;
import com.example.hoitnote.models.charts.TallyAnalysisCl;
import com.example.hoitnote.models.charts.TallyAnalysisClTimeDivision;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.ThirdPartyType;
import com.example.hoitnote.utils.helpers.DeviceHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.views.analysis.AnalysisActivity;
import com.example.hoitnote.views.analysis.InformationFragment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChartAnalysisManager {
    private static Random random = new Random(System.currentTimeMillis());

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    //数据相关
    private boolean ifHaveData;

    //扇形统计图相关
    private FontAwesome pcIcon;
    private TextView pcNoDataHint;
    private HoitNotePCView hoitNotePCView;
    private ListView myListViewPc;
    private CAMPCListAdapter CAMPCListAdapter;
    private RecyclerView recyclerViewShowScreen;
    private CAMPCRVAdapter campcrvAdapter;
    private ContentValues screenContentValues;

    //折线统计图相关
    static Calendar calendar = Calendar.getInstance();
    static Calendar calendar2 = Calendar.getInstance();
    private HoitNoteClView hoitNoteCLView;
    private CAMTdClListAdapter CAMTdClListAdapter;
    private CAMSgClListAdapter CAMSgClListAdapter;
    private ListView myListViewClTimeDivision;
    private ListView myListViewClSignName;
    private ArrayList<String> timeDivisionList;

    //对话框相关
    private boolean ifAnalysing = false;
    private AlertDialog dialog,dialogInformation;
    private ArrayList<ChooseScreenListNode> chooseScreenListNodeArrayList;      //包括一级，二级的ScreenList
    private boolean[] firstLevelCheckedList;            //一级checkedList
    private ArrayList<boolean[]> checkedList;           //二级checkedList
    private ArrayList<String> chooseScreenList;         //初始化于initialDialog中,其中包含了已选中的一级Screen，而且是按选中的先后顺序排列
    private TextView mTvStartTime,mTvEndTime;           //对话框中选择时间
    ArrayList<Tally> correspondingTallies;              //初始化于getCorrespondingTallies
    Account account;
    View dialogView;




    public ChartAnalysisManager(Context context) {
        /*初始化基础*/
        ifHaveData = false;

        //初始化Pc
        hoitNotePCView = null;
        myListViewPc = null;
        this.context = context;
        this.screenContentValues = new ContentValues();
        this.screenContentValues.put(Constants.tallyTableColumn_c1,"分类一");
        this.screenContentValues.put(Constants.tallyTableColumn_c2,"分类二");
        this.screenContentValues.put(Constants.tallyTableColumn_project,"项   目");
        this.screenContentValues.put(Constants.tallyTableColumn_member,"成   员");
//        this.screenContentValues.put(Constants.tallyTableColumn_account,"账   户");
        this.screenContentValues.put(Constants.tallyTableColumn_vendor,"商   家");

        //初始化Cl
        hoitNoteCLView = null;
        timeDivisionList = new ArrayList<>();
        timeDivisionList.add("年");
        timeDivisionList.add("月");
        timeDivisionList.add("周");
        timeDivisionList.add("日");

        //初始化对话框
        initializeDialog();
        ifAnalysing = false;
    }

    public boolean isIfHaveData() {
        return ifHaveData;
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

    public void setRecyclerViewShowScreen(RecyclerView recyclerViewShowScreen){
        this.recyclerViewShowScreen = recyclerViewShowScreen;
        this.campcrvAdapter = new CAMPCRVAdapter(context);
        this.recyclerViewShowScreen.setAdapter(campcrvAdapter);
        this.campcrvAdapter.setScreenContentValues(screenContentValues);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.recyclerViewShowScreen.setLayoutManager(linearLayoutManager);
    }

    public void setTallyAnalysisPCListPC(ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList,ArrayList<String> screenMarkList){
        //设置要绘制的数据array
        //设置扇形统计图数据array逻辑
        if(tallyAnalysisPCArrayList == null){
            ifHaveData = false;
        }else{
            ifHaveData = tallyAnalysisPCArrayList.size() > 0;
        }
        if(!ifHaveData){
            pcIcon.setVisibility(View.GONE);
            pcNoDataHint.setVisibility(View.VISIBLE);
        }else{
            pcIcon.setVisibility(View.VISIBLE);
            pcNoDataHint.setVisibility(View.GONE);
        }

        if(hoitNotePCView != null){
            hoitNotePCView.endAnimation();
            if(ifHaveData){
                hoitNotePCView.setTallyAnalysisPCArrayList(tallyAnalysisPCArrayList);
            }else{
                hoitNotePCView.setTallyAnalysisPCArrayList(null);
            }
        }
        if(myListViewPc != null){
            CAMPCListAdapter.setList(tallyAnalysisPCArrayList);
        }
        if(campcrvAdapter != null){
            if(tallyAnalysisPCArrayList == null || tallyAnalysisPCArrayList.size() == 0){
                campcrvAdapter.setScreenMarkList(null);
            }else{
                campcrvAdapter.setScreenMarkList(screenMarkList);
            }
            recyclerViewShowScreen.smoothScrollToPosition(0);
        }
    }

    public void notifyEnterTallyAnalysisPC(TallyAnalysisPC tallyAnalysisPC){
        //通知要深入
        //扇形统计图深入Screen逻辑
        if(hoitNotePCView != null){
            if(!hoitNotePCView.ifAnimation){
                hoitNotePCView.enterTallyAnalysisPC(tallyAnalysisPC);
                if(myListViewPc != null) {
                    CAMPCListAdapter.enterTallyAnalysisPC(tallyAnalysisPC);
                }
                if(campcrvAdapter != null){
                    campcrvAdapter.enterOnce();
                    recyclerViewShowScreen.smoothScrollToPosition(campcrvAdapter.nowPosition);
                }
            }
        }else {
            if(myListViewPc != null){
                CAMPCListAdapter.enterTallyAnalysisPC(tallyAnalysisPC);
            }
            if(campcrvAdapter != null){
                campcrvAdapter.enterOnce();
                recyclerViewShowScreen.smoothScrollToPosition(campcrvAdapter.nowPosition);
            }
        }
    }

    public void notifyGoBackPc(){
        //通知要回退
        //扇形统计图深入Screen逻辑
        if(hoitNotePCView != null){
            if(!hoitNotePCView.ifAnimation){
                hoitNotePCView.goBack();
                if(myListViewPc != null) {
                    CAMPCListAdapter.goBack();
                }
                if(campcrvAdapter != null){
                    campcrvAdapter.goBack();
                    recyclerViewShowScreen.smoothScrollToPosition(campcrvAdapter.nowPosition);
                }
            }
        }else {
            if(myListViewPc != null){
                CAMPCListAdapter.goBack();
            }
            if(campcrvAdapter != null){
                campcrvAdapter.goBack();
                recyclerViewShowScreen.smoothScrollToPosition(campcrvAdapter.nowPosition);
            }
        }
    }

    public void notifyDrawPC(){
        if(recyclerViewShowScreen != null){
            campcrvAdapter.notifyDataSetChanged();
        }
        if(myListViewPc != null){
            CAMPCListAdapter.notifyDataSetChanged();
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
        if(tallyAnalysisClListCl == null){
            ifHaveData = false;
        }else{
            ifHaveData = tallyAnalysisClListCl.size() > 0;
        }
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

    public void notifyDrawCL(){
        if(myListViewClSignName != null){
            CAMSgClListAdapter.notifyDataSetChanged();
        }
        if(myListViewClTimeDivision != null){
            CAMTdClListAdapter.notifyDataSetChanged();
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

    //对话框相关
    public void showDialog(){
        //重置时间选择
        mTvStartTime.setText("");
        mTvEndTime.setText("");
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //设置对话框大小
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = Objects.requireNonNull(window).getAttributes();
//        lp.gravity = Gravity.BOTTOM ;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//        window.setGravity(Gravity.BOTTOM);
//        window.getDecorView().setPadding( 0 , 0 , 0 , 0 );
    }

    public void setBtnActDialog(View btnActDialog){
        btnActDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ifAnalysing){
                    showDialog();
                }
            }
        });
    }

    public void initializeChooseScreenListNodeArrayList(){
        int i,j,len,len2,nowIndex;
        ArrayList<String> stringArrayList;
        chooseScreenListNodeArrayList = new ArrayList<>();
        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("分类一",Constants.tallyTableColumn_c1));
        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("分类二",Constants.tallyTableColumn_c2));
//        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("账   户",Constants.tallyTableColumn_account));
        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("成   员",Constants.tallyTableColumn_member));
        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("项   目",Constants.tallyTableColumn_project));
        chooseScreenListNodeArrayList.add(new ChooseScreenListNode("商   家",Constants.tallyTableColumn_vendor));

        //获取一级分类
        nowIndex = 0;
        ArrayList<String> classification1StringList;
        ArrayList<String> allClassification1StringList = new ArrayList<>();
        //获取收入一级分类
        classification1StringList = App.dataBaseHelper.getAllClassification1(true,ActionType.INCOME);
        len = classification1StringList.size();
        for(i = 0;i < len;i++){
            String nowClassification = classification1StringList.get(i) + "(收入)";
            allClassification1StringList.add(nowClassification);
        }
        //获取支出一级分类
        classification1StringList = App.dataBaseHelper.getAllClassification1(true,ActionType.OUTCOME);
        len = classification1StringList.size();
        for(i = 0;i < len;i++){
            String nowClassification = classification1StringList.get(i) + "(支出)";
            allClassification1StringList.add(nowClassification);
        }
        chooseScreenListNodeArrayList.get(nowIndex).content = allClassification1StringList;

        //获取二级分类
        nowIndex += 1;
        stringArrayList = new ArrayList<>();
        len = allClassification1StringList.size();
        for(i = 0; i < len;i++){
            String nowAllClassification1 = allClassification1StringList.get(i);
            int indexS = nowAllClassification1.indexOf("(");
            int indexE = nowAllClassification1.indexOf(")");
            String nowClassification1 = nowAllClassification1.substring(0,indexS);
            String nowActionType = nowAllClassification1.substring(indexS + 1,indexE);  //截断出来的不包括括号
            if(nowActionType.equals("收入")){
                ArrayList<String> classification2List = App.dataBaseHelper.getClassification2(nowClassification1,true,ActionType.INCOME);
                len2 = classification2List.size();
                for(j = 0; j < len2; j++){
                    stringArrayList.add(nowClassification1+"-"+classification2List.get(j)+"(收入)");
                }
            } else if(nowActionType.equals("支出")){
                ArrayList<String> classification2List = App.dataBaseHelper.getClassification2(nowClassification1,true,ActionType.OUTCOME);
                len2 = classification2List.size();
                for(j = 0; j < len2; j++){
                    stringArrayList.add(nowClassification1+"-"+classification2List.get(j)+"(支出)");
                }
            }
        }
        chooseScreenListNodeArrayList.get(nowIndex).content = stringArrayList;

        //获取账户
//        nowIndex+=1;
//        ArrayList<Account> accountArrayList = App.dataBaseHelper.getAccounts();
//        len = accountArrayList.size();
//        stringArrayList = new ArrayList<>();
//        for(i = 0; i < len;i++){
//            stringArrayList.add(accountArrayList.get(i).getAccountName()+"\n"+accountArrayList.get(i).getAccountCode());
//        }
//        chooseScreenListNodeArrayList.get(nowIndex).content = stringArrayList;

        //获取成员
        nowIndex += 1;
        chooseScreenListNodeArrayList.get(nowIndex).content = App.dataBaseHelper.getThirdParties(ThirdPartyType.MEMBER);

        //获取项目
        nowIndex += 1;
        chooseScreenListNodeArrayList.get(nowIndex).content = App.dataBaseHelper.getThirdParties(ThirdPartyType.PROJECT);

        //获取商家
        nowIndex += 1;
        chooseScreenListNodeArrayList.get(nowIndex).content = App.dataBaseHelper.getThirdParties(ThirdPartyType.VENDOR);
    }

    public void initializeCheckedList(){
        checkedList = new ArrayList<>();
        int i,len = chooseScreenListNodeArrayList.size();
        for(i = 0; i < len;i++){
            boolean[] booleans = new boolean[chooseScreenListNodeArrayList.get(i).content.size()];
            checkedList.add(booleans);
        }
        firstLevelCheckedList = new boolean [chooseScreenListNodeArrayList.size()];
    }

    public void setNowAccount(Account account){
        this.account = account;
    }

    public void getCorrespondingTallies(){
        int i,j,lenTallies,lenSecondScreen;
        boolean [] checkedArray;
        ArrayList<String> content;
        ArrayList<Tally> removeTallyArrayList =  new ArrayList<>();

        //筛选时间
        Date startDate = null,endDate = null;
        String startDateStr,endDateStr;
        startDateStr = mTvStartTime.getText().toString();
        if(startDateStr.length() != 0){
            startDate = Date.valueOf(startDateStr);
        }
        endDateStr = mTvEndTime.getText().toString();
        if(endDateStr.length() != 0){
            endDate = Date.valueOf(endDateStr);
        }
        DataBaseFilter filter = new DataBaseFilter(startDate,endDate,DataBaseFilter.IDInvalid,null,account,null);
        correspondingTallies = App.dataBaseHelper.getTallies(filter);
        //筛选其他Screen
        lenTallies = correspondingTallies.size();
        for(i = 0; i < lenTallies; i++){
            Tally tally = correspondingTallies.get(i);
            //筛选一级分类
            int nowIndex = 0;
            if(firstLevelCheckedList[nowIndex]){
                checkedArray = checkedList.get(nowIndex);
                lenSecondScreen = checkedArray.length;
                content = chooseScreenListNodeArrayList.get(nowIndex).content;
                String nowTallyAllClassification = "";
                if(tally.getActionType() == ActionType.INCOME){
                    nowTallyAllClassification = tally.getClassification1()+"(收入)";
                }else if(tally.getActionType() == ActionType.OUTCOME){
                    nowTallyAllClassification = tally.getClassification1()+"(支出)";
                }
                for(j = 0; j < lenSecondScreen ; j++){
                    if(!checkedArray[j] && nowTallyAllClassification.equals(content.get(j))){
                        removeTallyArrayList.add(tally);
                        break;
                    }
                }
                if(j != lenSecondScreen){
                    continue;
                }
            }
            //筛选二级分类  "c1-c2"
            nowIndex += 1;
            if(firstLevelCheckedList[nowIndex]){
                checkedArray = checkedList.get(nowIndex);
                lenSecondScreen = checkedArray.length;
                content = chooseScreenListNodeArrayList.get(nowIndex).content;
                String nowTallyAllClassification = "";
                if(tally.getActionType() == ActionType.INCOME){
                    nowTallyAllClassification = tally.getClassification1() + "-" + tally.getClassification2() + "(收入)";
                }else if(tally.getActionType() == ActionType.OUTCOME){
                    nowTallyAllClassification = tally.getClassification1() + "-" + tally.getClassification2() + "(支出)";
                }
                for(j = 0; j < lenSecondScreen ; j++){
                    if(!checkedArray[j] && nowTallyAllClassification.equals(content.get(j))){
                        removeTallyArrayList.add(tally);
                        break;
                    }
                }
                if(j != lenSecondScreen){
                    continue;
                }
            }
            //筛选"账户"  "ACN\nACC"
//            nowIndex += 1;
//            if(firstLevelCheckedList[nowIndex]){
//                checkedArray = checkedList.get(nowIndex);
//                lenSecondScreen = checkedArray.length;
//                content = chooseScreenListNodeArrayList.get(nowIndex).content;
//                Account account = tally.getAccount();
//                String tallyAccount = account.getAccountName() + "\n" + account.getAccountCode();
//                for(j = 0; j < lenSecondScreen ; j++){
//                    if(!checkedArray[j] && tallyAccount.equals(content.get(j))){
//                        removeTallyArrayList.add(tally);
//                        break;
//                    }
//                }
//                if(j != lenSecondScreen){
//                    continue;
//                }
//            }
            //筛选"成员"
            nowIndex += 1;
            if(firstLevelCheckedList[nowIndex]){
                checkedArray = checkedList.get(nowIndex);
                lenSecondScreen = checkedArray.length;
                content = chooseScreenListNodeArrayList.get(nowIndex).content;
                for(j = 0; j < lenSecondScreen ; j++){
                    if(!checkedArray[j] && tally.getMember().equals(content.get(j))){
                        removeTallyArrayList.add(tally);
                        break;
                    }
                }
                if(j != lenSecondScreen){
                    continue;
                }
            }
            //筛选"项目"
            nowIndex += 1;
            if(firstLevelCheckedList[nowIndex]){
                checkedArray = checkedList.get(nowIndex);
                lenSecondScreen = checkedArray.length;
                content = chooseScreenListNodeArrayList.get(nowIndex).content;
                for(j = 0; j < lenSecondScreen ; j++){
                    if(!checkedArray[j] && tally.getProject().equals(content.get(j))){
                        removeTallyArrayList.add(tally);
                        break;
                    }
                }
                if(j != lenSecondScreen){
                    continue;
                }
            }
            //筛选"商家"
            nowIndex += 1;
            if(firstLevelCheckedList[nowIndex]){
                checkedArray = checkedList.get(nowIndex);
                lenSecondScreen = checkedArray.length;
                content = chooseScreenListNodeArrayList.get(nowIndex).content;
                for(j = 0; j < lenSecondScreen ; j++){
                    if(!checkedArray[j] && tally.getVendor().equals(content.get(j))){
                        removeTallyArrayList.add(tally);
                        break;
                    }
                }
            }
        }
        //从correspondingTallies中移除不满足的Tally
        int lenRemove = removeTallyArrayList.size();
        for(i = 0;i<lenRemove;i++){
            correspondingTallies.remove(removeTallyArrayList.get(i));
        }
    }

    public void initializeDialog(){
        //选择分析内容对话框相关=============================================================
        //初始化Screen列表
        initializeChooseScreenListNodeArrayList();
        //初始化checked列表
        initializeCheckedList();
        chooseScreenList = new ArrayList<>();
        //建立对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cam_dialog_choose_screen,null);
        dialogView = view;

        /*获取屏幕相关属性*/
        int deviceHeight = DeviceHelper.getDeviceHeight(context);
        LinearLayout linearLayout = view.findViewById(R.id.popupViewContainer);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams)linearLayout.getLayoutParams();
        layoutParams.height = (int) (deviceHeight * 0.7f);
        linearLayout.setLayoutParams(layoutParams);

        dialog = builder.setView(view).create();
        /*获取，设置Screen-ListView*/
        /*一级Screen的ListView*/
        ListView mLvDialogScreen;
        /*一级Screen的ListView的适配器*/
        CAMFirstScreenListAdapter CAMFirstScreenListAdapter;
        mLvDialogScreen = view.findViewById(R.id.lv_screen);
        CAMFirstScreenListAdapter = new CAMFirstScreenListAdapter(context);
        CAMFirstScreenListAdapter.setList(chooseScreenListNodeArrayList);
        CAMFirstScreenListAdapter.setCheckedList(firstLevelCheckedList,checkedList);
        CAMFirstScreenListAdapter.setChooseScreenList(chooseScreenList);
        mLvDialogScreen.setAdapter(CAMFirstScreenListAdapter);
        //设置开始时间相关
        mTvStartTime = view.findViewById(R.id.tv_startDate);
        mTvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String string = year + "-" + (month+1) + "-" + dayOfMonth;
                        mTvStartTime.setText(string);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        //设置结束时间相关
        mTvEndTime = (TextView) view.findViewById(R.id.tv_endDate);
        mTvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String string = year + "-" + (month+1) + "-" + dayOfMonth;
                        mTvEndTime.setText(string);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        //确认选择好了需要分析的内容
        Button mBtnConfirmChooseScreen;             //终止对话框并返回
        mBtnConfirmChooseScreen = (Button)view.findViewById(R.id.bt_confirmChooseScreen);
        mBtnConfirmChooseScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                //此处展示出正在分析对话框
                ifAnalysing = true;
                getCorrespondingTallies();
                ArrayList<ContentValues> contentValuesArrayList = ChartAnalysisManager.getContentValuesTallies(correspondingTallies);
                //设置扇形统计图
                ArrayList<TallyAnalysisPC> tallyAnalysisPcArrayList = ChartAnalysisManager.analyseTalliesPC(contentValuesArrayList,chooseScreenList);
                setTallyAnalysisPCListPC(tallyAnalysisPcArrayList,chooseScreenList);
                //设置折线统计图
                ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList = ChartAnalysisManager.analyseTalliesCl(contentValuesArrayList);
                setTallyAnalysisClListCl(tallyAnalysisClArrayList);
                //Toast.makeText(context,"完成分析",Toast.LENGTH_SHORT).show();
                ifAnalysing = false;
            }
        });

        builder = new AlertDialog.Builder(context);
        View view2;
        view2 = LayoutInflater.from(context).inflate(R.layout.cam_dialog_information,null);
        dialogInformation = builder.setView(view2).create();
        Button button = view2.findViewById(R.id.bt_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInformation.hide();
                showDialog();
            }
        });

        ImageView imageView = view.findViewById(R.id.iv_dialog_show_information);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                dialogInformation.show();
                dialogInformation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });



    }


    public static RectF getViewRectF(View view){
        int width = view.getWidth();
        int height = view.getHeight();
        int [] location = new int[2];
        view.getLocationOnScreen(location);
        RectF rectf = new RectF();
        rectf.left = location[0];
        rectf.top = location[1];
        rectf.right = rectf.left + width;
        rectf.bottom = rectf.top + height;
        return rectf;
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
        ArrayList<Integer> colorList = new ArrayList<>();
        for(int i = 0;i < colorNum;i++){
            //加入颜色
            int color = (ThemeHelper.generateColor(context)| 0xFF000000);
            int len2 = colorList.size();
            int j;
            for(j = 0; j < len2;j++){
                int tmpColor = colorList.get(j);
                if(tmpColor == color){
                    break;
                }
            }
            if(j == len2){
                colorList.add(color);
            }else{
                i -= 1;
            }
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

            if(tallyAnalysisPCIncome.allMoney == 0.0d){
                tallyAnalysisPCArrayList.remove(tallyAnalysisPCIncome);
            }
            if(tallyAnalysisPCOutcome.allMoney == 0.0d){
                tallyAnalysisPCArrayList.remove(tallyAnalysisPCOutcome);
            }
            if(tallyAnalysisPCTransfer.allMoney == 0.0d){
                tallyAnalysisPCArrayList.remove(tallyAnalysisPCTransfer);
            }
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
                nowCountYear = calendar.get(Calendar.YEAR);
                nowTallyAnalysisClTimeDivision.addNewNode(nowCountYear + "年");
            }
            nowTallyAnalysisClTimeDivision.addMoney(contentValues.getAsDouble(Constants.tallyTableColumn_money));
            count -= 1;
        }
        calendar2.setTime(Date.valueOf(endDateStr));
        int endYear = calendar2.get(Calendar.YEAR);
        while(nowCountYear != endYear){
            calendar.add(Calendar.YEAR,1);
            nowCountYear = calendar.get(Calendar.YEAR);
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

    public void setPcNoDataHint(TextView pcNoDataHint) {
        this.pcNoDataHint = pcNoDataHint;
    }

    public void setPcIcon(FontAwesome pcIcon) {
        this.pcIcon = pcIcon;
    }
}
