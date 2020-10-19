package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.hoitnote.BR;
import com.example.hoitnote.R;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.views.analysis.AnalysisActivity;

import java.io.Serializable;

public class AccountCardViewModel extends BaseViewModel{
    private Account account;
    private String backgroundUrl;
    private String incomes;
    private String outcomes;
    private String transfer;
    private boolean isCard;
    private ClickType clickType;

    public AccountCardViewModel(Context context){
        super(context);
    }

    /*
    * @params
    * clickType:该Card是否可点击
    * isCard:该Card是否为Card，（可能是加号）
    * */
    public AccountCardViewModel(Account account, String backgroundUrl,
                                String incomes, String outcomes,
                                String transfer, boolean isCard,
                                Context context, ClickType clickType) {
        super(context);
        this.account = account;
        this.backgroundUrl = backgroundUrl;
        this.incomes = incomes;
        this.outcomes = outcomes;
        this.transfer = transfer;
        this.isCard = isCard;
        this.clickType = clickType;
    }

    @Bindable
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    @BindingAdapter("backgroundUrl")//类似Converter
    public void setBackgroundUrl(ImageView imageView, String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
        Glide.with(imageView.getContext()).load(backgroundUrl).error(R.drawable.ic_launcher_foreground).into(imageView);
        notifyPropertyChanged(BR.backgroundUrl);
    }

    @Bindable
    public String getIncomes() {
        return incomes;
    }

    public void setIncomes(String incomes) {
        this.incomes = incomes;
        notifyPropertyChanged(BR.incomes);
    }

    @Bindable
    public String getOutcomes() {
        return outcomes;
    }


    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
        notifyPropertyChanged(BR.outcomes);
    }

    @Bindable
    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
        notifyPropertyChanged(BR.transfer);
    }

    @Bindable
    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
        notifyPropertyChanged(BR.card);
    }

    public ClickType getClickType() {
        return clickType;
    }
}
