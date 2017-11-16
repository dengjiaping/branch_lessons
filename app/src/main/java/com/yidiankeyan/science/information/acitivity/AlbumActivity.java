package com.yidiankeyan.science.information.acitivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.AlbumPagerAdapter;
import com.yidiankeyan.science.information.fragment.AllAlbumFragment;
import com.yidiankeyan.science.information.fragment.MineAlbumFragment;
import com.yidiankeyan.science.information.fragment.NewContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 *  -专题
 *      -更多专题
 */
public class AlbumActivity extends BaseActivity {

    private ViewPager vPager;
    private AlbumPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void initView() {
        vPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.ll_return).setOnClickListener(this);
        fragmentList.add(new NewContentFragment());
        fragmentList.add(new AllAlbumFragment());
        fragmentList.add(new MineAlbumFragment());
        adapter = new AlbumPagerAdapter(getSupportFragmentManager(), fragmentList);
        vPager.setAdapter(adapter);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_album;
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
