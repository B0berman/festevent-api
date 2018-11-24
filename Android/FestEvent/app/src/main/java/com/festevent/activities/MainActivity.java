package com.festevent.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.festevent.R;
import com.festevent.adapters.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private MenuItem prevMenuItem = null;
    private BottomNavigationView mNavigationView;
    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_actuality:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_event:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profil:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_all:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
            }
    };
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            }
            else {
                mNavigationView.getMenu().getItem(0).setChecked(false);
            }

            mNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = mNavigationView.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        mNavigationView = findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(mPageChangeListener);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
    }

}
