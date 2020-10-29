package com.example.hoitnote.utils.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.Interfaces.IDataBase;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.utils.commuications.DataBaseFilter;
import com.example.hoitnote.utils.commuications.DataPackage;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.FilterType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.enums.PasswordStyle;
import com.example.hoitnote.utils.enums.Theme;
import com.example.hoitnote.utils.enums.ThirdPartyType;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
/*
 * 该类用于实现数据库交互方法,可能会用到SQLiteOpenHelper
 * */
public class DataBaseHelper extends SQLiteOpenHelper implements IDataBase {

    private String name;
    private Context mContext;
    private boolean hasCreated = true;
    private SQLiteDatabase sqLiteDatabase;

    public boolean isHasCreated() {
        return hasCreated;
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        initialize(context,name);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        initialize(context,name);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DataBaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
        initialize(context,name);
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        if(ifHaveSymbol(Constants.symbol_ifSaveDataPackage)){
            hasCreated = false;
            delSymbol(Constants.symbol_ifSaveDataPackage);
        }
    }

    public void initialize(Context context,String name){
        this.mContext = context;
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.createTallyTable);
        sqLiteDatabase.execSQL(Constants.createClassificationTable);
        sqLiteDatabase.execSQL(Constants.createConfigTable);
        sqLiteDatabase.execSQL(Constants.createThirdPartyTable);
        sqLiteDatabase.execSQL(Constants.createAccountTable);
        sqLiteDatabase.execSQL(Constants.createIconInformationTable);
        sqLiteDatabase.execSQL(Constants.createSymbolTable);
        hasCreated = false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public boolean myDeleteDatabase(Context context) {
        return context.deleteDatabase(name);
    }

