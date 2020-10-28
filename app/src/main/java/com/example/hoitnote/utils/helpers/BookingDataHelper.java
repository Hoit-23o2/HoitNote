package com.example.hoitnote.utils.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.HzsExpandItemTallyBinding;
import com.example.hoitnote.databinding.PopupwindowDialogNormalBinding;
import com.example.hoitnote.databinding.PopupwindowTallyInfoBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.enums.ThirdPartyType;
import com.example.hoitnote.viewmodels.AccountCardViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BookingDataHelper {

    public static List<String> getClassifications1(BookingType bookingType) {
        if(bookingType == BookingType.INCOME){
            incomeClassifications1.clear();
            incomeClassifications1 = App.dataBaseHelper.getAllClassification1(true,ActionType.INCOME);
            return incomeClassifications1;
        }else{
            outcomeClassifications1.clear();
            outcomeClassifications1 = App.dataBaseHelper.getAllClassification1(true,ActionType.OUTCOME);
            return outcomeClassifications1;
        }
    }

    public static List<List<String>> getClassifications2(BookingType bookingType) {
        if(bookingType == BookingType.INCOME){
            incomeClassifications2.clear();
            for(String classname: incomeClassifications1){
                incomeClassifications2.add(App.dataBaseHelper.getClassification2(classname,true,ActionType.INCOME));
            }
            return incomeClassifications2;
        }else{
            outcomeClassifications2.clear();
            for(String classname: outcomeClassifications1){
                outcomeClassifications2.add(App.dataBaseHelper.getClassification2(classname,true,ActionType.OUTCOME));
            }
            return outcomeClassifications2;
        }

    }

    public static List<String> getClassifications1WithIcons(BookingType bookingType){
        IconType iconType;
        switch (bookingType){
            case OUTCOME:
                iconType = IconType.OUTCOMECLASS1;
                break;
            default:
                iconType = IconType.INCOMECLASS1;
                break;
        }
        List<String> list = new ArrayList<>();
        if(bookingType == BookingType.INCOME){
            list = App.dataBaseHelper.getAllClassification1(true,ActionType.INCOME);
        }else{
            list = App.dataBaseHelper.getAllClassification1(true,ActionType.OUTCOME);
        }
        List<String> stringList = new ArrayList<>();
        for(String string:list){
            String iconCode = App.dataBaseHelper.getIconInformation(string,iconType);
            stringList.add(iconCode + " " + string);
        }
        return stringList;
    }

    public static List<List<String>> getClassifications2WithIcons(BookingType bookingType){
        IconType iconType;
        switch (bookingType){
            case OUTCOME:
                iconType = IconType.OUTCOMECLASS2;
                break;
            default:
                iconType = IconType.INCOMECLASS2;
                break;
        }
        List<String> classifications1 = getClassifications1(bookingType);
        List<List<String>> list = getClassifications2(bookingType);
        List<List<String>> stringList = new ArrayList<>();
        for(int i = 0;i<classifications1.size();i++){
            List<String> tempList = new ArrayList<>();
            for(int j=0;j<list.get(i).size();j++){
                String iconCode = App.dataBaseHelper.getIconInformation(classifications1.get(i)+list.get(i).get(j),iconType);
                tempList.add(iconCode + " " +list.get(i).get(j));
            }
            stringList.add(tempList);
        }
        return stringList;
    }


    public static List<String> getAccounts() {

        List<Account> list = App.dataBaseHelper.getAccounts();
        List<String> accountsList = new ArrayList<>();
        for(Account account:list){
            if(account.getAccountCode() == null || account.getAccountCode() == ""){
                accountsList.add(account.getAccountName());
            }else{
                accountsList.add(account.getAccountName()+" "+ConvertHelper.cutoffAccountCode(account.getAccountCode()));
            }
        }
        return accountsList;
    }

    public static List<String> getPersons() {
        return App.dataBaseHelper.getThirdParties(ThirdPartyType.MEMBER);
    }

    public static List<String> getPersonsWithIcons(){
        List<String> list = App.dataBaseHelper.getThirdParties(ThirdPartyType.MEMBER);
        List<String> stringList = new ArrayList<>();
        for(String string:list){
            String iconCode = App.dataBaseHelper.getIconInformation(string,IconType.MEMBER);
            stringList.add(iconCode+" "+string);
        }
        return stringList;
    }

    public static List<String> getStores() {
        return App.dataBaseHelper.getThirdParties(ThirdPartyType.VENDOR);
    }

    public static List<String> getStoresWithIcons(){
        List<String> list = App.dataBaseHelper.getThirdParties(ThirdPartyType.VENDOR);
        List<String> stringList = new ArrayList<>();
        for(String string:list){
            String iconCode = App.dataBaseHelper.getIconInformation(string,IconType.VENDOR);
            stringList.add(iconCode+" "+string);
        }
        return stringList;
    }
    public static List<String> getProjects() {
        return App.dataBaseHelper.getThirdParties(ThirdPartyType.PROJECT);
    }
    public static List<String> getProjectsWithIcons(){
        List<String> list = App.dataBaseHelper.getThirdParties(ThirdPartyType.PROJECT);
        List<String> stringList = new ArrayList<>();
        for(String string:list){
            String iconCode = App.dataBaseHelper.getIconInformation(string,IconType.PROJECT);
            stringList.add(iconCode+" "+string);
        }
        return stringList;
    }
    public static Account getNowAccount() {
        return nowAccount;
    }

    public static void setNowAccount(Account nowAccount) {
        BookingDataHelper.nowAccount = nowAccount;
    }

    private static List<String> outcomeClassifications1 = new ArrayList<>();
    private static List<List<String>> outcomeClassifications2 = new ArrayList<>();
    private static List<String> incomeClassifications1 = new ArrayList<>();
    private static List<List<String>> incomeClassifications2 = new ArrayList<>();
    private static List<String> accounts = new ArrayList<>();
    private static List<String> persons = new ArrayList<>();
    private static List<String> stores = new ArrayList<>();
    private static List<String> projects = new ArrayList<>();
    private static Account nowAccount;
    static {
        if(!App.dataBaseHelper.isHasCreated()){
            initClassifications();
            initPersons();
            initStores();
            initProjects();
        }else{
            ArrayList<Account> accountArrayList = new ArrayList<>();
            accountArrayList = App.dataBaseHelper.getAccounts();
            for(Account account:accountArrayList){
                if(account.getAccountCode() == null || account.getAccountCode() == ""){
                    accounts.add(account.getAccountName());
                }else{
                    accounts.add(account.getAccountName()+" "+account.getAccountCode());
                }
            }
            persons = App.dataBaseHelper.getThirdParties(ThirdPartyType.MEMBER);
            stores = App.dataBaseHelper.getThirdParties(ThirdPartyType.VENDOR);
            projects = App.dataBaseHelper.getThirdParties(ThirdPartyType.PROJECT);
        }

    }
    private static void initClassifications(){    //为数据库添加系统默认的分类数据
        addClassification1("食品酒水",ActionType.OUTCOME);
        addClassification2("食品酒水","早午晚餐",ActionType.OUTCOME);
        addClassification2("食品酒水","烟酒茶",ActionType.OUTCOME);
        addClassification2("食品酒水","水果零食",ActionType.OUTCOME);
        addClassification1("休闲娱乐",ActionType.OUTCOME);
        addClassification2("休闲娱乐","运动健身",ActionType.OUTCOME);
        addClassification2("休闲娱乐","旅游度假",ActionType.OUTCOME);
        addClassification2("休闲娱乐","宠物宝贝",ActionType.OUTCOME);
        addClassification1("职业收入",ActionType.INCOME);
        addClassification2("职业收入","工资收入",ActionType.INCOME);
        addClassification2("职业收入","利息收入",ActionType.INCOME);
        addClassification2("职业收入","加班收入",ActionType.INCOME);
        addClassification1("其它收入",ActionType.INCOME);
        addClassification2("其它收入","礼金收入",ActionType.INCOME);
        addClassification2("其它收入","中奖收入",ActionType.INCOME);
        addClassification2("其它收入","经营所得",ActionType.INCOME);
        App.dataBaseHelper.addIconInformation("食品酒水", IconType.OUTCOMECLASS1,"\uf5d7");
        App.dataBaseHelper.addIconInformation("食品酒水" + "无", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("食品酒水" + "早午晚餐", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("食品酒水" + "烟酒茶", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("食品酒水" + "水果零食", IconType.OUTCOMECLASS2,"\uf5d7");

        App.dataBaseHelper.addIconInformation("休闲娱乐", IconType.OUTCOMECLASS1,"\uf5d7");
        App.dataBaseHelper.addIconInformation("休闲娱乐" + "无", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("休闲娱乐" + "运动健身", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("休闲娱乐" + "旅游度假", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("休闲娱乐" + "宠物", IconType.OUTCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("职业收入", IconType.INCOMECLASS1,"\uf5d7");
        App.dataBaseHelper.addIconInformation("职业收入" + "无", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("职业收入" + "工资收入", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("职业收入" + "利息收入", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("职业收入" + "加班收入", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("其它收入", IconType.INCOMECLASS1,"\uf5d7");
        App.dataBaseHelper.addIconInformation("其它收入" + "无", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("其它收入" + "礼金收入", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("其它收入" + "中奖收入", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("其它收入" + "经营所得", IconType.INCOMECLASS2,"\uf5d7");
        App.dataBaseHelper.addIconInformation("转账收入", IconType.INCOMECLASS1,Constants.IconIncome);
        App.dataBaseHelper.addIconInformation("转账收入" + "转账收入", IconType.INCOMECLASS2,Constants.IconIncome);
        App.dataBaseHelper.addIconInformation("转账支出", IconType.OUTCOMECLASS1,Constants.IconOutcome);
        App.dataBaseHelper.addIconInformation("转账支出" + "转账支出", IconType.OUTCOMECLASS2,Constants.IconOutcome);
    }
    private static void initPersons(){
        addPerson("无");
        addPerson("我");
        addPerson("伴侣");
        addPerson("父亲");
        addPerson("母亲");
        App.dataBaseHelper.addIconInformation("无", IconType.MEMBER, Constants.IconNormal);
        App.dataBaseHelper.addIconInformation("我", IconType.MEMBER,"\uf5d7");
        App.dataBaseHelper.addIconInformation("伴侣", IconType.MEMBER,"\uf5d7");
        App.dataBaseHelper.addIconInformation("父亲", IconType.MEMBER,"\uf5d7");
        App.dataBaseHelper.addIconInformation("母亲", IconType.MEMBER,"\uf5d7");
    }
    private static void initStores(){
        addStore("无");
        addStore("饭堂");
        addStore("商场");
        addStore("超市");
        addStore("银行");
        App.dataBaseHelper.addIconInformation("无", IconType.VENDOR,Constants.IconNormal);
        App.dataBaseHelper.addIconInformation("饭堂", IconType.VENDOR,"\uf5d7");
        App.dataBaseHelper.addIconInformation("商场", IconType.VENDOR,"\uf5d7");
        App.dataBaseHelper.addIconInformation("超市", IconType.VENDOR,"\uf5d7");
        App.dataBaseHelper.addIconInformation("银行", IconType.VENDOR,"\uf5d7");
    }
    private static void initProjects(){
        addProject("无");
        addProject("回家过年");
        addProject("出差");
        addProject("旅游");
        App.dataBaseHelper.addIconInformation("无", IconType.PROJECT,Constants.IconNormal);
        App.dataBaseHelper.addIconInformation("回家过年", IconType.PROJECT,"\uf5d7");
        App.dataBaseHelper.addIconInformation("出差", IconType.PROJECT,"\uf5d7");
        App.dataBaseHelper.addIconInformation("旅游", IconType.PROJECT,"\uf5d7");
    }
    private static void initAccounts(){
        addAccount("现金","1111");
        addAccount("饭卡","2222");
        addAccount("平安银行","3333");
    }
    public static void addClassification1(String classification1, ActionType actionType){
        App.dataBaseHelper.addClassification(classification1,"无",actionType);
    }
    public static void addClassification2(String classfication1, String classfication2,ActionType actionType){
        App.dataBaseHelper.addClassification(classfication1,classfication2,actionType);

    }

    public static void addPerson(String person){
        App.dataBaseHelper.addThirdParty(ThirdPartyType.MEMBER,person);
    }
    public static void addStore(String store){
        App.dataBaseHelper.addThirdParty(ThirdPartyType.VENDOR,store);
    }
    public static void addProject(String project){
        App.dataBaseHelper.addThirdParty(ThirdPartyType.PROJECT,project);
    }
    public static void addAccount(String name, String code){
        Account account = new Account(name,code);
        App.dataBaseHelper.addAccount(account);
        if(account.getAccountCode() == null || account.getAccountCode() == ""){
            accounts.add(name);
        }else{
            accounts.add(name+" "+code);
        }
    }

    public static void setClickListener(HzsExpandItemTallyBinding binding, Context context){

        PopupwindowTallyInfoBinding dialogNormalBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.popupwindow_tally_info,
                        null,
                        false
                );
        dialogNormalBinding.setTally(binding.getTally());
        final AlertDialog alertDialog =
                DialogHelper.buildDialog(context, dialogNormalBinding);
        dialogNormalBinding.hzsExpandItemTallyIcon.setBackgroundColor(ThemeHelper.generateColor(context));

        int height = DeviceHelper.getDeviceHeight(context);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) dialogNormalBinding.mainContainer.getLayoutParams();
        layoutParams.height = (int) (height * 0.7);
        dialogNormalBinding.mainContainer.setLayoutParams(layoutParams);

        binding.tallyMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });


    }
}
