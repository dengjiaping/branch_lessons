package com.yidiankeyan.science.information.acitivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.fragment.ClsAudioHottestFragment;
import com.yidiankeyan.science.information.fragment.ClsAudioNewestFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 *  -音频专辑
 */
public class ClassAudioActivity extends BaseActivity {


    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton titleBtn;
    private TextView tvAllAlbum;
    private TextView tvSubscribedAlbum;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected int setContentView() {
        return R.layout.activity_class_audio;
    }
    @Override
    protected void initView() {

        txtTitle= (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        tvAllAlbum = (TextView) findViewById(R.id.txt_audioall);
        tvSubscribedAlbum = (TextView) findViewById(R.id.txt_audiobooked);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        txtTitle.setText("音频专辑");
    }

    @Override
    protected void initAction() {
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        fragments = new ArrayList<>();
        fragments.add(new ClsAudioNewestFragment());
        fragments.add(new ClsAudioHottestFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        llReturn.setOnClickListener(this);
        tvAllAlbum.setOnClickListener(this);
        tvSubscribedAlbum.setOnClickListener(this);
        tvAllAlbum.setTextColor(Color.parseColor("#F1312E"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.txt_audioall:
                currIndex =0;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.txt_audiobooked:
                currIndex = 1;
                viewPager.setCurrentItem(currIndex);
                break;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
                    tvAllAlbum.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
                    tvSubscribedAlbum.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        tvAllAlbum.setTextColor(Color.parseColor("#999999"));
        tvSubscribedAlbum.setTextColor(Color.parseColor("#999999"));
    }
}
