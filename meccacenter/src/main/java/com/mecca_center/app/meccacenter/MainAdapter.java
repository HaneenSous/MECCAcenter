package com.mecca_center.app.meccacenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moshx.indicators.tab.IconicProvider;

/**
 * Created by The_Dev on 2/3/2015.
 */
public class MainAdapter extends FragmentPagerAdapter implements IconicProvider {
    int[] iconsRes = new int[]{R.drawable.ic_action_access_time, R.drawable.map, R.drawable.compass, R.drawable.event};
    Fragment[] Frags;

    String[] titles = {"Prayer Time", "Contact", "Qublah", "Events"};


    public MainAdapter(FragmentManager fm) {
        super(fm);
        this.Frags = new Fragment[]{FragMoaqit_.builder().build(), FragMap_.builder().build()
                , CompassFragment_.builder().build(), FragEvent_.builder().build()};

    }

    @Override
    public Fragment getItem(int position) {
        return Frags[position];
    }

    @Override
    public int getCount() {
        return iconsRes.length;
    }


    @Override
    public int getIconicDrawable(int position) {
        return iconsRes[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}


