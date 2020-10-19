package com.example.hoitnote.models.flow;

import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.enums.ActionType;

import java.text.SimpleDateFormat;
import java.util.List;

public class HzsDayData {
    public List<Tally> getData() {
        return data;
    }

    public void setData(List<Tally> data) {
        this.data = data;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
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

    public Double getBal() {
        return bal;
    }

    public Double getOut() {
        return out;
    }

    public Double getIn() {
        return in;
    }

    private List<Tally> data;
    private String day;
    private String weekday;
    private String balance;
    private String income;
    private String outcome;
    private Double bal = 0.0;
    private Double out = 0.0;
    private Double in = 0.0;
    public HzsDayData(List<Tally> data){
        this.data = data;
        this.day = String.valueOf(data.get(0).getDate().getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("E");
        this.weekday = formatter.format(data.get(0).getDate().getDate());

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
    }
}
