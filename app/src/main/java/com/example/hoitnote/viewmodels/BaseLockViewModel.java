package com.example.hoitnote.viewmodels;

import android.content.Context;

import androidx.databinding.Bindable;

import com.example.hoitnote.BR;
import com.example.hoitnote.utils.enums.LockViewType;

public class BaseLockViewModel extends BaseViewModel {
    /*
     * 页面类型
     * */

    private LockViewType lockViewType;
    @Bindable
    public LockViewType getLockViewType() {
        return lockViewType;
    }

    public void setLockViewType(LockViewType lockViewType) {
        this.lockViewType = lockViewType;
        notifyPropertyChanged(BR.lockViewType);
    }
    /*
    * 密码
    * */
    private String password;

    @Bindable
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
    /*
     * 标题
     * */
    private String title;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    /*
     * 按钮字符
     * */
    private String btnText;


    @Bindable
    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
        notifyPropertyChanged(BR.btnText);
    }

    public BaseLockViewModel(Context context){
        super(context);
    }

    public BaseLockViewModel(Context context,
                             LockViewType lockViewType, String title, String btnText) {
        super(context);
        this.lockViewType = lockViewType;
        this.title = title;
        this.btnText = btnText;
    }


}
