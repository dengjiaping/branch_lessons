package com.yidiankeyan.science.information.acitivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.InfomationVPAdapter;
import com.yidiankeyan.science.information.fragment.AlbumChartsRankingFragment;
import com.yidiankeyan.science.information.fragment.EditorChartsRankingFragment;
import com.yidiankeyan.science.information.fragment.PayTopRankingFragment;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titles; //tab名称列表
    private List<Fragment> fragments;// Tab页面列表
    private InfomationVPAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        AlbumChartsRankingFragment graphicFragment = new AlbumChartsRankingFragment();
        Bundle graphic = new Bundle();
        graphic.putInt("type", 4);
        graphicFragment.setArguments(graphic);
        fragments.add(graphicFragment);

        AlbumChartsRankingFragment audioFragment = new AlbumChartsRankingFragment();
        Bundle audio = new Bundle();
        audio.putInt("type", 5);
        audioFragment.setArguments(audio);
        fragments.add(audioFragment);

        AlbumChartsRankingFragment videoFragment = new AlbumChartsRankingFragment();
        Bundle video = new Bundle();
        video.putInt("type", 6);
        videoFragment.setArguments(video);
        fragments.add(videoFragment);

        AlbumChartsRankingFragment subFragment = new AlbumChartsRankingFragment();
        Bundle sub = new Bundle();
        sub.putInt("type", 2);
        subFragment.setArguments(sub);
        fragments.add(subFragment);

        fragments.add(new EditorChartsRankingFragment());
        fragments.add(new PayTopRankingFragment());

        titles = new ArrayList<>();
        titles.add("图文");
        titles.add("音频");
        titles.add("视频");
        titles.add("订阅");
        titles.add("主编");
        titles.add("付费");

        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(4)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(5)));
        adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, titles);

        //viewpager加载adapter
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
        }
    }
}
