package com.example.hoitnote.views.flow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.tallies.HzsAddOptionRecyclerViewAdapter;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.enums.ThirdPartyType;

import java.util.ArrayList;
import java.util.List;

public class ManageOptionActivity extends BaseActivity {
    /*
    * Activity for Member, Store, Project
    * */
    private IconType iconType;
    private String typeString;
    private String newClassIconCode;
    private String newClassName;
    private ThirdPartyType thirdPartyType;
    private HzsAddOptionRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_option);
        actionBarInit();
        optionTypeInit();
        recyclerViewInit();
    }

    private void actionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("管理分类");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void recyclerViewInit(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new HzsAddOptionRecyclerViewAdapter(iconType,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private void optionTypeInit(){
        iconType = IconType.values()[getIntent().getIntExtra("icon_type",0)];
        switch (iconType){
            case PROJECT:
                thirdPartyType = ThirdPartyType.PROJECT;
                this.typeString = Constants.TypeStringProject;
                break;
            case MEMBER:
                thirdPartyType = ThirdPartyType.MEMBER;
                this.typeString = Constants.TypeStringMember;
                break;
            case VENDOR:
                thirdPartyType = ThirdPartyType.VENDOR;
                this.typeString = Constants.TypeStringVendor;
                break;
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
                intent.putExtra("icon_type",typeString);
                ((ManageOptionActivity)context).startActivityForResult(intent,1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Constants.AddOptionResultCode){
            newClassIconCode = data.getStringExtra("icon_code");
            newClassName = data.getStringExtra("class_name");
            App.dataBaseHelper.addThirdParty(thirdPartyType,newClassName);
            App.dataBaseHelper.addIconInformation(newClassName,iconType,newClassIconCode);
            adapter.getOptionList().add(newClassName);
            adapter.notifyDataSetChanged();
        }
    }
}