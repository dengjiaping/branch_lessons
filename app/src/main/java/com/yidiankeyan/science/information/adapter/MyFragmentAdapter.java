package com.yidiankeyan.science.information.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 *
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private List<String> titlelist;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> list, List<String> titlelist) {
        super(fm);
        this.list=list;
        this.titlelist=titlelist;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }
}
