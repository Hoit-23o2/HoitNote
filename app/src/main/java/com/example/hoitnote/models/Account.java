package com.example.hoitnote.models;

public class Account {
    private String accountName;
    private String accountCode;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Account(){

    }

    public Account(String accountName, String accountCode) {
        this.accountName = accountName;
        this.accountCode = accountCode;
    }
}
