package com.example.hoitnote.customviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.hoitnote.R;
import com.example.hoitnote.databinding.FragmentAccountcardBinding;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.helpers.NavigationHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;
import com.example.hoitnote.views.analysis.AnalysisActivity;

public class AccountCardFragment extends Fragment {

    FragmentAccountcardBinding binding;
    AccountCardViewModel accountViewModel;

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
        return binding.getRoot();
    }

    public FragmentAccountcardBinding getBinding(){
        return this.binding;
    }

    /*当点击Fragment后应该执行的动作*/
    public void actionWhenCardClicked(View v){
        switch (accountViewModel.getClickType()){
            case TAP:
                NavigationHelper.navigationWithParameter(
                        Constants.analysisParamTag,
                        accountViewModel.getAccount(),
                        accountViewModel.context,
                        AnalysisActivity.class,
                        false
                );
                break;
            case NONE:
                break;
            case LONG_TAP:
                break;
            case DOUBLE_TAP:
                break;
        }

    }

}
