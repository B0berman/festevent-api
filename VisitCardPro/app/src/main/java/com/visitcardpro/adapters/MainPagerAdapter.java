package com.visitcardpro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.visitcardpro.fragments.CardsFragment;
import com.visitcardpro.fragments.ContactsFragment;
import com.visitcardpro.fragments.UserFragment;

import java.util.ArrayList;

/**
 * Created by hugo on 18/04/18.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
        add(new ContactsFragment());
        add(new CardsFragment());
        add(new UserFragment());
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
