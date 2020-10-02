package com.example.hoitnote.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.CustomviewHoitPasswordFrameBinding;

public class HoitPasswordFrame extends CardView {
    private int frameColor;
    private String passwordDisplayStyle;
    private CustomviewHoitPasswordFrameBinding binding;
    public HoitPasswordFrame(@NonNull Context context) {
        super(context);
    }

    public HoitPasswordFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //从xml的属性中获取到字体颜色与string
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HoitPasswordFrame);
        frameColor = typedArray.getColor(R.styleable.HoitPasswordFrame_frameColor,
                Color.WHITE);
        passwordDisplayStyle = typedArray.getString(R.styleable
                .HoitPasswordFrame_passwordDisplayStyle);
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

    public void showPassword() {
        binding.passwordDisplay.setText(this.passwordDisplayStyle);
    }

    public void hidePassword(){
        binding.passwordDisplay.setText("");
    }
}
