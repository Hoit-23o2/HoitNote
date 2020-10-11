package com.example.hoitnote.models;

import com.example.hoitnote.utils.enums.ActionType;

import java.sql.Date;
import java.sql.Time;

public class Tally {

    private long id;
    private Double money;
    private Date date;
    private Time time;
    private String remark;
    private Account account;
    private ActionType actionType;

    private String classification1;
    private String classification2;
    private String member;
    private String project;
    private String vendor;

    public Tally(Date date, Time time, Double money, String classification1, String classification2, String remark, Account account, ActionType actionType, String member, String project, String vendor) {
        this.date = date;
        this.money = money;
        this.classification1 = classification1;
        this.classification2 = classification2;
        this.remark = remark;
        this.account = account;
        this.actionType = actionType;
        this.member = member;
        this.project = project;
        this.vendor = vendor;
        this.time = time;
    }

    public Tally(){

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getClassification1() {
        return classification1;
    }

    public void setClassification1(String classification1) {
        this.classification1 = classification1;
    }

    public String getClassification2() {
        return classification2;
    }

    public void setClassification2(String classification2) {
        this.classification2 = classification2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
