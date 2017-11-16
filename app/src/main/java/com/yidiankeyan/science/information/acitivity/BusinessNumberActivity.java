package com.yidiankeyan.science.information.acitivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.fragment.BusinessAllFragment;
import com.yidiankeyan.science.information.fragment.NumberBookedFragment;
import com.yidiankeyan.science.information.fragment.RecentContentFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐
 * -企业号
 */
public class BusinessNumberActivity extends BaseActivity {

    private TextView txtTitle;
    private AutoLinearLayout llReturn;
    private ImageView titleReturn;
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
    private BroadcastReceiver broadcastReceiver;
    public static String ACTION_VIDEO_PLAY_START = "action_video_play_start";
    private RecentContentFragment recentContentFragment;
    private AutoLinearLayout llBus;
    private boolean mFlag;

    @Override
    protected int setContentView() {
        return R.layout.activity_business_number;
    }

    @Override
    protected void initView() {
        llBus = (AutoLinearLayout) findViewById(R.id.ll_bus);
        btnTitle = (ImageButton) findViewById(R.id.title_btn);
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        titleReturn = (ImageView) findViewById(R.id.title_return);
        txtTitle = (TextView) findViewById(R.id.maintitle_txt);
        selectedColor = getResources()
                .getColor(R.color.main_orange);
        unSelectedColor = getResources().getColor(
                R.color.qianhui);
        txtContent = (TextView) findViewById(R.id.txt_zuixin);
        txtAll = (TextView) findViewById(R.id.txt_whole);
        txtBooked = (TextView) findViewById(R.id.txt_booked);

        viewPager = (ViewPager) findViewById(R.id.vp_business);
        fragments = new ArrayList<>();

    }

    @Override
    protected void initAction() {
        btnTitle.setVisibility(View.GONE);
        if (TextUtils.isEmpty(getIntent().getStringExtra("title")))
            txtTitle.setText("企业号");
        else
            txtTitle.setText(getIntent().getStringExtra("title"));
        llReturn.setOnClickListener(this);
        recentContentFragment = new RecentContentFragment();
        fragments.add(recentContentFragment);
        fragments.add(new BusinessAllFragment());
        fragments.add(new NumberBookedFragment());
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
        intentFilter.addAction(ACTION_VIDEO_PLAY_START);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                recentContentFragment.onVideoPlaying();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    protected void toggleSystemStatusBar() {
        /*int flag = 0; 这种方法只能隐藏部分状态栏的icon
        flag = View.STATUS_BAR_HIDDEN;
        this.getWindow().getDecorView().setSystemUiVisibility(flag);
        this.getWindow().getDecorView().requestLayout();*/
        if(mFlag){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //如果使用下面这个选项则无法再切会全屏模式
            /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); */
            mFlag = false;
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mFlag  = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        recentContentFragment.destroyMediaPlayer();
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