    @Override
    public ArrayList<Tally> getTallies(DataBaseFilter filter) {
        ArrayList<Tally> tallyArrayList = new ArrayList<>();
        boolean ifAdd = false;
        String actionStr;
        Cursor cursor;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select * from ");
        stringBuilder.append(Constants.tallyTableName);
        if (filter != null) {
            /*
             * find by id
             * */
            if (filter.getId() != DataBaseFilter.IDInvalid) {
                stringBuilder.append(" where ");
                stringBuilder.append("(id=");
                stringBuilder.append(filter.getId());
                stringBuilder.append(") ");
                ifAdd = true;
            }
            /*
             * find by classification
             * */
            if (filter.getClassifications() != null && filter.getClassifications().size() != 0) {
                int i, lenc = filter.getClassifications().size();
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                for (i = 0; i < lenc; i++) {
                    String nowC1AndC2 = filter.getClassifications().get(i);
                    int spliterIndex = nowC1AndC2.indexOf(Constants.tallyTableC1C2Spliter);
                    if(spliterIndex == -1){
                        stringBuilder.append(Constants.tallyTableColumn_c1);
                        stringBuilder.append("='");
                        stringBuilder.append(nowC1AndC2);
                        stringBuilder.append("'");
                    }else{
                        stringBuilder.append(Constants.tallyTableColumn_c1);
                        stringBuilder.append("='");
                        stringBuilder.append(nowC1AndC2.substring(0,spliterIndex));
                        stringBuilder.append("'");
                        stringBuilder.append(" and ");
                        stringBuilder.append(Constants.tallyTableColumn_c2);
                        stringBuilder.append("='");
                        stringBuilder.append(nowC1AndC2.substring(spliterIndex+1));
                        stringBuilder.append("'");
                    }
                    if (i != lenc - 1) {
                        stringBuilder.append(" or ");
                    }
                }
                stringBuilder.append(") ");
                ifAdd = true;
            }
            /*
             * find by date
             * */
            if (filter.getStartDate() != null || filter.getEndDate() != null) {
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                if (filter.getStartDate() != null && filter.getEndDate() != null) {
                    stringBuilder.append(Constants.tallyTableColumn_date);
                    stringBuilder.append(" >= '");
                    stringBuilder.append(filter.getStartDate().toString());
                    stringBuilder.append("'");
                    stringBuilder.append(" and ");
                    stringBuilder.append(Constants.tallyTableColumn_date);
                    stringBuilder.append(" <= '");
                    stringBuilder.append(filter.getEndDate().toString());
                    stringBuilder.append("'");
                } else if (filter.getStartDate() != null) {
                    stringBuilder.append(Constants.tallyTableColumn_date);
                    stringBuilder.append(" >= '");
                    stringBuilder.append(filter.getStartDate().toString());
                    stringBuilder.append("'");
                } else if (filter.getEndDate() != null) {
                    stringBuilder.append(Constants.tallyTableColumn_date);
                    stringBuilder.append(" <= '");
                    stringBuilder.append(filter.getEndDate().toString());
                    stringBuilder.append("'");
                }
                stringBuilder.append(") ");
                ifAdd = true;
            }
            /*
             * find by account
             * */
            if(filter.getAccount() != null){
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                stringBuilder.append(Constants.tallyTableColumn_account);
                stringBuilder.append("='");
                stringBuilder.append(filter.getAccount().getAccountName());
                stringBuilder.append(" ");
                stringBuilder.append(filter.getAccount().getAccountCode());
                stringBuilder.append("'");
                stringBuilder.append(") ");
                ifAdd = true;
            }
            /*
             * find by actionType
             * */
            if(filter.getActionType() != null){
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                stringBuilder.append(Constants.tallyTableColumn_actionType);
                stringBuilder.append("=");
                stringBuilder.append(filter.getActionType().ordinal());
                stringBuilder.append(") ");
                ifAdd = true;
            }
            /*
             * find by thirdParty project
             * */
            if(filter.getProjectName() != null){
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                stringBuilder.append(Constants.tallyTableColumn_project);
                stringBuilder.append("='");
                stringBuilder.append(filter.getProjectName());
                stringBuilder.append("') ");
                ifAdd = true;
            }
            /*
             * find by thirdParty member
             * */
            if(filter.getMemberName() != null){
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                stringBuilder.append(Constants.tallyTableColumn_member);
                stringBuilder.append("='");
                stringBuilder.append(filter.getMemberName());
                stringBuilder.append("') ");
                ifAdd = true;
            }
            /*
             * find by thirdParty vendor
             * */
            if(filter.getVendorName() != null){
                if (ifAdd) {
                    stringBuilder.append("and ");
                }else{
                    stringBuilder.append(" where ");
                }
                stringBuilder.append("(");
                stringBuilder.append(Constants.tallyTableColumn_vendor);
                stringBuilder.append("='");
                stringBuilder.append(filter.getVendorName());
                stringBuilder.append("') ");
                ifAdd = true;
            }
        }
        actionStr = stringBuilder.toString();
        actionStr = actionStr + " order by "
                + Constants.tallyTableColumn_date + " desc, "
                + Constants.tallyTableColumn_time + " desc";
        cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            do{
                Tally newTally = new Tally();
                newTally.setId(cursor.getLong(cursor.getColumnIndex("id")));
                newTally.setMoney(cursor.getDouble(cursor.getColumnIndex(Constants.tallyTableColumn_money)));
                newTally.setDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_date))));
                newTally.setClassification1(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_c1)));
                newTally.setClassification2(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_c2)));
                newTally.setActionType(ActionType.values()[cursor.getInt(cursor.getColumnIndex(Constants.tallyTableColumn_actionType))]);
                newTally.setMember(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_member)));
                newTally.setProject(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_project)));
                newTally.setRemark(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_remark)));
                newTally.setVendor(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_vendor)));
                newTally.setTime(Time.valueOf(cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_time))));
                //获取Account字段信息
                String accountName,accountCode;
                String accountString = cursor.getString(cursor.getColumnIndex(Constants.tallyTableColumn_account));
                int i = accountString.indexOf(" ");
                accountName = accountString.substring(0,i);
                accountCode = accountString.substring(i + 1);
                newTally.setAccount(new Account(accountName,accountCode));

                tallyArrayList.add(newTally);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return tallyArrayList;
    }

    @Override
    public boolean addTally(Tally tally) {
        String actionStr = "insert into " + Constants.tallyTableName + " ("
                + Constants.tallyTableColumn_money + ", "
                + Constants.tallyTableColumn_account + ", "
                + Constants.tallyTableColumn_actionType + ", "
                + Constants.tallyTableColumn_date + ", "
                + Constants.tallyTableColumn_time + ", "
                + Constants.tallyTableColumn_c1 + ", "
                + Constants.tallyTableColumn_c2 + ", "
                + Constants.tallyTableColumn_project + ", "
                + Constants.tallyTableColumn_remark + ", "
                + Constants.tallyTableColumn_vendor + ", "
                + Constants.tallyTableColumn_member + ") "
                + "values(?,?,?,?,?,?,?,?,?,?,?)";

        sqLiteDatabase.execSQL(actionStr,new Object[]{tally.getMoney(),
                tally.getAccount().getAccountName() + " " + tally.getAccount().getAccountCode(),
                tally.getActionType().ordinal(),
                tally.getDate(),
                tally.getTime(),
                tally.getClassification1(),
                tally.getClassification2(),
                tally.getProject(),
                tally.getRemark(),
                tally.getVendor(),
                tally.getMember()});

        long strid;
        boolean ret = false;
        Cursor cursor = sqLiteDatabase.rawQuery("select last_insert_rowid() from "+ Constants.tallyTableName, null);
        if (cursor.moveToFirst()) {
            strid = cursor.getLong(0);
            tally.setId(strid);
            ret = true;
        }
        cursor.close();
        return ret;
    }

    @Override
    public boolean modifyTally(long id, Tally tally)  {
        String actionStr = "select * from " + Constants.tallyTableName + " "
                + "where id=" + id;
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            cursor.close();
            actionStr = "update " + Constants.tallyTableName + " set "
                    + Constants.tallyTableColumn_money + "=?, "
                    + Constants.tallyTableColumn_account + "=?, "
                    + Constants.tallyTableColumn_actionType + "=?, "
                    + Constants.tallyTableColumn_c1 + "=?, "
                    + Constants.tallyTableColumn_c2 + "=?, "
                    + Constants.tallyTableColumn_date + "=?, "
                    + Constants.tallyTableColumn_time + "=?, "
                    + Constants.tallyTableColumn_member + "=?, "
                    + Constants.tallyTableColumn_project +"=?, "
                    + Constants.tallyTableColumn_remark + "=?, "
                    + Constants.tallyTableColumn_vendor + "=? "
                    + "where id=" + id;

            sqLiteDatabase.execSQL(actionStr,new Object[]{tally.getMoney(),
                    tally.getAccount().getAccountName() + " " + tally.getAccount().getAccountCode(),
                    tally.getActionType().ordinal(),
                    tally.getClassification1(),
                    tally.getClassification2(),
                    tally.getDate(),
                    tally.getTime(),
                    tally.getMember(),
                    tally.getProject(),
                    tally.getRemark(),
                    tally.getVendor()});
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    @Override
    public boolean delTally(long id) {
        String actionStr = "select * from " + Constants.tallyTableName + " "
                + "where id=" + id;
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            cursor.close();
            actionStr = "delete from " + Constants.tallyTableName + " "
                    + "where id=" + id;
            sqLiteDatabase.execSQL(actionStr);
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    @Override
    public boolean delTally(Tally tally) {
        return delTally(tally.getId());
    }

    @Override
    public boolean saveConfig(Config config, Theme theme) {
        String sql = "select count(*) from "+Constants.configTableName;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        boolean ret;
        if(config != null){
            if(count == 0){
                ContentValues mContentValues = new ContentValues();
                mContentValues.put(Constants.configTableColumn_ct,(config.getCurrentTheme()).ordinal());
                mContentValues.put(Constants.configTableColumn_pw,config.getPassword());
                mContentValues.put(Constants.configTableColumn_pws,config.getPasswordStyle().ordinal());
                sqLiteDatabase.insert(Constants.configTableName,null,mContentValues);
                mContentValues.clear();
                ret = true;     //添加
            }else{
                cursor.close();
                sql = "select * from "+Constants.configTableName;
                cursor = sqLiteDatabase.rawQuery(sql, null);
                cursor.moveToFirst();
                ret = true;
                do{
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    int passwordStyle = cursor.getInt(cursor.getColumnIndex(Constants.configTableColumn_pws));
                    if(passwordStyle == config.getPasswordStyle().ordinal()){
                        String password = cursor.getString(cursor.getColumnIndex(Constants.configTableColumn_pw));
                        if(password.equals(config.getPassword())){
                            ret = false;        //覆盖
                        }else{
                            String actionStr = "update "+Constants.configTableName
                                    + " set " + Constants.configTableColumn_ct + "=" + config.getCurrentTheme().ordinal() + ", "
                                    + Constants.configTableColumn_pw + "=" + "'" + config.getPassword()+ "'" + " "
                                    + "where id="+id;
                            sqLiteDatabase.execSQL(actionStr);
                        }
                    }else{
                        String actionStr = "update "+Constants.configTableName
                                + " set " + Constants.configTableColumn_ct + "=" + config.getCurrentTheme().ordinal() + " "
                                + "where id="+id;
                        sqLiteDatabase.execSQL(actionStr);
                    }
                }while(cursor.moveToNext());
                if(ret){
                    ContentValues mContentValues = new ContentValues();
                    mContentValues.put(Constants.configTableColumn_ct,(config.getCurrentTheme()).ordinal());
                    mContentValues.put(Constants.configTableColumn_pw,config.getPassword());
                    mContentValues.put(Constants.configTableColumn_pws,config.getPasswordStyle().ordinal());
                    sqLiteDatabase.insert(Constants.configTableName,null,mContentValues);
                    mContentValues.clear();
                }
            }
        }else{
            if(count == 0){
                ret = false;     //添加
            }else{
                cursor.close();
                sql = "select * from "+Constants.configTableName;
                cursor = sqLiteDatabase.rawQuery(sql, null);
                cursor.moveToFirst();
                ret = true;
                do{
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String actionStr = "update "+Constants.configTableName
                            + " set " + Constants.configTableColumn_ct + "=" + theme.ordinal() + " "
                            + "where id="+id;
                    sqLiteDatabase.execSQL(actionStr);
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        return ret;
    }

    @Override
    public ArrayList<Config> getConfigs() {
        Config config;
        ArrayList<Config> configList = new ArrayList<>();
        String sql = "select count(*) from "+Constants.configTableName;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        if(count == 0) {
            return null;
        }
        sql = "select * from "+Constants.configTableName;
        cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        do{
            config = new Config();
            config.setPassword(cursor.getString(cursor.getColumnIndex(Constants.configTableColumn_pw)));
            config.setPasswordStyle(PasswordStyle.values()[cursor.getInt(cursor.getColumnIndex(Constants.configTableColumn_pws))]);
            config.setCurrentTheme(Theme.values()[cursor.getInt(cursor.getColumnIndex(Constants.configTableColumn_ct))]);
            configList.add(config);
        }while(cursor.moveToNext());
        cursor.close();
        return configList;
    }

    @Override
    public ArrayList<String> getAllClassification1(boolean ifActionType,ActionType actionType) {
        ArrayList<String> classification1List = new ArrayList<>();
        String actionStr;
        if(ifActionType){
            actionStr = "select * from " + Constants.classificationTableName + " "
                    + "where " + Constants.classificationColumn_actionType + "=" + actionType.ordinal() + " ";
//                    + "order by " + Constants.classificationColumn_c1 +  " asc";
        }else{
            actionStr = "select * from " + Constants.classificationTableName;
//                    + " order by " + Constants.classificationColumn_c1 +  " asc";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            do{
                String newClassification1 = cursor.getString(cursor.getColumnIndex(Constants.classificationColumn_c1));
                int i,len = classification1List.size();
                for(i = 0; i < len; i++){
                    if(classification1List.get(i).equals(newClassification1)){
                        break;
                    }
                }
                if(i == len){
                    classification1List.add(newClassification1);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return classification1List;
    }

    @Override
    public ArrayList<String> getClassification2(String classification1,boolean ifActionType,ActionType actionType) {
        ArrayList<String> classification2List = new ArrayList<>();
        String actionStr;
        if(ifActionType){
            actionStr = "select * from " + Constants.classificationTableName + " where "
                    + Constants.classificationColumn_c1 + "='" + classification1 + "' "
                    + "and " + Constants.classificationColumn_actionType + "=" + actionType.ordinal() + " ";
//                    + "order by " + Constants.classificationColumn_c2 +  " asc";
        }else{
            actionStr = "select * from " + Constants.classificationTableName + " where "
                    + Constants.classificationColumn_c1 + "='" + classification1 + "'";
//                    + " order by " + Constants.classificationColumn_c2 +  " asc";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            do{
                String newClassification2 = cursor.getString(cursor.getColumnIndex(Constants.classificationColumn_c2));
                int i,len = classification2List.size();
                for(i = 0; i < len; i++){
                    if(classification2List.get(i).equals(newClassification2)){
                        break;
                    }
                }
                if(i == len){
                    if(len > 0 && newClassification2.equals("无")){
                        classification2List.add(0,newClassification2);
                    }else{
                        classification2List.add(newClassification2);
                    }
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return classification2List;
    }

    @Override
    public boolean delClassification1(String classification1,ActionType actionType) {
        String actionStr = "select * from " + Constants.classificationTableName + " where "
                + Constants.classificationColumn_c1 + "='" + classification1 + "' "
                + "and " + Constants.classificationColumn_actionType + "=" + actionType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        boolean ret = true;
        if(!cursor.moveToFirst()){
            ret = false;
        }
        cursor.close();
        actionStr = "delete from " + Constants.classificationTableName + " where "
                + Constants.classificationColumn_c1 + "='" + classification1 + "' "
                + "and " + Constants.classificationColumn_actionType + "=" + actionType.ordinal();
        sqLiteDatabase.execSQL(actionStr);
        return ret;
    }

    @Override
    public boolean delClassification2(String classification1, String classification2,ActionType actionType) {
        String actionStr = "select * from " + Constants.classificationTableName + " where "
                + Constants.classificationColumn_c1 + "='" + classification1 + "' and "
                + Constants.classificationColumn_c2 + "='" + classification2 + "' and "
                + Constants.classificationColumn_actionType + "=" + actionType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        boolean ret = true;
        if(!cursor.moveToFirst()){
            ret = false;
        }
        cursor.close();
        actionStr = "delete from " + Constants.classificationTableName + " where "
                + Constants.classificationColumn_c1 + "='" + classification1 + "' and "
                + Constants.classificationColumn_c2 + "='" + classification2 + "' and "
                + Constants.classificationColumn_actionType + "=" + actionType.ordinal();
        sqLiteDatabase.execSQL(actionStr);
        return ret;
    }

    @Override
    public boolean addClassification(String classification1, String classification2,ActionType actionType) {
        boolean ret = true;
        String actionStr = "select * from " + Constants.classificationTableName + " where "
                + Constants.classificationColumn_c1 + "='" + classification1 + "' and "
                + Constants.classificationColumn_c2 + "='" + classification2 + "' and "
                + Constants.classificationColumn_actionType + "=" + actionType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            ret = false;
        }else{
            actionStr = "insert into " + Constants.classificationTableName + " ("
                    + Constants.classificationColumn_c1 + ", "
                    + Constants.classificationColumn_c2 + ", "
                    + Constants.classificationColumn_actionType + ") "
                    + "values(?,?,?)";
            sqLiteDatabase.execSQL(actionStr,new Object[]{classification1,classification2,actionType.ordinal()});
        }
        cursor.close();
        return ret;
    }

    @Override
    public ArrayList<String> getThirdParties(ThirdPartyType thirdPartyType) {
        String actionStr = "select * from " + Constants.ThirdPartyTableName + " where "
                + Constants.ThirdPartyColumn_tp + "=" + thirdPartyType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        ArrayList<String> thirdPartyList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String newString = cursor.getString(cursor.getColumnIndex(Constants.ThirdPartyColumn_ct));
                if(newString.equals("无") && thirdPartyList.size() > 0){
                    thirdPartyList.add(0,newString);
                }else{
                    thirdPartyList.add(newString);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        return thirdPartyList;
    }

    @Override
    public boolean delThirdParty(ThirdPartyType thirdPartyType, String field) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.ThirdPartyTableName + " where "
                + Constants.ThirdPartyColumn_tp + "=" + thirdPartyType.ordinal() + " and "
                + Constants.ThirdPartyColumn_ct + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,new String[]{field});
        if(cursor.moveToFirst()){
            ret = true;
            actionStr = "delete from " + Constants.ThirdPartyTableName + " where "
                    + Constants.ThirdPartyColumn_tp + "=" + thirdPartyType.ordinal() + " and "
                    + Constants.ThirdPartyColumn_ct + "=?";
            sqLiteDatabase.execSQL(actionStr,new String[]{field});
        }
        cursor.close();
        return ret;
    }

    @Override
    public boolean addThirdParty(ThirdPartyType thirdPartyType, String field) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.ThirdPartyTableName + " where "
                + Constants.ThirdPartyColumn_tp + "=" + thirdPartyType.ordinal() + " and "
                + Constants.ThirdPartyColumn_ct + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,new String[]{field});
        if(!cursor.moveToFirst()){
            ret = true;
            actionStr = "insert into " + Constants.ThirdPartyTableName + " ("
                    + Constants.ThirdPartyColumn_tp + ", "
                    + Constants.ThirdPartyColumn_ct + ") "
                    + "values(?,?)";
            sqLiteDatabase.execSQL(actionStr,new Object[]{thirdPartyType.ordinal(),field});
        }
        cursor.close();
        return ret;
    }

    @Override
    public ArrayList<Account> getAccounts() {
        String actionStr = "select * from " + Constants.AccountTableName;
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        ArrayList<Account> accountArrayList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Account account = new Account();
                account.setAccountName(cursor.getString(cursor.getColumnIndex(Constants.AccountColumn_an)));
                account.setAccountCode(cursor.getString(cursor.getColumnIndex(Constants.AccountColumn_ac)));
                accountArrayList.add(account);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return accountArrayList;
    }

    @Override
    public boolean delAccount(Account account) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.AccountTableName + " where "
                + Constants.AccountColumn_an + "=?" + " and "
                + Constants.AccountColumn_ac + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,new String[]{account.getAccountName(),account.getAccountCode()});
        if(cursor.moveToFirst()){
            ret = true;
            actionStr = "delete from " + Constants.AccountTableName + " where "
                    + Constants.AccountColumn_an + "=?" + " and "
                    + Constants.AccountColumn_ac + "=?";
            sqLiteDatabase.execSQL(actionStr,new String[]{account.getAccountName(),account.getAccountCode()});
        }
        cursor.close();
        return ret;
    }

    @Override
    public boolean addAccount(Account account) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.AccountTableName + " where "
                + Constants.AccountColumn_an + "=?" + " and "
                + Constants.AccountColumn_ac + "=?";
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,new String[]{account.getAccountName(),account.getAccountCode()});
        if(!cursor.moveToFirst()){
            ret = true;
            actionStr = "insert into " + Constants.AccountTableName + " ("
                    + Constants.AccountColumn_an + ", "
                    + Constants.AccountColumn_ac + ") "
                    + "values(?,?)";
            sqLiteDatabase.execSQL(actionStr,new Object[]{account.getAccountName(),account.getAccountCode()});
        }
        cursor.close();
        return ret;
    }
    @Override
    public String getIconInformation(String iconName, IconType iconType) {
        String actionStr = "select * from " + Constants.IconInformationTableName + " where "
                + Constants.IconInformationColumn_in + "='" + iconName  +"' and "
                + Constants.IconInformationColumn_it + "=" + iconType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        String iconCode = null;
        if(cursor.moveToFirst()){
            iconCode = cursor.getString(cursor.getColumnIndex(Constants.IconInformationColumn_ic));
        }
        cursor.close();
        return iconCode;
    }

    @Override
    public boolean addIconInformation(String iconName, IconType iconType, String iconCode) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.IconInformationTableName + " where "
                + Constants.IconInformationColumn_in + "='" + iconName + "' and "
                + Constants.IconInformationColumn_it + "=" + iconType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(!cursor.moveToFirst()){
            ret = true;
            actionStr = "insert into " + Constants.IconInformationTableName + " ("
                    + Constants.IconInformationColumn_in + ", "
                    + Constants.IconInformationColumn_it + ", "
                    + Constants.IconInformationColumn_ic + ") "
                    + "values(?,?,?)";
            sqLiteDatabase.execSQL(actionStr,new Object[]{iconName,iconType.ordinal(),iconCode});
        }
        cursor.close();
        return ret;
    }

    @Override
    public boolean delIconInformation(String iconName, IconType iconType) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.IconInformationTableName + " where "
                + Constants.IconInformationColumn_in + "='" + iconName + "' and "
                + Constants.IconInformationColumn_it + "=" + iconType.ordinal();
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            ret = true;
            actionStr = "delete from " + Constants.IconInformationTableName + " where "
                    + Constants.IconInformationColumn_in + "='" + iconName + "' and "
                    + Constants.IconInformationColumn_it + "=" + iconType.ordinal();
            sqLiteDatabase.execSQL(actionStr);
        }
        cursor.close();
        return ret;
    }

    @Override
    public boolean saveSymbol(String symbolName){
        boolean ret = false;
        String actionStr;
        if(!ifHaveSymbol(symbolName)){
            actionStr = "insert into " + Constants.SymbolTableName + " ("
                    + Constants.SymbolColumn_sn + ") "
                    + "values(?)";
            sqLiteDatabase.execSQL(actionStr,new Object[]{symbolName});
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean delSymbol(String symbolName){
        boolean ret = false;
        String actionStr;
        if(ifHaveSymbol(symbolName)){
            actionStr = "delete from " +  Constants.SymbolTableName + " where "
                    + Constants.SymbolColumn_sn + "='" + symbolName + "'";
            sqLiteDatabase.execSQL(actionStr);
            ret = true;
        }
        return ret;
    }


    @Override
    public boolean ifHaveSymbol(String symbolName) {
        boolean ret = false;
        String actionStr = "select * from " + Constants.SymbolTableName + " where "
                + Constants.SymbolColumn_sn + "='" +  symbolName  +"'";
        Cursor cursor = sqLiteDatabase.rawQuery(actionStr,null);
        if(cursor.moveToFirst()){
            ret = true;
        }
        cursor.close();
        return ret;
    }

    public void clearTable(String tableName){
        sqLiteDatabase.execSQL("delete from " + tableName);
    }

    @Override
    public boolean saveDataPackage(DataPackage dataPackage) {
        if(dataPackage == null) return false;
        clearTable(Constants.tallyTableName);
        clearTable(Constants.classificationTableName);
        clearTable(Constants.configTableName);
        clearTable(Constants.ThirdPartyTableName);
        clearTable(Constants.AccountTableName);
        clearTable(Constants.IconInformationTableName);
        clearTable(Constants.SymbolTableName);

        saveSymbol(Constants.symbol_ifSaveDataPackage);

        ArrayList<Config> configArrayList = dataPackage.getConfigs();
        for(Config config : configArrayList){
            saveConfig(config,null);
        }

        ArrayList<Tally> tallyArrayList = dataPackage.getAllTallies();
        for(Tally tally : tallyArrayList){
            addTally(tally);
            addAccount(tally.getAccount());
            addClassification(tally.getClassification1(),tally.getClassification2(),tally.getActionType());
            addThirdParty(ThirdPartyType.PROJECT,tally.getProject());
            addThirdParty(ThirdPartyType.MEMBER,tally.getMember());
            addThirdParty(ThirdPartyType.VENDOR,tally.getVendor());
        }
        return true;
    }
}
