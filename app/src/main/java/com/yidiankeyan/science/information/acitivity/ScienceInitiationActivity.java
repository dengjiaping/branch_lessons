package com.yidiankeyan.science.information.acitivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.fragment.BookedAlbumForScienceInitiationFragment;
import com.yidiankeyan.science.information.fragment.InitiationContentFragment;
import com.yidiankeyan.science.information.fragment.WholeAlbumForScienceInitiationFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 * -科学启蒙
 */
public class ScienceInitiationActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageButton btnTitle;

    private ViewPager viewPager;// 页卡内容
    private TextView txtContent, txtAll, txtBooked;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private int selectedColor, unSelectedColor;
    private MyPagerAdapter adapter;
    /**
     * 页卡总数
     **/
    private static final int pageSize = 3;
    private InitiationContentFragment initiationContentFragment;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected int setContentView() {
        return R.layout.activity_science_initiation;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        selectedColor = getResources()
                .getColor(R.color.heione);
        unSelectedColor = getResources().getColor(
                R.color.qianhui);
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtAll = (TextView) findViewById(R.id.txt_all);
        txtBooked = (TextView) findViewById(R.id.tab_booked);

        viewPager = (ViewPager) findViewById(R.id.vp_whiz);
        fragments = new ArrayList<>();
        txtTitle.setText("科学启蒙");

    }

    @Override
    protected void initAction() {
        btnTitle.setVisibility(View.GONE);
        llReturn.setOnClickListener(this);
        initiationContentFragment = new InitiationContentFragment();
        fragments.add(initiationContentFragment);
        fragments.add(new WholeAlbumForScienceInitiationFragment());
        fragments.add(new BookedAlbumForScienceInitiationFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        //增加缓冲页为当前页的前两页和后两页，如果不设置默认缓冲页为前一页和后一页
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        txtContent.setTextColor(selectedColor);
        txtAll.setTextColor(unSelectedColor);
        txtBooked.setTextColor(unSelectedColor);

        txtContent.setText("最新内容");
        txtAll.setText("所有专辑");
        txtBooked.setText("已订专辑");

        txtContent.setOnClickListener(new MyOnClickListener(0));
        txtAll.setOnClickListener(new MyOnClickListener(1));
        txtBooked.setOnClickListener(new MyOnClickListener(2));
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BusinessNumberActivity.ACTION_VIDEO_PLAY_START);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initiationContentFragment.onVideoPlaying();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        initiationContentFragment.destroyMediaPlayer();
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            switch (index) {
                case 0:
                    txtContent.setTextColor(selectedColor);
                    txtAll.setTextColor(unSelectedColor);
                    txtBooked.setTextColor(unSelectedColor);
                    break;
                case 1:
                    txtAll.setTextColor(selectedColor);
                    txtBooked.setTextColor(unSelectedColor);
                    txtContent.setTextColor(unSelectedColor);
                    break;
                case 2:
                    txtBooked.setTextColor(selectedColor);
                    txtContent.setTextColor(unSelectedColor);
                    txtAll.setTextColor(unSelectedColor);
                    break;
            }
            viewPager.setCurrentItem(index);
        }
    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            currIndex = index;

            switch (index) {
                case 0:
                    txtContent.setTextColor(selectedColor);
                    txtAll.setTextColor(unSelectedColor);
                    txtBooked.setTextColor(unSelectedColor);
                    break;
                case 1:
                    txtAll.setTextColor(selectedColor);
                    txtBooked.setTextColor(unSelectedColor);
                    txtContent.setTextColor(unSelectedColor);
                    break;
                case 2:
                    txtBooked.setTextColor(selectedColor);
                    txtContent.setTextColor(unSelectedColor);
                    txtAll.setTextColor(unSelectedColor);
                    break;
            }
        }
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
