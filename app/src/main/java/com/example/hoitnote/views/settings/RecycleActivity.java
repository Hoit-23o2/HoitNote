package com.example.hoitnote.views.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.main.TallyListAdapter;
import com.example.hoitnote.databinding.ActivityRecycleBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.viewmodels.RecycleViewModel;
import com.example.hoitnote.viewmodels.TallyViewModel;

import java.util.ArrayList;

public class RecycleActivity extends BaseActivity {

    ActivityRecycleBinding binding;
    TallyListAdapter tallyListAdapter;
    RecycleViewModel recycleViewModel;
    ArrayList<TallyViewModel> tallyViewModels;
    ViewGroup decorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_recycle);
        recycleViewModel = new RecycleViewModel(context);

        decorView = (ViewGroup) this.getWindow().getDecorView();
        /*暂时测试所有账单*/
        ArrayList<Tally> backupTallies = App.backupDataBaseHelper.getTallies(null);
        tallyViewModels = recycleViewModel.parseTalliesToViewModel(backupTallies);
        tallyListAdapter = new TallyListAdapter(context, tallyViewModels);
        binding.recycleListView.setAdapter(tallyListAdapter);
        initActivity();

    }
    private boolean isSelectedAll = false;
    private void initActivity() {
        showBackButton();
        binding.recycleListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        binding.recycleListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int pos, long id, boolean checked) {
                final int checkedCount = binding.recycleListView.getCheckedItemCount();
                actionMode.setTitle(checkedCount + " 已选择");
                tallyListAdapter.toggleSelection(pos);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.recycle_choice_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                /*添加close按钮*/
                decorView.postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                        int buttonId = getResources().getIdentifier("action_mode_close_button", "id", "android");

                        ImageView v = decorView.findViewById(buttonId);
                        if (v == null)
                        {
                            buttonId = R.id.action_mode_close_button;
                            v = decorView.findViewById(buttonId);
                        }

                        if (v != null)
                        {
                            v.setCropToPadding(true);
                            v.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            v.setImageResource(R.drawable.ic_baseline_close_24);
                            v.setScaleX(0.6f);
                            v.setScaleY(0.6f);
                            v.setBackground(null);
                        }
                    }
                }, Constants.delayDuration / 10);
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                /*Calls getSelectedIds method from ListViewAdapter Class*/
                SparseBooleanArray selected = tallyListAdapter
                        .getSelectedIds();
                switch (menuItem.getItemId()) {
                    /*全选/取消全选*/
                    case R.id.recycle_all:
                        int count = tallyListAdapter.getCount();
                        SparseBooleanArray items = binding.recycleListView.getCheckedItemPositions();
                        if(!isSelectedAll){
                            for(int pos = 0; pos < count; pos++){
                                if(!items.get(pos))
                                    binding.recycleListView.setItemChecked(pos, true);
                            }
                            isSelectedAll = true;
                            menuItem.setTitle(getString(R.string.recycle_unselect_all));
                        }
                        else{
                            for(int pos = 0; pos < count; pos++){
                                if(pos != 0)
                                    binding.recycleListView.setItemChecked(pos, false);
                            }
                            menuItem.setTitle(getString(R.string.recycle_select_all));
                            isSelectedAll = false;
                        }
                        /*更新菜单*/
                        invalidateOptionsMenu();
                        return false;
                    /*删除*/
                    case R.id.recycle_delete:
                        /*Captures all selected ids with a loop*/
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                               /* Remove selected items following the ids*/
                                TallyViewModel tallyViewModel = (TallyViewModel)
                                        tallyListAdapter.getItem(selected.keyAt(i));
                                tallyListAdapter.remove(selected.keyAt(i));
                                App.backupDataBaseHelper.delTally(tallyViewModel.getTally());
                            }
                        }
                        /*Close CAB*/
                        actionMode.finish();
                        return true;
                    /*恢复*/
                    case R.id.recycle_recover:
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                TallyViewModel tallyViewModel = (TallyViewModel)
                                        tallyListAdapter.getItem(selected.keyAt(i));
                                tallyListAdapter.remove(selected.keyAt(i));
                                App.backupDataBaseHelper.delTally(tallyViewModel.getTally());
                                App.dataBaseHelper.addTally(tallyViewModel.getTally());
                                App.dataBaseHelper.addAccount(tallyViewModel.getTally().getAccount());
                            }
                        }
                        /*Close CAB*/
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                tallyListAdapter.removeSelection();
            }
        });
    }
}