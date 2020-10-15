package com.example.hoitnote.customviews;

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
import com.example.hoitnote.viewmodels.AccountCardViewModel;

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

        return binding.getRoot();
    }

    public FragmentAccountcardBinding getBinding(){
        return this.binding;
    }


}
