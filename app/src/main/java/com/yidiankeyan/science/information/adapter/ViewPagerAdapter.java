package com.yidiankeyan.science.information.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 头部轮播适配
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imgList=new ArrayList<>();//list存放所有的imageview
    private Context context;

    public ViewPagerAdapter(List<ImageView> imgList, Context context){
        this.imgList=imgList;
        this.context=context;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imgList.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imgList.get(position));

        /**
         * 设置每个item的点击事件
         */
//        ImageView img = imgList.get(position);
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        return imgList.get(position);
    }

    @Override
    public int getCount() {

        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return (arg0 == arg1);
    }
}
