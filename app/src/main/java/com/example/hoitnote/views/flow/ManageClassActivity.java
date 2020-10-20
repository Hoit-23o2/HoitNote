package com.example.hoitnote.views.flow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.tallies.HzsAddClassExpandableListViewAdapter;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;

import java.util.Arrays;

public class ManageClassActivity extends BaseActivity {
    private String newClassName;
    private String newClassIconCode;
    private HzsAddClassExpandableListViewAdapter adapter;
    private IconType class1Type;
    private IconType class2Type;
    private BookingType bookingType;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        initTypes();
        int classSelectOption1 = getIntent().getIntExtra("classification1", 0);
        adapter = new HzsAddClassExpandableListViewAdapter(bookingType,this);
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.hzs_add_class_expandable_list_view);
        listView.setAdapter(adapter);
        listView.expandGroup(classSelectOption1);
        ActionBarInit();
    }
    private void initTypes(){
        bookingType = BookingType.values()[getIntent().getIntExtra("booking_type", 0)];
        switch (bookingType){
            case OUTCOME:
                class1Type = IconType.OUTCOMECLASS1;
                class2Type = IconType.OUTCOMECLASS2;
                break;
            case INCOME:
                class1Type = IconType.INCOMECLASS1;
                class2Type = IconType.INCOMECLASS2;
                break;
        }
    }
    private void ActionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("管理分类");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hzs_manage_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            case R.id.hzs_menu_save:
                finish();
                break;
            case R.id.hzs_menu_add:
                Intent intent = new Intent(context, AddOptionActivity.class);
                intent.putExtra("icon_type","一级分类");
                intent.putExtra("action_type",bookingType.ordinal());
                ((ManageClassActivity)context).startActivityForResult(intent,2);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Constants.AddOptionResultCode) {  //增加二级分类
            newClassIconCode = data.getStringExtra("icon_code");
            newClassName = data.getStringExtra("class_name");
            String classification1 = data.getStringExtra("classification1");
            ActionType actionType = ActionType.values()[data.getIntExtra("action_type",0)];
            App.dataBaseHelper.addClassification(classification1,newClassName,actionType);
            App.dataBaseHelper.addIconInformation(classification1+newClassName, class2Type,newClassIconCode);
            int id = data.getIntExtra("id",0);
            adapter.classifications2.get(id).add(adapter.classifications2.get(id).size()-1,newClassName);
            //adapter.classifications2.addAll(BookingDataHelper.getClassifications2(BookingType.values()[actionType.ordinal()]));
            adapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == Constants.AddOptionResultCode) {   //增加一级分类
            newClassIconCode = data.getStringExtra("icon_code");
            newClassName = data.getStringExtra("class_name");
            ActionType actionType = ActionType.values()[data.getIntExtra("action_type",0)];
            BookingDataHelper.addClassification1(newClassName,actionType);
            App.dataBaseHelper.addIconInformation(newClassName, class1Type,newClassIconCode);
            App.dataBaseHelper.addIconInformation(newClassName+"无", class2Type,newClassIconCode);
            adapter.classifications1.add(newClassName);
            adapter.classifications2.add(Arrays.asList("无","添加二级分类"));
            adapter.notifyDataSetChanged();
        }
    }
}
