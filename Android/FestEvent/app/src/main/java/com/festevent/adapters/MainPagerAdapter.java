package com.festevent.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.festevent.fragments.ActualityFragment;
import com.festevent.fragments.EventsFragment;
import com.festevent.fragments.UserFragment;

import java.util.ArrayList;

/**
 * Created by hugo on 18/04/18.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
        add(new ActualityFragment());
        add(new EventsFragment());
        add(new UserFragment());
        add(new ActualityFragment());
    }};

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
