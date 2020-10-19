package com.example.hoitnote.views.analysis;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hoitnote.R;
import com.example.hoitnote.adapters.analysis.AnalysisFragmentAdapter;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.databinding.FragmentInformationBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.viewmodels.AccountCardViewModel;

public class InformationFragment extends Fragment {
    FragmentInformationBinding binding;
    AccountCardViewModel accountCardViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public InformationFragment(AccountCardViewModel accountCardViewModel, FragmentManager
                               fragmentManager, FragmentTransaction fragmentTransaction){
        this.accountCardViewModel = accountCardViewModel;
        this.fragmentManager = fragmentManager;
        this.fragmentTransaction = fragmentManager.beginTransaction();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animFrame();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_information,
                container,
                false
        );

        binding.setAccountCardViewModel(accountCardViewModel);
        binding.informationContainer.setAlpha(0);
        /*长按这个Card*/
        AccountCardFragment accountCardFragment = accountCardViewModel.getAccount().
                parseToAccountCardFragment(accountCardViewModel.context, accountCardViewModel.getClickType());
        fragmentTransaction.add(binding.cardContainer.getId(), accountCardFragment).commit();
        return binding.getRoot();
    }

    private void animFrame(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.cardContainer.setPivotX(150);
                binding.cardContainer.setPivotY(150);
                binding.cardContainer.animate().rotation(90).start();
            }
        }, Constants.delayDuration / 2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.informationContainer.animate().alpha(1).start();
            }
        }, Constants.delayDuration);
    }
}
