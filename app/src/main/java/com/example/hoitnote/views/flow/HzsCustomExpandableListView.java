package com.example.hoitnote.views.flow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class HzsCustomExpandableListView extends ExpandableListView {
    public HzsCustomExpandableListView(Context context) {
        super(context);
    }

    public HzsCustomExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HzsCustomExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 解决显示不全的问题
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
