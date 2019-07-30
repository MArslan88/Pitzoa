package com.example.pitzoa;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                PizzaFragment pizzaFragment = new PizzaFragment();
                return pizzaFragment;
            case 1:
                StartersFragment startersFragment = new StartersFragment();
                return startersFragment;
            case 2:
                DealsFragment dealsFragment = new DealsFragment();
                return dealsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // because we have three fragments
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Pizza";
            case 1:
                return "Starters";
            case 2:
                return "Deals";

            default:
                return null;
        }
    }
}
