package com.example.hoitnote.views.analysis;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.RelativeGuide;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.analysis.AnalysisFragmentAdapter;
import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.databinding.FragmentInformationBinding;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ClickType;
import com.example.hoitnote.utils.helpers.DeviceHelper;
import com.example.hoitnote.viewmodels.AccountCardViewModel;

public class InformationFragment extends Fragment{
    FragmentInformationBinding binding;
    AccountCardViewModel accountCardViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FragmentInformationBinding getBinding() {
        return binding;
    }

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

        /*初始化1：1Container*/
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                binding.cardRelatedContainer.getLayoutParams();
        layoutParams.verticalBias = 0;
        binding.cardRelatedContainer.setLayoutParams(layoutParams);

        /*长按这个Card*/
        AccountCardFragment accountCardFragment = accountCardViewModel.getAccount().
                parseToAccountCardFragment(accountCardViewModel.context, accountCardViewModel.getClickType());
        fragmentTransaction.add(binding.cardContainer.getId(), accountCardFragment).commit();
        return binding.getRoot();
    }

    /*设置进入动画*/
    private void animFrame(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int deviceHeight = DeviceHelper.getDeviceHeight(accountCardViewModel.context);
                int deviceWidth = DeviceHelper.getDeviceWidth(accountCardViewModel.context);
                int actionBarHeight = DeviceHelper.getActionBarHeight(accountCardViewModel.context);
                int statueBarHeight = DeviceHelper.getStatueBarHeight(accountCardViewModel.context);
                int navigationKeyHeight = DeviceHelper.getNavigationKeyHeight(accountCardViewModel.context);
                int screenHeight = deviceHeight - actionBarHeight - statueBarHeight - navigationKeyHeight;
                int transitionY = (screenHeight - deviceWidth) / 2;
                binding.cardRelatedContainer.animate().translationY(transitionY).start();
            }
        }, Constants.delayDuration / 4);


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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NewbieGuide.with((Fragment)InformationFragment.this)
                        .setLabel("guide_information")
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(binding.informationContainer, new RelativeGuide(R.layout.guide_cam_information_fragment_1,
                                        Gravity.BOTTOM, 20)))
                        .show();
            }
        },Constants.delayDuration);

    }
}
