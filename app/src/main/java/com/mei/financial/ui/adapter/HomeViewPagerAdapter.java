package com.mei.financial.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/5/28
 */
public class HomeViewPagerAdapter extends PagerAdapter {

    public List<LinearLayout> mLayoutList = new ArrayList<>();

    public HomeViewPagerAdapter(List<LinearLayout> layoutList) {
        this.mLayoutList = layoutList;
    }

    @Override
    public int getCount() {
        return mLayoutList == null ? 0 : mLayoutList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mLayoutList.get(position));
        return mLayoutList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mLayoutList.get(position));
    }
}
