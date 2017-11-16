package com.yidiankeyan.science.app.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by zn on 2016/7/9.
 */
public class GuidePageAdapter extends PagerAdapter {
    private ArrayList<View> pageViews;

    private ArrayList<ImageView> ivPages;
    private boolean isDif = false;

    public GuidePageAdapter(ArrayList<View> pageViews) {
        this.pageViews = pageViews;
    }

    public GuidePageAdapter(ArrayList<ImageView> pageViews, boolean dif) {
        this.ivPages = pageViews;
        this.isDif = dif;
    }

    @Override
    public int getCount() {
        if (isDif) {
            return ivPages != null ? ivPages.size() : 0;
        } else {
            return pageViews != null ? pageViews.size() : 0;
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

    @Override
    public synchronized void destroyItem(View arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub
        if (isDif) {
            if (ivPages != null && ivPages.size() > arg1) {
                ((ViewPager) arg0).removeView(ivPages.get(arg1));
            }
        } else {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }
    }

    @Override
    public synchronized Object instantiateItem(View arg0, int arg1) {
        // TODO Auto-generated method stub
        if (isDif) {
            ((ViewPager) arg0).addView(ivPages.get(arg1));
            return ivPages.get(arg1);
        } else {
            ((ViewPager) arg0).addView(pageViews.get(arg1));
            return pageViews.get(arg1);
        }
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }
}
