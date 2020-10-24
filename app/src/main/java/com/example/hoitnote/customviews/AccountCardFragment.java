package com.example.hoitnote.customviews;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.BaseActivity;
import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentAccountcardBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.views.analysis.AnalysisActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountCardFragment extends Fragment {

    FragmentAccountcardBinding binding;
    AccountCardViewModel accountViewModel;
    private boolean respondToLongClick = false;

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface LongClickListener{
        void onLongClick(AccountCardFragment accountCardFragment, View v);
    }

    private LongClickListener longClickListener;

    public AccountCardFragment(){

    }

    public AccountCardFragment(AccountCardViewModel accountViewModel){
        this.accountViewModel = accountViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_accountcard,
                container,
                false
        );
        binding.setAccountCardViewModel(accountViewModel);
        binding.setAccountCardFragment(this);
        binding.accountCard.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                respondToLongClick = true;
                actionWhenCardClicked(view);
                return true;
            }
        });
        if(accountViewModel.isCard()){
            List<String> info = new ArrayList<>();
            info.add(accountViewModel.context.getString(R.string.account_remain_money)
                    + accountViewModel.getRemains());
            info.add(accountViewModel.context.getString(R.string.account_income)
                    + accountViewModel.getIncomes());
            info.add(accountViewModel.context.getString(R.string.account_outcome)
                    + accountViewModel.getOutcomes());
            binding.tipsTextView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        }
        return binding.getRoot();
    }

    public FragmentAccountcardBinding getBinding(){
        return this.binding;
    }

    /*当点击Fragment后应该执行的动作*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void actionWhenCardClicked(View v){
        if(!accountViewModel.isCard())
            return;
        switch (accountViewModel.getClickType()){
            case TAP:
                if(respondToLongClick){
                    this.longClickListener.onLongClick(this, v);
                    respondToLongClick = false;
                    return;
                }
                NavigationHelper.navigationWithParameter(
                        Constants.analysisParamTag,
                        accountViewModel.getAccount(),
                        accountViewModel.context,
                        AnalysisActivity.class,
                        false
                );
                ((BaseActivity)(accountViewModel.context)).overridePendingTransition(0, 0);
                break;
            case NONE:
                break;
        }

    }


}
