package com.example.hoitnote.views.flow;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.tallies.HzsAddOptionRecyclerViewAdapter;
import com.example.hoitnote.adapters.tallies.HzsRecycleBinAdapter;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.commuications.DataBaseFilter;

import java.util.List;

public class RecycleBinActivity extends BaseActivity {
    private HzsRecycleBinAdapter adapter;
    private int hasOpened = 0;
    private int hasChosed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);
        actionBarInit();
        recyclerViewInit();
        buttonsInit();
    }
    private void actionBarInit(){

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("回收站");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void recyclerViewInit(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        List<Tally> deletedTallys = App.backupDataBaseHelper.getTallies(new DataBaseFilter(null,null,-1,null,null,null));
        adapter = new HzsRecycleBinAdapter(deletedTallys);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void buttonsInit(){
        TextView operateAll = findViewById(R.id.operate_all);
        TextView chooseAll = findViewById(R.id.choose_all);
        TextView restoreAll = findViewById(R.id.restore_all_chosed);
        TextView deleteAll = findViewById(R.id.delete_all_chosed);
        operateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasOpened == 0){
                    adapter.allOpen();
                }else{
                    adapter.allClose();
                }
                hasOpened = hasOpened^1;
            }
        });
        chooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasChosed == 0){
                    adapter.allOpenAndChoose();
                }else{
                    adapter.allOpenAndNotChoose();
                }
                hasChosed = hasChosed^1;
            }
        });
        restoreAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.restoreAllChosed();
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteAllChosed();
            }
        });
    }
}