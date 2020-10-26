package com.example.hoitnote.views.tallyadd;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.adapters.tallies.HzsPagerAdapter;
import com.example.hoitnote.R;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.utils.helpers.DataBaseHelper;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.utils.helpers.ToastHelper;
import com.example.hoitnote.views.flow.HistoryActivity;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BookingActivity extends BaseActivity {
    private static final String TAG = "BookingActivity";
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;
    private ArrayList<String> tab_title_list = new ArrayList<>();
    private ArrayList<Fragment> fragment_list = new ArrayList<>();
    private Fragment outcomeFragment, incomeFragment, transferFragment;
    private HzsPagerAdapter adapter;
    private DetailOnPageChangeListener pageChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Account account = (Account)NavigationHelper.getNavigationParameter(this,Constants.mainParamTag);
        BookingDataHelper.setNowAccount(account);
        ActionBarInit();
        ViewPagerInit();
    }

    private void ViewPagerInit(){
        viewPager = findViewById(R.id.hzs_booking_viewpager);
        tabLayout = findViewById(R.id.hzs_booking_tabLayout);

        tab_title_list.add("支出");
        tab_title_list.add("收入");
        tab_title_list.add("转账");

        outcomeFragment = new BookingOutcomeFragment(BookingType.OUTCOME);
        incomeFragment = new BookingIncomeFragment(BookingType.INCOME);
        transferFragment = new BookingTransferFragment(BookingType.TRANSFER);

        fragment_list.add(outcomeFragment);
        fragment_list.add(incomeFragment);
        fragment_list.add(transferFragment);

        adapter = new HzsPagerAdapter(getSupportFragmentManager(),tab_title_list,fragment_list);
        viewPager.setAdapter(adapter);

        pageChangeListener = new DetailOnPageChangeListener();
        viewPager.setOnPageChangeListener(pageChangeListener);
        tabLayout.setViewPager(viewPager);
        tabLayout.setLayoutMode(SlidingTabLayout.LAYOUT_MODE_OPTICAL_BOUNDS);
    }
    private void ActionBarInit(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            //actionBar.setCustomView(R.layout.actionbar_booking);  //绑定自定义的布局：actionbar_layout.xml
            actionBar.setTitle("记一笔");
            actionBar.setDisplayHomeAsUpEnabled(true);

            /*ImageButton saveButton = (ImageButton)actionBar.getCustomView().findViewById(R.id.hzs_booking_save);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveTally();

                }
            });*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hzs_booking_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.hzs_booking_menu_save:
                saveTally();
                break;
        }
        return true;
    }

    public void saveTally(){
        if(pageChangeListener.getCurrentPage() == 0){
            Tally tally = ((BookingOutcomeFragment)outcomeFragment).getOutcomeTally();
            App.dataBaseHelper.addTally(tally);
        }else if(pageChangeListener.getCurrentPage() == 1){
            Tally tally = ((BookingIncomeFragment)incomeFragment).getIncomeTally();
            App.dataBaseHelper.addTally(tally);
        }
        else{
            ((BookingTransferFragment)transferFragment).saveTransferTally();
        }
        NavigationHelper.closeCurrentActivity(context);
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

    static class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }
}