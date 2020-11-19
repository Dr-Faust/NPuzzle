package com.alex_podolian.npuzzle.utils;

import androidx.viewpager.widget.ViewPager;

// Making your code shorter
public abstract class ViewPagerPageSelectedListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public abstract void onPageSelected(int position);

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
