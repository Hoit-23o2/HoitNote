package com.example.hoitnote.views.flow;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.customviews.FontAwesome;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;


public class AddOptionActivity extends BaseActivity {
    private String iconType;
    private FontAwesome iconImage;
    private String iconCode;
    private String classname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_option);
        iconImage = findViewById(R.id.icon_image);
        iconCode = iconImage.getText().toString();
        iconType = getIntent().getStringExtra("icon_type");
        ActionBarInit();
        chooseIconButtonInit();
    }

    private void ActionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("新建" + iconType);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void chooseIconButtonInit(){
        TextView textName = findViewById(R.id.text_name);
        textName.setText(iconType+"名称");
        TextView textIcon = findViewById(R.id.text_icon);
        textIcon.setText("默认"+iconType+"图标");
        findViewById(R.id.choose_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddOptionActivity.this,ChooseIconActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Constants.ChooseIconResultCode) {
            iconCode = data.getStringExtra("icon_code");
            iconImage.setText(iconCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hzs_booking_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        classname = ((EditText)findViewById(R.id.option_name)).getText().toString();
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.hzs_booking_menu_save:
                switch (iconType){
                    case "二级分类":
                        Intent i = new Intent();
                        i.putExtra("id",getIntent().getIntExtra("id",0));
                        i.putExtra("icon_code", iconCode);
                        i.putExtra("icon_type", iconType);
                        i.putExtra("class_name",classname);
                        i.putExtra("action_type", getIntent().getIntExtra("action_type",0));
                        i.putExtra("classification1",getIntent().getStringExtra("classification1"));
                        setResult(Constants.AddOptionResultCode,i);
                        finish();
                        break;
                    case "一级分类":
                        Intent i1 = new Intent();
                        i1.putExtra("icon_code", iconCode);
                        i1.putExtra("icon_type", iconType);
                        i1.putExtra("class_name",classname);
                        i1.putExtra("action_type", getIntent().getIntExtra("action_type",0));
                        setResult(Constants.AddOptionResultCode,i1);
                        finish();
                        break;
                    case Constants.TypeStringProject:
                    case Constants.TypeStringMember:
                    case Constants.TypeStringVendor:
                        Intent i2 = new Intent();
                        i2.putExtra("icon_code", iconCode);
                        i2.putExtra("class_name",classname);
                        setResult(Constants.AddOptionResultCode,i2);
                        finish();
                        break;
                }
                break;

        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;

    }
}