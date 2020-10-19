package com.example.hoitnote.views.flow;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.tallies.HzsChooseIconRecyclerViewAdapter;
import com.example.hoitnote.utils.constants.Constants;

import java.util.List;

public class ChooseIconActivity extends BaseActivity {
    private static final String TAG = "ChooseIconActivity";
    private List<String> iconTextList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon);
        ActionBarInit();
        initIconIds();
        showAllIcon();
    }
    private void ActionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择图标");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void initIconIds(){
        iconTextList = Constants.IconFonts;
    }

    private void showAllIcon(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        HzsChooseIconRecyclerViewAdapter adapter = new HzsChooseIconRecyclerViewAdapter(iconTextList,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}