package com.example.hoitnote.models.flow;

import android.util.Log;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.enums.ActionType;

import java.util.ArrayList;
import java.util.List;

public class HzsYearData {
    private static final String TAG = "HzsYearData";
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<Tally> getData() {
        return data;
    }

    public void setData(List<Tally> data) {
        this.data = data;
    }

    public List<HzsMonthData> getMonthDataList() {
        return monthDataList;
    }

    public void setMonthDataList(List<HzsMonthData> monthDataList) {
        this.monthDataList = monthDataList;
    }

    public Double getBal() {
        return bal;
    }

    public Double getOut() {
        return out;
    }

    public Double getIn() {
        return in;
    }

    private String year;
    private String balance;
    private List<Tally> data = new ArrayList<>();
    private List<HzsMonthData> monthDataList = new ArrayList<>();
    private Double bal = 0.0;
    private Double out = 0.0;
    private Double in = 0.0;
    public HzsYearData(List<Tally> data){
        this.data = data;
        this.year = String.valueOf(data.get(0).getDate().getYear() + 1900);
        bal = 0.0;
        List<List<Tally>> tempData = new ArrayList<>();   //将相同月份的数据打包成MonthData
        int lastMonth = -1;
        List<Tally> tempList = new ArrayList<>();
        for(int i = 0;i < data.size();i++){
            if(data.get(i).getActionType() == ActionType.OUTCOME){
                bal -= data.get(i).getMoney();
                out += data.get(i).getMoney();
            }else{
                bal += data.get(i).getMoney();
                in += data.get(i).getMoney();
            }

            if(lastMonth == -1){
                tempList = new ArrayList<>();
                tempList.add(data.get(i));
                lastMonth = data.get(i).getDate().getMonth();
            }else if(data.get(i).getDate().getMonth() == lastMonth){
                tempList.add(data.get(i));
            }else{
                HzsMonthData hzsMonthData = new HzsMonthData(tempList);
                monthDataList.add(hzsMonthData);
                tempList = new ArrayList<>();
                tempList.add(data.get(i));
                Log.i(TAG, "HzsYearData: ");
                lastMonth = data.get(i).getDate().getMonth();
            }
        }
        HzsMonthData hzsMonthData = new HzsMonthData(tempList);
        monthDataList.add(hzsMonthData);
        this.balance = bal.toString();

    }
}
