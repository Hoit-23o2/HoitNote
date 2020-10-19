package com.example.hoitnote.models.flow;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.enums.ActionType;

import java.util.List;

public class HzsMonthData {
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public List<Tally> getData() {
        return data;
    }

    public void setData(List<Tally> data) {
        this.data = data;
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
    private String month;
    private String balance;
    private String income;
    private String outcome;
    private List<Tally> data;
    private Double bal = 0.0;
    private Double out = 0.0;
    private Double in = 0.0;
    private HzsYearData parent;
    public HzsMonthData(List<Tally> data, HzsYearData parent){
        this.data = data;
        this.year = String.valueOf(data.get(0).getDate().getYear()+1900);
        this.month = String.valueOf(data.get(0).getDate().getMonth()+1);

        refreshData();
        this.balance = bal.toString();
        this.income = in.toString();
        this.outcome = out.toString();
        this.parent = parent;
    }
    public void refreshData(){
        bal = 0.0;
        out = 0.0;
        in = 0.0;
        for(int i = 0;i < data.size();i++){

            if(data.get(i).getActionType() == ActionType.INCOME){
                bal += data.get(i).getMoney();
                in += data.get(i).getMoney();
            }else if(data.get(i).getActionType() == ActionType.OUTCOME){
                bal -= data.get(i).getMoney();
                out += data.get(i).getMoney();
            }
        }
        this.balance = bal.toString();
        this.income = in.toString();
        this.outcome = out.toString();
        if(parent != null){
            parent.refreshData();
        }
    }
}
