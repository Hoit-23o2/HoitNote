package com.example.hoitnote.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.CustomviewHoitPasswordFrameBinding;

public class HoitPasswordFrame extends CardView {
    private int frameColor;
    private String passwordDisplayStyle;
    private boolean isMask;
    private CustomviewHoitPasswordFrameBinding binding;
    public HoitPasswordFrame(@NonNull Context context) {
        super(context);
    }


    public boolean getIsMask(){
        return this.isMask;
    }

    public void setIsMask(boolean isMask){
        this.isMask = isMask;
    }

    public HoitPasswordFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //从xml的属性中获取到字体颜色与string
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HoitPasswordFrame);
        frameColor = typedArray.getColor(R.styleable.HoitPasswordFrame_frameColor,
                Color.WHITE);
        passwordDisplayStyle = typedArray.getString(R.styleable
                .HoitPasswordFrame_passwordDisplayStyle);
        isMask = typedArray.getBoolean(R.styleable.HoitPasswordFrame_isMask, true);
        typedArray.recycle();

        //获取到控件
        //加载布局文件，与setContentView()效果一样
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.customview_hoit_password_frame,
                this,
                true
        );
    }

    public HoitPasswordFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showPassword(char text) {
        if(isMask){
            binding.passwordDisplay.setText(this.passwordDisplayStyle);
        }
        else {
            binding.passwordDisplay.setText(String.valueOf(text));
        }

    }

    public void hidePassword(){
        binding.passwordDisplay.setText("");
    }
}
