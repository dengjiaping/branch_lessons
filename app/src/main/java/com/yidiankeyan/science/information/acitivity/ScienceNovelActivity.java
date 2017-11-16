package com.yidiankeyan.science.information.acitivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.fragment.BookedAlbumForScienceNovelFragment;
import com.yidiankeyan.science.information.fragment.NovelAllFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 * -科技小说
 */
public class ScienceNovelActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageView titleReturn;
    private TextView maintitleTxt;
    private ImageButton titleBtn;
//    private TextView tvRecentContent;
    private TextView tvAllAlbum;
    private TextView tvSubscribedAlbum;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected int setContentView() {
        return R.layout.activity_science_novel;
    }

    @Override
    protected void initView() {
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleReturn = (ImageView) findViewById(R.id.title_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
//        tvRecentContent = (TextView) findViewById(R.id.tv_recent_content);
        tvAllAlbum = (TextView) findViewById(R.id.tv_all_album);
        tvSubscribedAlbum = (TextView) findViewById(R.id.tv_subscribed_album);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        txtTitle.setText("科技小说");
    }

    @Override
    protected void initAction() {
        titleBtn.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        fragments = new ArrayList<>();
//        fragments.add(new NovelNewestFragment());
        fragments.add(new NovelAllFragment());
        fragments.add(new BookedAlbumForScienceNovelFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        llReturn.setOnClickListener(this);
//        tvRecentContent.setOnClickListener(this);
        tvAllAlbum.setOnClickListener(this);
        tvSubscribedAlbum.setOnClickListener(this);
        tvAllAlbum.setTextColor(Color.parseColor("#F1312E"));
        maintitleTxt.setText("墨子小说");
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
//            case R.id.tv_recent_content:
//                currIndex = 0;
//                viewPager.setCurrentItem(currIndex);
//                break;
            case R.id.tv_all_album:
                currIndex = 0;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_subscribed_album:
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
//                case 0:
//                    tvRecentContent.setTextColor(Color.parseColor("#F1312E"));
//                    break;
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
//        tvRecentContent.setTextColor(Color.parseColor("#999999"));
        tvAllAlbum.setTextColor(Color.parseColor("#999999"));
        tvSubscribedAlbum.setTextColor(Color.parseColor("#999999"));
    }

}
