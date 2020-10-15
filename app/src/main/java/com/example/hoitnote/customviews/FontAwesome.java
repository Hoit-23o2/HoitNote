package com.example.hoitnote.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.hoitnote.R;

public class FontAwesome extends androidx.appcompat.widget.AppCompatTextView {


    public FontAwesome(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FontAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontAwesome(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        //Font name should not contain "/".
        Typeface tf = ResourcesCompat.getFont(context, R.font.fontawesome5solid);
        setTypeface(tf);
    }

}