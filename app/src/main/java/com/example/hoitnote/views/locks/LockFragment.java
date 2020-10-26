package com.example.hoitnote.views.locks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.RelativeGuide;
import com.example.hoitnote.R;
import com.example.hoitnote.adapters.locks.LockFragmentAdapter;
import com.example.hoitnote.databinding.FragmentLockBinding;
import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.viewmodels.BaseLockViewModel;

import java.util.ArrayList;

public class LockFragment extends Fragment {
    public FragmentManager fragmentManager;
    public Lifecycle lifecycle;
    public ArrayList<BaseLockViewModel> baseLockViewModels;
    public Context context;
    public Config pinConfig;
    public Config traditionalConfig;
    public Config fingerprintConfig;

    FragmentLockBinding binding;

    ArrayList<View> icons = new ArrayList<>();

    public LockFragment(){

    }

    public LockFragment(@NonNull FragmentManager fragmentManager,
                        @NonNull Lifecycle lifecycle,
                        ArrayList<BaseLockViewModel> baseLockViewModels,
                        Context context,
                        Config pinConfig,
                        Config traditionalConfig,
                        Config fingerprintConfig){
        this.fragmentManager = fragmentManager;
        this.lifecycle = lifecycle;
        this.baseLockViewModels = baseLockViewModels;
        this.context = context;
        this.pinConfig = pinConfig;
        this.traditionalConfig = traditionalConfig;
        this.fingerprintConfig = fingerprintConfig;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lock,
                container,
                false
        ) ;
        binding.viewPager.setAdapter(new LockFragmentAdapter(this));
        binding.viewPager.setUserInputEnabled(false);
        binding.setLockFragment(this);

        icons.add(binding.fingerprintLock);
        icons.add(binding.patternLock);
        icons.add(binding.traditionalLock);
        icons.add(binding.syncSettings);

        renderIcons(binding.fingerprintLock);
        return  binding.getRoot();
    }


    public void switchTheme(View view) {
        switch (view.getId()){
            case R.id.fingerprintLock:
                binding.viewPager.setCurrentItem(0,true);
                renderIcons(binding.fingerprintLock);
                break;
            case R.id.patternLock:
                binding.viewPager.setCurrentItem(1,true);
                renderIcons(binding.patternLock);
                break;
            case R.id.traditionalLock:
                binding.viewPager.setCurrentItem(2,true);
                renderIcons(binding.traditionalLock);
                break;
            case R.id.syncSettings:
                binding.viewPager.setCurrentItem(3,true);
                renderIcons(binding.syncSettings);
                break;
            default:
        }
    }

    private void renderIcons(View targetIcon){
        for (View icon:
             icons) {
            if(icon == targetIcon){
                icon.animate().scaleX(1.3f).scaleY(1.3f).start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    icon.setElevation(10.0f);
                }
            }
            else{
                icon.animate().scaleX(1).scaleY(1).start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    icon.setElevation(0);
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = 0;
        rectF.top = 0;
        rectF.bottom = 0;
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void run() {
                NewbieGuide.with(LockFragment.this)
                        .setLabel("guide_lock")
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(rectF).setLayoutRes(R.layout.guide_lock_all))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(rectF).setLayoutRes(R.layout.guide_lock_information))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(binding.fingerprintLock, new RelativeGuide(R.layout.guide_lock_fingerprint,
                                        Gravity.TOP, 20)))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(binding.patternLock, new RelativeGuide(R.layout.guide_lock_pattern,
                                        Gravity.TOP, 20)))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(binding.traditionalLock, new RelativeGuide(R.layout.guide_lock_text,
                                        Gravity.TOP, 20)))
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLight(binding.syncSettings, new RelativeGuide(R.layout.guide_lock_syn,
                                        Gravity.TOP, 20)))
                        .show();
            }
        },800);
    }
}
