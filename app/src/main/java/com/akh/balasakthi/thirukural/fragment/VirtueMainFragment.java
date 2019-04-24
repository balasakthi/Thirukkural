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
import com.akh.balasakthi.thirukural.fragmentvirtue.AsceticVirtueFragment;
import com.akh.balasakthi.thirukural.fragmentvirtue.DomesticVirtueFragment;
import com.akh.balasakthi.thirukural.fragmentvirtue.FateFragment;
import com.akh.balasakthi.thirukural.fragmentvirtue.PrologueFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class VirtueMainFragment extends Fragment {


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 4;

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
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

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
                    return new PrologueFragment();
                case 1:
                    return new DomesticVirtueFragment();
                case 2:
                    return new AsceticVirtueFragment();
                case 3:
                    return new FateFragment();
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
                    return getResources().getString(R.string.tab_introduction);
                case 1:
                    return getResources().getString(R.string.tab_domestic_virtue);
                case 2:
                    return getResources().getString(R.string.tab_ascetic_virtue);
                case 3:
                    return getResources().getString(R.string.tab_fate);
            }
            return null;
        }
    }

}
