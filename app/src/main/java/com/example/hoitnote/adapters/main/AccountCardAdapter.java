package com.example.hoitnote.adapters.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hoitnote.customviews.AccountCardFragment;
import com.example.hoitnote.viewmodels.AccountCardViewModel;

import java.util.ArrayList;

public class AccountCardAdapter extends FragmentStateAdapter {
    ArrayList<AccountCardFragment> AccountCardFragments;

    public AccountCardAdapter(@NonNull FragmentManager fragmentManager,
                              @NonNull Lifecycle lifecycle,
                              ArrayList<AccountCardFragment> AccountCardFragments) {
        super(fragmentManager, lifecycle);
        this.AccountCardFragments = AccountCardFragments;
        /*这是添加按钮页Banner*/
        AccountCardFragment addFragment = new AccountCardFragment(
                new AccountCardViewModel(
                        null,
                        null,
                        null,
                        null,
                        null,
                        false
                )
        );
        this.AccountCardFragments.add(addFragment);
    }


    public void addAccountCard(ViewPager2 container, AccountCardFragment accountCardFragment){
        container.setAdapter(null);
        this.AccountCardFragments.add(0, accountCardFragment);
        container.setAdapter(this);
    }

    public void removeAccountCard(ViewPager2 container, int pos){
        container.setAdapter(null);
        if(pos == this.AccountCardFragments.size() - 1){
            return;
        }
        else{
            this.AccountCardFragments.remove(this.AccountCardFragments.get(pos));
        }
        container.setAdapter(this);
    }


    public AccountCardFragment getFragment(int position){
        return this.AccountCardFragments.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.AccountCardFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return this.AccountCardFragments.size();
    }
}
