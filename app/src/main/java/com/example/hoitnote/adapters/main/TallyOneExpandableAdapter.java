package com.example.hoitnote.adapters.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.ItemTallyBinding;
import com.example.hoitnote.databinding.ItemTallyGroupBinding;
import com.example.hoitnote.databinding.PopupwindowTallyInfoBinding;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.models.flow.HzsTally;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.helpers.DeviceHelper;
import com.example.hoitnote.utils.helpers.DialogHelper;
import com.example.hoitnote.utils.helpers.ThemeHelper;
import com.example.hoitnote.viewmodels.TallyViewModel;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class TallyOneExpandableAdapter extends BaseExpandableListAdapter {
    ItemTallyBinding binding;
    ItemTallyGroupBinding groupBinding;
    TreeMap<String, ArrayList<TallyViewModel>> talliesWithGroup;
    ArrayList<String> groupsTitle;
    Context context;


    @Override
    public int getGroupCount() {
        return groupsTitle.size();
    }

    @Override
    public int getChildrenCount(int groupId) {
        ArrayList<TallyViewModel> innerList =
                this.talliesWithGroup.get(this.groupsTitle.get(groupId));
        int count = 0;
        if(innerList != null){
            count = innerList.size();
        }
        return count;
    }

    @Override
    public Object getGroup(int groupId) {
        return this.groupsTitle.get(groupId);
    }

    @Override
    public Object getChild(int groupId, int childId) {
        ArrayList<TallyViewModel> innerList =
                this.talliesWithGroup.get(this.groupsTitle.get(groupId));
        TallyViewModel tallyViewModel = null;
        if(innerList != null){
            tallyViewModel = innerList.get(childId);
        }
        return tallyViewModel;
    }

    @Override
    public long getGroupId(int groupId) {
        return groupId;
    }

    @Override
    public long getChildId(int groupId, int childId) {
        return childId;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupId, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally_group,null);
            groupBinding = DataBindingUtil.bind(convertView);
            convertView.setTag(groupBinding);
        }
        else{
            groupBinding = (ItemTallyGroupBinding) convertView.getTag();
        }
        String groupIdStr = this.groupsTitle.get(groupId);
        groupBinding.groupTitle.setText(groupIdStr);
        ArrayList<TallyViewModel> tallyViewModels = talliesWithGroup.get(groupIdStr);
        if(tallyViewModels != null){
            int remains;
            int incomes = 0;
            int outcomes = 0;
            for (TallyViewModel tallyViewModel:
                 tallyViewModels) {
                Tally tally = tallyViewModel.getTally();
                if(tally.getActionType() == ActionType.INCOME)
                    incomes += tally.getMoney();
                else if(tally.getActionType() == ActionType.OUTCOME)
                    outcomes += tally.getMoney();
            }
            remains = incomes - outcomes;
            List<String> info = new ArrayList<>();
            info.add(context.getString(R.string.account_remain_money) + remains);
            info.add(context.getString(R.string.account_income) + incomes);
            info.add(context.getString(R.string.account_outcome) + outcomes);
            /*在代码里设置自己的动画*/
            groupBinding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);


        }


        /*设置展开图标*/
        if (isExpanded) {
            groupBinding.expandableArrow.setText(Constants.IconDownArrow);
        } else {
            groupBinding.expandableArrow.setText(Constants.IconUpArrow);
        }

        return groupBinding.getRoot();
    }

    @Override
    public View getChildView(int groupId, int childId, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tally,null);
            binding = DataBindingUtil.bind(convertView);
            convertView.setTag(binding);
        }
        else{
            binding = (ItemTallyBinding) convertView.getTag();
        }

        assert binding != null;
        ArrayList<TallyViewModel> group = talliesWithGroup.get(this.groupsTitle.get(groupId));
        if(group != null){
            binding.setTallyViewModel(group.get(childId));
        }
        PopupwindowTallyInfoBinding dialogNormalBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.popupwindow_tally_info,
                        null,
                        false
                );
        dialogNormalBinding.setTally(new HzsTally(group.get(childId).getTally(), 0));
        final AlertDialog alertDialog =
                DialogHelper.buildDialog(context, dialogNormalBinding);
        dialogNormalBinding.hzsExpandItemTallyIcon.setBackgroundColor(ThemeHelper.generateColor(context));

        int height = DeviceHelper.getDeviceHeight(context);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) dialogNormalBinding.mainContainer.getLayoutParams();
        layoutParams.height = (int) (height * 0.7);
        dialogNormalBinding.mainContainer.setLayoutParams(layoutParams);

        binding.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public TallyOneExpandableAdapter(Context context,
                                     TreeMap<String, ArrayList<TallyViewModel>> talliesWithGroup){
        this.context = context;
        this.talliesWithGroup = talliesWithGroup;
        this.groupsTitle = new ArrayList<>(talliesWithGroup.keySet());
    }

    public void expandAllGroup(ExpandableListView expandableListView){
        for (int groupId = 0; groupId < getGroupCount(); groupId++) {
            expandableListView.expandGroup(groupId);
        }
    }


    


}
