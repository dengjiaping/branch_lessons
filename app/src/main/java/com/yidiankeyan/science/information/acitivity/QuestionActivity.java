package com.yidiankeyan.science.information.acitivity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.information.adapter.MyPagerAdapter;
import com.yidiankeyan.science.information.fragment.AllAnswerFragment;
import com.yidiankeyan.science.information.fragment.AttentionAnswerFragment;
import com.yidiankeyan.science.information.fragment.MyQuestionFragment;
import com.yidiankeyan.science.information.fragment.NewAnswerFragment;
import com.yidiankeyan.science.utils.SystemConstant;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 我要问
 */
public class QuestionActivity extends BaseActivity {

    private AutoLinearLayout llReturn;
    private TextView maintitleTxt;
    private ImageButton titleBtn;
    private TextView tvAttention;
    //    private TextView tvAuthenticate;
    private TextView tvNew;
    private TextView tvAll;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;// Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    private TextView tvQuestion;

    @Override
    protected int setContentView() {
        return R.layout.activity_question;
    }

    @Override
    protected void initView() {
        llReturn = (AutoLinearLayout) findViewById(R.id.ll_return);
        maintitleTxt = (TextView) findViewById(R.id.maintitle_txt);
        titleBtn = (ImageButton) findViewById(R.id.title_btn);
        tvAttention = (TextView) findViewById(R.id.tv_attention);
//        tvAuthenticate = (TextView) findViewById(R.id.tv_authenticate);
        tvNew = (TextView) findViewById(R.id.tv_new);
        tvAll = (TextView) findViewById(R.id.tv_all);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        maintitleTxt.setText("我要问Ta");
    }

    @Override
    protected void initAction() {
        llReturn.setOnClickListener(this);
        titleBtn.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(new MyQuestionFragment());
        fragments.add(new AttentionAnswerFragment());
        fragments.add(new NewAnswerFragment());
        fragments.add(new AllAnswerFragment());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        tvAttention.setOnClickListener(this);
//        tvAuthenticate.setOnClickListener(this);
        tvNew.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvQuestion.setOnClickListener(this);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        tvQuestion.setTextColor(Color.parseColor("#F1312E"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                finish();
                break;
            case R.id.tv_question:
                currIndex = 0;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_attention:
                currIndex = 1;
                viewPager.setCurrentItem(currIndex);
                break;
//            case R.id.tv_authenticate:
//                currIndex = 1;
//                viewPager.setCurrentItem(currIndex);
//                break;
            case R.id.tv_new:
                currIndex = 2;
                viewPager.setCurrentItem(currIndex);
                break;
            case R.id.tv_all:
                currIndex = 3;
                viewPager.setCurrentItem(currIndex);
                break;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            setNormal();
            switch (currIndex) {
                case 0:
                    tvQuestion.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 1:
                    tvAttention.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 2:
                    tvNew.setTextColor(Color.parseColor("#F1312E"));
                    break;
                case 3:
                    tvAll.setTextColor(Color.parseColor("#F1312E"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setNormal() {
        tvQuestion.setTextColor(Color.parseColor("#999999"));
        tvAttention.setTextColor(Color.parseColor("#999999"));
        tvNew.setTextColor(Color.parseColor("#999999"));
        tvAll.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventMsg msg = EventMsg.obtain(SystemConstant.ON_KEDA_NOTICE);
        msg.setArg1(1);
        EventBus.getDefault().post(msg);
    }
}
