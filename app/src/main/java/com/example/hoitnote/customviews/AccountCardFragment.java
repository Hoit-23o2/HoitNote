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

public class AccountCardFragment extends Fragment {

    FragmentAccountcardBinding binding;
    Account account;

    public AccountCardFragment(Account account){
        account = account;
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
        binding.setAccount(account);

        return binding.getRoot();
    }
}
