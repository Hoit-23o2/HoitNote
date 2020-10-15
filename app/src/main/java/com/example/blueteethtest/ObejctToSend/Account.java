package com.example.blueteethtest.ObejctToSend;



import java.io.Serializable;

public class Account implements Serializable {
    private String accountName;
    private String accountCode;

    public Account(String accountName, String accountCode) {
        this.accountName = accountName;
        this.accountCode = accountCode;
    }

    public Account(){

    }

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


}
