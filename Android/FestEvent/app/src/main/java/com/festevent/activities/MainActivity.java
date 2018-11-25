package com.festevent.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.festevent.R;
import com.festevent.adapters.MainPagerAdapter;

import java.util.ArrayList;

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
/*        for (int i = 0; i < mNavigationView.getChildCount(); i++) {
            View tab = mNavigationView.getChildAt(i);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            System.out.println(height);
            tab.setPadding(0, -180, 0, 0);
            // the paddingTop will be modified when select/deselect,
            // so, in order to make the icon always center in tab,
            // we need set the paddingBottom equals paddingTop

            //icon.setPadding(0, icon.getPaddingTop(), 0, icon.getPaddingTop());

            //View title = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
            //title.setVisibility(View.GONE);
        }*/
    }

}
