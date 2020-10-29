package com.example.hoitnote.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DraggableHand extends LinearLayout {

    public interface DraggableHandListener
    {
        void onTouchDown(float x, float y);
        void onTouchUp(float x, float y);
        void onTouchMove(float x, float y);
    }

    public void setDraggableHandListener(DraggableHandListener draggableHandListener) {
        this.draggableHandListener = draggableHandListener;
    }

    private DraggableHandListener draggableHandListener;

    public DraggableHand(Context context) {
        super(context);
    }

    public DraggableHand(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableHand(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                draggableHandListener.onTouchDown(event.getRawX(), event.getRawY());
                return true;
            case MotionEvent.ACTION_MOVE:
                draggableHandListener.onTouchMove(event.getRawX(), event.getRawY());
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                draggableHandListener.onTouchUp(event.getRawX(), event.getRawY());
                return true;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
