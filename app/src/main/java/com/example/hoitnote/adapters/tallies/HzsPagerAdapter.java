package com.example.hoitnote.adapters.tallies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class HzsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> title_list;
    private ArrayList<Fragment> fragment_list;
    public HzsPagerAdapter(FragmentManager fragmentManager, ArrayList<String> title_list, ArrayList<Fragment> fragment_list){
        super(fragmentManager);
        this.title_list = title_list;
        this.fragment_list = fragment_list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }
}
