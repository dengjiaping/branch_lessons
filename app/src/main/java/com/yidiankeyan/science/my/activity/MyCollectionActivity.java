package com.yidiankeyan.science.my.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.InfomationVPAdapter;
import com.yidiankeyan.science.my.fragment.CollectionInterviewFragment;
import com.yidiankeyan.science.my.fragment.CollectionRikeFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/***
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<String> titles; //tab名称列表
    private List<Fragment> fragments;// Tab页面列表
    private InfomationVPAdapter adapter;
    private MagicIndicator magicIndicator;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
    }

    @Override
    protected void initAction() {
        findViewById(R.id.img_return).setOnClickListener(this);
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
//        fragments.add(new CollectionGraphicFragment());
//        fragments.add(new CollectionAudioFragment());
//        fragments.add(new CollectionVideoFragment());
        fragments.add(new CollectionRikeFragment());
        fragments.add(new CollectionInterviewFragment());

        titles = new ArrayList<>();
//        titles.add("图文");
//        titles.add("音频");
//        titles.add("视频");
        titles.add("日课");
        titles.add("专访");

        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setText(titles.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 2));
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 50));
                linePagerIndicator.setColors(Color.parseColor("#f1312e"));
                return linePagerIndicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        adapter = new InfomationVPAdapter(getSupportFragmentManager(), fragments, titles);
        //viewpager加载adapter
        viewPager.setAdapter(adapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }
}
