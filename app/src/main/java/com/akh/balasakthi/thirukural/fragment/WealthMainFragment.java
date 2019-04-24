package com.akh.balasakthi.thirukural.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.fragmentwealth.FriendshipFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.MinistersOfStateFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.MiscellaneousFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.RoyaltyFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.TheEssentialsOfAStateFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.TheExcellenceOfAnArmyFragment;
import com.akh.balasakthi.thirukural.fragmentwealth.WayOfMakingWealthFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WealthMainFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 7;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.fragment_kuralsections, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new WealthMainFragment.MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RoyaltyFragment();
                case 1:
                    return new MinistersOfStateFragment();
                case 2:
                    return new TheEssentialsOfAStateFragment();
                case 3:
                    return new WayOfMakingWealthFragment();
                case 4:
                    return new TheExcellenceOfAnArmyFragment();
                case 5:
                    return new FriendshipFragment();
                case 6:
                    return new MiscellaneousFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_royalty);
                case 1:
                    return getResources().getString(R.string.tab_ministers_of_state);
                case 2:
                    return getResources().getString(R.string.tab_the_essentials_of_a_state);
                case 3:
                    return getResources().getString(R.string.tab_way_of_making_wealth);
                case 4:
                    return getResources().getString(R.string.tab_excellence_of_an_army);
                case 5:
                    return getResources().getString(R.string.tab_friendship);
                case 6:
                    return getResources().getString(R.string.tab_miscellaneous);
            }
            return null;
        }
    }

}
