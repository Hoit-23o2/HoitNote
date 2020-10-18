package com.example.hoitnote.viewmodels;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.hoitnote.BR;
import com.example.hoitnote.R;
import com.example.hoitnote.models.Tally;

public class TallyViewModel extends BaseViewModel {
    private Tally tally;

    @Bindable
    public Tally getTally() {
        return tally;
    }


    public void setTally(Tally tally) {
        this.tally = tally;
        notifyPropertyChanged(BR.tally);
    }
    /*类型的icon*/
    private String iconUrl;


    @Bindable
    public String getIconUrl() {
        return iconUrl;
    }

    @BindingAdapter("iconUrl")
    public static void setIconUrl(ImageView imageView, String iconUrl) {
        Glide.with(imageView.getContext()).load(iconUrl).error(R.drawable.ic_launcher_foreground).into(imageView);
    }

    public TallyViewModel(Context context){
        super(context);
    }

    public TallyViewModel(Context context, Tally tally, String iconUrl) {
        super(context);
        this.tally = tally;
        this.iconUrl = iconUrl;
    }
}
