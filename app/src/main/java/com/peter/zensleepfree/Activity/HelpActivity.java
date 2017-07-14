package com.peter.zensleepfree.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.peter.zensleepfree.Fragments.Intros.Intro1Fragment;
import com.peter.zensleepfree.Fragments.Intros.Intro2Fragment;
import com.peter.zensleepfree.Fragments.Intros.Intro3Fragment;
import com.peter.zensleepfree.Fragments.Intros.Intro4Fragment;
import com.peter.zensleepfree.Fragments.Intros.Intro5Fragment;
import com.peter.zensleepfree.Interfaces.IntroPageButtonListener;
import com.peter.zensleepfree.R;

import me.relex.circleindicator.CircleIndicator;


public class HelpActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, IntroPageButtonListener {

    private static final String TAG = "Help Activity";
    private ViewPager fragmentPager;
    private CircleIndicator circleIndicator;
    private MyFragmentPagerAdapter pagerAdapter;
    private Intro1Fragment intro1Fragment;
    private Intro2Fragment intro2Fragment;
    private Intro3Fragment intro3Fragment;
    private Intro4Fragment intro4Fragment;
    private Intro5Fragment intro5Fragment;

    private IntroPageButtonListener callback = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        intro1Fragment = new Intro1Fragment().newInstance(callback);
        intro2Fragment = new Intro2Fragment().newInstance(callback);
        intro3Fragment = new Intro3Fragment().newInstance(callback);
        intro4Fragment = new Intro4Fragment().newInstance(callback);
        intro5Fragment = new Intro5Fragment().newInstance(callback);

        fragmentPager = (ViewPager) this.findViewById(R.id.helper_pager);
        circleIndicator = (CircleIndicator) this.findViewById(R.id.dot_indicator);

        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        fragmentPager.setAdapter(pagerAdapter);
        circleIndicator.setViewPager(fragmentPager);
        fragmentPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setTranslationX(page.getWidth() * -position);

                if(position <= -1.0F || position >= 1.0F) {
                    page.setAlpha(0.0F);
                } else if( position == 0.0F ) {
                    page.setAlpha(1.0F);
                } else {
                    // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    page.setAlpha(1.0F - Math.abs(position));
                }
            }
        });

        fragmentPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d(TAG, "Page " + String.valueOf(position) + " selected");
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            intro1Fragment.onPageSelected();
            intro3Fragment.onPageUnSelected();
        } else if (position == 2) {
            intro1Fragment.onPageUnSelected();
            intro3Fragment.onPageSelected();
        } else {
            intro1Fragment.onPageUnSelected();
            intro3Fragment.onPageUnSelected();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return intro1Fragment;
                case 1:
                    return intro2Fragment;
                case 2:
                    return intro3Fragment;
                case 3:
                    return intro4Fragment;
                case 4:
                    return intro5Fragment;
            }
            return null;
        }
    }
}
