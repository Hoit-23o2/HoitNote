package com.example.blueteethtest.ObejctToSend;

import java.io.Serializable;

/*
* 配置模型
* 1.当前的主题
* 2.当前的密码
* 3.当前的密码样式
* */
public class Config implements Serializable {
    private Theme currentTheme;

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme currentTheme) {
        this.currentTheme = currentTheme;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private PasswordStyle passwordStyle;

    public PasswordStyle getPasswordStyle() {
        return passwordStyle;
    }

    public void setPasswordStyle(PasswordStyle passwordStyle) {
        this.passwordStyle = passwordStyle;
    }

    public Config(Theme currentTheme, String password, PasswordStyle passwordStyle) {
        this.currentTheme = currentTheme;
        this.password = password;
        this.passwordStyle = passwordStyle;
    }

    public Config(){

    }
}
