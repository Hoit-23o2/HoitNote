package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.hoitnote.BR;
import com.example.hoitnote.R;
import com.example.hoitnote.customviews.FontAwesome;
import com.example.hoitnote.models.Tally;

public class TallyViewModel extends BaseViewModel {
    /*
    * 账单
    * */
    private Tally tally;

    @Bindable
    public Tally getTally() {
        return tally;
    }


    public void setTally(Tally tally) {
        this.tally = tally;
        notifyPropertyChanged(BR.tally);
    }
    /*
    * 类型：收入支出的颜色
    * */
    public int typeBackgroundColor;
    @Bindable
    public int getTypeBackgroundColor() {
        return typeBackgroundColor;
    }

    @BindingAdapter("typeBackgroundColor")
    public static void setTypeBackgroundColor(TextView textView, int typeBackgroundColor) {
        textView.setBackgroundColor(typeBackgroundColor);
    }

    /*
    * Icon的颜色
    * */
    private int iconBackgroundColor;

    @Bindable
    public int getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    @BindingAdapter("iconBackgroundColor")
    public static void setIconBackgroundColor(FontAwesome fontAwesome, int backgroundColor) {
        fontAwesome.setBackgroundColor(backgroundColor);
    }

    /*
    * Icon
    * */
    private String iconUrl;

    @Bindable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        notifyPropertyChanged(BR.iconUrl);
    }

    /*
    * Constructor
    * */
    public TallyViewModel(Context context){
        super(context);
    }

    public TallyViewModel(Context context, Tally tally, String iconUrl,
                          int iconBackgroundColor, int typeBackgroundColor) {
        super(context);
        this.tally = tally;
        this.iconUrl = iconUrl;
        this.iconBackgroundColor = iconBackgroundColor;
        this.typeBackgroundColor = typeBackgroundColor;
    }


}
