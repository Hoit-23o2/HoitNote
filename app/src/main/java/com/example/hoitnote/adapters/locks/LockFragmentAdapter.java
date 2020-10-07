package com.example.hoitnote.adapters.locks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hoitnote.utils.commuications.Config;
import com.example.hoitnote.viewmodels.BaseLockViewModel;
import com.example.hoitnote.views.locks.DataSyncFragment;
import com.example.hoitnote.views.passwordfragments.FingerprintPasswordFragment;
import com.example.hoitnote.views.locks.LockFragment;
import com.example.hoitnote.views.passwordfragments.PINPasswordFragment;
import com.example.hoitnote.views.passwordfragments.TraditionalPasswordFragment;

import java.util.ArrayList;

public class LockFragmentAdapter extends FragmentStateAdapter {

    ArrayList<BaseLockViewModel> baseLockViewModels;
    Context context;
    Config pinConfig;
    Config traditionalConfig;
    Config fingerprintConfig;

    FingerprintPasswordFragment fingerprintPasswordFragment;
    TraditionalPasswordFragment traditionalPasswordFragment;
    PINPasswordFragment pinPasswordFragment;
    DataSyncFragment dataSyncFragment;

    ArrayList<Fragment> fragments = new ArrayList<>();

    public LockFragmentAdapter(LockFragment lockFragment) {
        super(lockFragment.fragmentManager, lockFragment.lifecycle);
        this.baseLockViewModels = lockFragment.baseLockViewModels;
        this.context = lockFragment.context;
        this.pinConfig = lockFragment.pinConfig;
        this.traditionalConfig = lockFragment.traditionalConfig;
        this.fingerprintConfig = lockFragment.fingerprintConfig;

        this.fingerprintPasswordFragment = new FingerprintPasswordFragment(baseLockViewModels.get(0),context,fingerprintConfig);
        this.pinPasswordFragment = new PINPasswordFragment(baseLockViewModels.get(1),context,pinConfig);
        this.traditionalPasswordFragment = new TraditionalPasswordFragment(baseLockViewModels.get(2),context,traditionalConfig);
        this.dataSyncFragment = new DataSyncFragment();

        fragments.add(fingerprintPasswordFragment);
        fragments.add(pinPasswordFragment);
        fragments.add(traditionalPasswordFragment);
        fragments.add(dataSyncFragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
